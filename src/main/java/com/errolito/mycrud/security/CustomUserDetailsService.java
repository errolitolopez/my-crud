package com.errolito.mycrud.security;

import com.errolito.mycrud.entity.Permission;
import com.errolito.mycrud.entity.Role;
import com.errolito.mycrud.entity.User;
import com.errolito.mycrud.repository.UserRepository;
import io.github.uncaughterrol.commons.exception.ExceptionFactory;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @NonNull
    @Override
    @Transactional
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> ExceptionFactory.notFound("User not found"));

        CustomUserDetails userDetails = new CustomUserDetails();
        userDetails.setId(user.getId());
        userDetails.setUsername(user.getUsername());

        if (user.getUserCredential() != null) {
            userDetails.setPassword(user.getUserCredential().getEncodedPassword());
        }

        Set<GrantedAuthority> authorities = Stream.concat(
                        user.getRoles().stream().map(role -> "ROLE_" + role),
                        user.getRoles().stream().map(Role::getPermissions)
                                .flatMap(Collection::stream).map(Permission::getName)
                )
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        userDetails.setAuthorities(authorities);

        return userDetails;
    }
}