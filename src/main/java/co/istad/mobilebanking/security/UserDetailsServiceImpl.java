package co.istad.mobilebanking.security;

import co.istad.mobilebanking.api.auth.AuthMapper;
import co.istad.mobilebanking.api.user.Authority;
import co.istad.mobilebanking.api.user.Role;
import co.istad.mobilebanking.api.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {



    private final AuthMapper authMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("Loading user: {}", username);

        User user = authMapper.loadUserByUsername(username).orElseThrow(()
            -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        log.info("Loading user: {}", user);

        for (Role role : user.getRoles()) {
            for (Authority authority : role.getAuthorities()) {
                System.out.println(authority.getName());
            }
        }

        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        customUserDetails.setUser(user);

        log.info("User: {}", customUserDetails);
        log.info("User: {}", customUserDetails.getAuthorities());

        return customUserDetails;
    }
}
