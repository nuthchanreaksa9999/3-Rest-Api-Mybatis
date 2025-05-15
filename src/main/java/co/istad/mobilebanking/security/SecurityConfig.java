package co.istad.mobilebanking.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {

        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService);
        auth.setPasswordEncoder(passwordEncoder);

        return auth;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // Disable CSRF
        http.csrf(csrf -> csrf.disable());

        // Authorize url mapping
        http.authorizeHttpRequests(auth -> {
                    auth
                                    .requestMatchers("/api/v1/auth/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/v1/users/**").hasAnyRole("ADMIN","SYSTEM")
                            .requestMatchers(HttpMethod.POST, "/api/v1/users/**").hasRole("SYSTEM")
                            .anyRequest().authenticated()
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