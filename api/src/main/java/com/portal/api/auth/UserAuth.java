package com.portal.api.auth;

import com.portal.api.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Adapta a entidade {@link User} para o contrato {@link UserDetails} do Spring Security.
 */
public class UserAuth implements UserDetails {

    public static final String ROLE_USER = "USER";

    private final User user;

    public UserAuth(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public List<String> getRoles() {
        return List.of(ROLE_USER);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .toList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }
}
