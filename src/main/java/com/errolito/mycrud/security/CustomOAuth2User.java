package com.errolito.mycrud.security;

import com.errolito.mycrud.entity.User;
import com.errolito.mycrud.entity.UserAuthProvider;
import com.errolito.mycrud.enums.AuthProvider;
import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2User implements OAuth2User {

    private final OAuth2User delegate;
    private final User user;
    private final AuthProvider provider;

    public CustomOAuth2User(OAuth2User delegate, User user, AuthProvider provider
    ) {
        this.delegate = delegate;
        this.user = user;
        this.provider = provider;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return delegate.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return delegate.getAuthorities();
    }

    @Override
    @NonNull
    public String getName() {
        return user.getUsername();
    }

    public String getEmail() {
        return user.getUserAuthProviders().stream()
                .filter(userAuthProvider -> provider.equals(userAuthProvider.getProvider()))
                .map(UserAuthProvider::getEmail)
                .findFirst().orElse(null);
    }

    public String getFullName() {
        return user.getUserProfile().getFullName();
    }
}