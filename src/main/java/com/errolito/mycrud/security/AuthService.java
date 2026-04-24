package com.errolito.mycrud.security;

import com.errolito.mycrud.entity.Permission;
import com.errolito.mycrud.entity.Role;
import com.errolito.mycrud.entity.User;
import com.errolito.mycrud.repository.UserRepository;
import io.github.uncaughterrol.commons.exception.ExceptionFactory;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

import static io.github.uncaughterrol.commons.exception.ExceptionFactory.forbidden;

@Service
@RequiredArgsConstructor
public class AuthService {
    @Value("${security.jwt.secret}")
    private String jwtSecret;

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> ExceptionFactory.unauthorized("Invalid username or password"));

        authenticateCredentials(request);

        return buildAuthResponse(user);
    }

    private AuthResponse buildAuthResponse(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", user.getId());
        claims.put("username", user.getUsername());
        claims.put("roles", toRoleNames(user.getRoles()));
        claims.put("permissions", toPermissionNames(user.getRoles()));

        String accessToken = JwtUtils.generateToken(jwtSecret, claims, 86400L);
        String refreshToken = JwtUtils.generateToken(jwtSecret, claims, 86400L * 7);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken(accessToken);
        authResponse.setRefreshToken(refreshToken);
        authResponse.setRoles(toRoleNames(user.getRoles()));
        authResponse.setPermissions(toPermissionNames(user.getRoles()));

        return authResponse;
    }

    private void authenticateCredentials(AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);

            SecurityContextHolder.setContext(context);
        } catch (BadCredentialsException e) {
            throw ExceptionFactory.unauthorized("Invalid username or password");
        }
    }

    public AuthResponse refreshToken(AuthRefreshTokenRequest authRefreshTokenRequest) {
        String refreshToken = authRefreshTokenRequest.getRefreshToken();

        if (!JwtUtils.isTokenValid(refreshToken, jwtSecret)) throw forbidden("Invalid or expired refresh token");

        Claims claims = JwtUtils.extractClaims(refreshToken, jwtSecret);

        String username = claims.get("username", String.class);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> ExceptionFactory.forbidden("Invalid or expired refresh token"));

        return buildAuthResponse(user);
    }

    private List<String> toRoleNames(Set<Role> roles) {
        if (roles == null) return new ArrayList<>();

        return roles.stream()
                .map(Role::getName)
                .toList();
    }

    private List<String> toPermissionNames(Set<Role> roles) {
        if (roles == null) return new ArrayList<>();

        return roles.stream().map(Role::getPermissions)
                .flatMap(Collection::stream).map(Permission::getName)
                .toList();
    }
}