package co.istad.mobilebanking.security;

import co.istad.mobilebanking.ulti.KeyUtil;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.UUID;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final KeyUtil keyUtil;

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {

        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService);
        auth.setPasswordEncoder(passwordEncoder);

        return auth;
    }

    @Bean(name = "jwtRefreshTokenAuthProvider")
    public JwtAuthenticationProvider jwtRefreshTokenAuthProvider()  throws NoSuchAlgorithmException, InvalidKeySpecException {

        JwtAuthenticationProvider provider =  new JwtAuthenticationProvider(jwtRefreshTokenDecoder());
        provider.setJwtAuthenticationConverter(jwtAuthenticationConverter());

        return provider;

    }

    // To create refresh token
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
       // JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
       // grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
        // jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

        return new JwtAuthenticationConverter();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // Disable CSRF
        http.csrf(csrf -> csrf.disable());

        // Authorize url mapping
        http.authorizeHttpRequests(auth -> {
                    auth
                                    .requestMatchers("/api/v1/auth/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/v1/users/**").hasAuthority("SCOPE_user:read")
                            .requestMatchers(HttpMethod.POST, "/api/v1/users/**").hasAuthority("SCOPE_user:write")
                            .requestMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasAuthority("SCOPE_user:delete")
                            .requestMatchers(HttpMethod.PUT, "/api/v1/users/**").hasAuthority("SCOPE_user:update")

//                            .requestMatchers(HttpMethod.POST, "/api/v1/users/**").hasAnyAuthority("SCOPE_WRITE", "SCOPE_FULL_CONTROL")
//                            .requestMatchers(HttpMethod.PUT, "/api/v1/users/**").hasAnyAuthority("SCOPE_UPDATE", "SCOPE_FULL_CONTROL")
//                            .requestMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasAnyAuthority("SCOPE_DELETE", "SCOPE_FULL_CONTROL")


                            .anyRequest().authenticated()
                            ;
                        }
                )

                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .jwt(Customizer
                                .withDefaults())
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                )
                .sessionManagement(session -> session
                        // make security http STATELESS
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // Exception
                .exceptionHandling(exception -> exception.authenticationEntryPoint(customAuthenticationEntryPoint)

        );

                // Security mechanism
                //.httpBasic(httpBasic -> {});

            return http.build();

    }

    // Use RSA public Key for Decoding
    // Using for verify token
    @Bean
    @Primary
    public JwtDecoder jwtAccessTokenDecoder() {
        return NimbusJwtDecoder.withPublicKey(keyUtil.getAccessTokenPublicKey()).build();
    }

    @Bean(name = "jwtRefreshTokenDecoder")
    public JwtDecoder jwtRefreshTokenDecoder() {
        return NimbusJwtDecoder.withPublicKey(keyUtil.getRefreshTokenPublicKey()).build();
    }

    // Call NimbusJwtEncoder with jwkSource
    @Bean
    @Primary
    public JwtEncoder jwtAccessTokenEncoder() {

        JWK jwk = new RSAKey.Builder(keyUtil.getAccessTokenPublicKey())
                .privateKey(keyUtil.getAccessTokenPrivateKey())
                .build();

        JWKSet jwkSet = new JWKSet(jwk);

        return new NimbusJwtEncoder((jwkSelector, context)
            -> jwkSelector.select(jwkSet)
        );
    }

    @Bean(name = "jwtRefreshTokenEncoder")
    public JwtEncoder jwtRefreshTokenEncoder() {

        JWK jwk = new RSAKey.Builder(keyUtil.getRefreshTokenPublicKey())
                .privateKey(keyUtil.getRefreshTokenPrivateKey())
                .build();

        JWKSet jwkSet = new JWKSet(jwk);

        return new NimbusJwtEncoder((jwkSelector, context)
                -> jwkSelector.select(jwkSet)
        );
    }

    // 1 Create Key pair
    // Using for generate public and private key
/*    @Bean
    public KeyPair keyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }*/

    // 2 Create RSA Key object using Key Pair
    // Using for generate public and private key
/*    @Bean
    public RSAKey rsaKey(KeyPair keyPair){
        return new RSAKey.Builder((RSAPublicKey)keyPair.getPublic())
                .privateKey(keyPair.getPrivate())
                .keyID(UUID.randomUUID().toString())
                .build();
    }*/


    // Create JWKSource (Json Web Key Source)
    // Store public key and private key for encoder generate token
/*    @Bean
    public JWKSource<SecurityContext> jwkSource(RSAKey rsaKey) {
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);

    }*/

}

/*private final PasswordEncoder encoder;

    //  Define in-memory user
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();

        UserDetails admin = User.builder()
                .username("admin")
                .password(encoder.encode("1234"))
                .roles("ADMIN")
                .build();

        UserDetails goldUser = User.builder()
                        .username("gold")
                                  .password(encoder.encode("1234"))
                                        .roles("ADMIN","ACCOUNT")
                                                .build();

        UserDetails user = User.builder()
                .username("user")
                .password(encoder.encode("1234"))
                .roles("USER")
                .build();

        userDetailsManager.createUser(admin);
        userDetailsManager.createUser(goldUser);
        userDetailsManager.createUser(user);
        return userDetailsManager;
    }*/