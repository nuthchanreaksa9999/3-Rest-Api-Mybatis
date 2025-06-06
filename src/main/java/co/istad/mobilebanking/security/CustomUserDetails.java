package co.istad.mobilebanking.security;

import co.istad.mobilebanking.api.user.Authority;
import co.istad.mobilebanking.api.user.Role;
import co.istad.mobilebanking.api.user.User;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomUserDetails implements UserDetails {

    private User user;

    // roles and permission
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for(Role role : user.getRoles()){
            authorities.add(new SimpleGrantedAuthority(role.getAuthority()));

            for(Authority authority : role.getAuthorities()){
                authorities.add(new SimpleGrantedAuthority(authority.getName()));
            }
        }

        return authorities;
    }

    // get information from user
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
