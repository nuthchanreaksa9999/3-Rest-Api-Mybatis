package co.istad.mobilebanking.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final PasswordEncoder encoder;

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
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // Disable CSRF
        http.csrf(csrf -> csrf.disable());

        // Authorize url mapping
        http.authorizeHttpRequests(request -> {
                            request
                                    .requestMatchers("/api/v1/users").hasRole("ADMIN")
                                    .requestMatchers("/api/v1/account-types/**", "/api/v1/files/**").hasAnyRole("ACCOUNT", "USER")
                                    .anyRequest().permitAll()
                            ;
                        }
                )
                .sessionManagement(session -> session
                        // make security http STATELESS
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // Security mechanism
                .httpBasic(httpBasic -> {});

            return http.build();

    }

}
