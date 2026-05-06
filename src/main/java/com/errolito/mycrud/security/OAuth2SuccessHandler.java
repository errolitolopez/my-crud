package com.errolito.mycrud.security;

import com.errolito.mycrud.entity.User;
import com.errolito.mycrud.mapper.RoleMapper;
import com.errolito.mycrud.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${security.jwt.secret}")
    private String jwtSecret;

    @Value("${app.oauth2.redirect-uri:http://localhost:8080/oauth2/callback}")
    private String redirectUri;

    private final RoleMapper roleMapper;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(@NonNull HttpServletRequest request,
                                        @NonNull HttpServletResponse response,
                                        @NonNull Authentication authentication) throws IOException {

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String email = Optional.ofNullable(oAuth2User)
                .map(CustomOAuth2User::getEmail)
                .orElse(null);

        User user = userRepository.findByUsername(email).orElse(null);

        if (user == null) {
            log.error("User not found in OAuth2 principal attributes");
            response.sendRedirect("/auth/login?error");
            return;
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", user.getId());
        claims.put("username", user.getUsername());
        claims.put("roles", roleMapper.toRoleNames(user.getRoles()));
        claims.put("permissions", roleMapper.toPermissionNames(user.getRoles()));

        String accessToken = JwtUtils.generateToken(jwtSecret, claims, 86400L);

        Cookie cookie = new Cookie("accessToken", accessToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(86400);
        response.addCookie(cookie);

        String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("accessToken", accessToken)
                .build()
                .toUriString();

        log.debug("OAuth2 login success for: {}, redirecting to: {}", email, targetUrl);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}