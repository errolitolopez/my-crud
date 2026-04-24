package com.errolito.mycrud.security;

import org.springframework.http.HttpMethod;

import java.util.Map;
import java.util.Set;

public final class UrlProperties {
    public static final String[] PUBLIC_URLS = new String[]{
            "/actuator/**",
            "/api/v1/auth/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
    };

    public static final Map<String, Set<HttpMethod>> PUBLIC_METHODS = Map.ofEntries(
            Map.entry("/api/v1/users/**", Set.of(HttpMethod.GET))
    );
}