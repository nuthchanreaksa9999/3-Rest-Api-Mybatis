package co.istad.mobilebanking.api.auth;

import co.istad.mobilebanking.api.auth.web.AuthDto;
import co.istad.mobilebanking.api.auth.web.LogInDto;
import co.istad.mobilebanking.api.auth.web.RegisterDto;
import co.istad.mobilebanking.api.auth.web.TokenDto;
import co.istad.mobilebanking.api.user.User;
import co.istad.mobilebanking.api.user.UserMapStruct;
import co.istad.mobilebanking.api.user.UserMapper;
import co.istad.mobilebanking.ulti.MailUntil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthMapper authMapper;
    private final UserMapper userMapper;
    private final UserMapStruct userMapStruct;
    private final PasswordEncoder encoder;
    private final MailUntil mailUntil;
    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final JwtEncoder jwtAccessTokenEncoder;

    private JwtEncoder jwtRefreshTokenEncoder;

    @Autowired
    public void setJwtRefreshTokenEncoder(@Qualifier("jwtRefreshTokenEncoder") JwtEncoder jwtEncoder) {
        this.jwtRefreshTokenEncoder = jwtEncoder;
    }

    @Value("${spring.mail.username}")
    private String appMail;

    @Override
    public AuthDto refreshToken(TokenDto tokenDto) {

        Authentication authentication = new BearerTokenAuthenticationToken(tokenDto.refreshToken());
        authentication =  jwtAuthenticationProvider.authenticate(authentication);
        System.out.println("Refresh: " + authentication.getName());

        Instant now = Instant.now();

        Jwt jwt = (Jwt) authentication.getCredentials();

        System.out.println(jwt.getSubject());
        System.out.println(jwt.getClaims());
        System.out.println(jwt.getClaimAsString("scope"));

        JwtClaimsSet jwtAccessTokenClaimSet = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .subject(jwt.getSubject())
                .expiresAt(now.plus(1, ChronoUnit.SECONDS))
                .claim("scope", jwt.getClaimAsString("scope"))
                .build();

        String accessToken = jwtAccessTokenEncoder.encode(
                JwtEncoderParameters.from(jwtAccessTokenClaimSet)
        ).getTokenValue();

        return new AuthDto("Bearer",
                accessToken,
                tokenDto.refreshToken()
                );
    }

    @Override
    public AuthDto login(LogInDto logInDto) {

        Authentication authentication = new UsernamePasswordAuthenticationToken(logInDto.email(), logInDto.password());

        authentication = daoAuthenticationProvider.authenticate(authentication);

        // Create Time now
        Instant now = Instant.now();

        //  Define scope
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(auth -> !auth.startsWith("ROLE_"))
                .collect(Collectors.joining(" "));

/*        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("WRITE"));
        authorities.add(new SimpleGrantedAuthority("READ"));
        authorities.add(new SimpleGrantedAuthority("DELETE"));
        authorities.add(new SimpleGrantedAuthority("UPDATE"));
        authorities.add(new SimpleGrantedAuthority("FULL_CONTROL"));*/

/*        String scope = authorities.stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(" "));*/


        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .subject(authentication.getName())
                .expiresAt(now.plus(1, ChronoUnit.SECONDS))
                .claim("scope", scope)
                .build();

        JwtClaimsSet jwtRefreshClaimsSet = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .subject(authentication.getName())
                .expiresAt(now.plus(5, ChronoUnit.HOURS))
                .claim("scope", scope)
                .build();

        String accessToken = jwtAccessTokenEncoder.encode(
                JwtEncoderParameters.from(jwtClaimsSet)
        ).getTokenValue();

        String refreshToken = jwtRefreshTokenEncoder.encode(
                JwtEncoderParameters.from(jwtRefreshClaimsSet)
        ).getTokenValue();

        return new AuthDto("Bearer", accessToken, refreshToken);

        // first test with auth header
/*        log.info("Authenticated user: {}", authentication);
        log.info("Authenticated name: {}", authentication.getName());
        log.info("Authenticated credentials: {}", authentication.getCredentials());

        // Logic on basic authorization header
        String basicAuthFormat = authentication.getName() + ":" + authentication.getCredentials();
        String encoding = Base64.getEncoder().encodeToString(basicAuthFormat.getBytes());

        log.info("Basic: {}", encoding);

        return new AuthDto(String.format("Basic %s", encoding));*/
    }

    @Transactional
    @Override
    public void register(RegisterDto registerDto) {

        User user = userMapStruct.registerDtoToUser(registerDto);
        user.setIsVerified(false);
        user.setPassword(encoder.encode(user.getPassword()));

        log.info("Registering user: {}", user.getEmail());

        if (authMapper.register(user)){
            // Create user roles
            for(Integer role : registerDto.roleIds()){
                authMapper.createUserRole(user.getId(), role);

            }
        }
    }

    @Override
    public void verify(String email) {

        User user = authMapper.selectByEmail(email).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND
        , "User with email " + email + " not found"));

        String verifiedCode =  UUID.randomUUID().toString();

       if ( authMapper.updateVerifiedCode(email, verifiedCode)){
           user.setVerifiedCode(verifiedCode);
       } else {
           throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                   "User cannot be verified"
                   );
       }

        user.setVerifiedCode(verifiedCode);

        Map<String, Object> data = new HashMap<>();
        data.put("email", email);
        data.put("verifiedCode", verifiedCode);

        MailUntil.Meta<?> meta = MailUntil.Meta.builder()
                .from(appMail)
                .to(email)
                .subject("Verification email")
                .templateUrl("auth/verify")
                .data(data)
                .build();

        try {
            mailUntil.send(meta);
        } catch (MessagingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage()
                    );
        }
    }

    @Override
    public void checkVerify(String email, String verifiedCode) {

        User user = authMapper.selectByEmailAndVerifiedCode(email, verifiedCode).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User with email " + email + " not found in Database"));

        if (!user.getIsVerified()) {
            authMapper.verify(email, verifiedCode);
        }
    }
}
