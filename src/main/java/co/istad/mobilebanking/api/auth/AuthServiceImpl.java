package co.istad.mobilebanking.api.auth;

import co.istad.mobilebanking.api.auth.web.RegisterDto;
import co.istad.mobilebanking.api.user.User;
import co.istad.mobilebanking.api.user.UserMapStruct;
import co.istad.mobilebanking.api.user.UserMapper;
import co.istad.mobilebanking.ulti.MailUntil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthMapper authMapper;
    private final UserMapper userMapper;
    private final UserMapStruct userMapStruct;
    private final PasswordEncoder encoder;
    private final MailUntil mailUntil;

    @Value("${spring.mail.username}")
    private String appMail;

    @Override
    public void register(RegisterDto registerDto) {

        User user = userMapStruct.registerDtoToUser(registerDto);
        user.setIsVerified(false);
        user.setPassword(encoder.encode(user.getPassword()));

        log.info("Registering user: {}", user.getEmail());
         authMapper.register(user);

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
