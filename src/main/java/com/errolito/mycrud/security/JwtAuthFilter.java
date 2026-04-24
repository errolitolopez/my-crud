package com.errolito.mycrud.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Value("${security.jwt.secret}")
    private String jwtSecret;

    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthFilter(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        log.debug("Processing authentication for request: {} {}", request.getMethod(), request.getServletPath());

        String authorization = request.getHeader("Authorization");
        final String token = JwtUtils.extractToken(authorization);

        if (shouldAuthenticateUser(token)) {
            Claims claims = JwtUtils.extractClaims(token, jwtSecret);
            String username = claims.get("username", String.class);

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private boolean shouldAuthenticateUser(String token) {
        return JwtUtils.isTokenValid(token, jwtSecret) && SecurityContextHolder.getContext().getAuthentication() == null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        boolean shouldSkip = Arrays.stream(UrlProperties.PUBLIC_URLS)
                .anyMatch(url -> path.startsWith(url.replace("/**", "")));

        if (shouldSkip) {
            log.debug("Skipping authentication for public URL: {}", path);
        }

        return shouldSkip;
    }
}