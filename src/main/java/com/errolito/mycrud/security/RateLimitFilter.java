package com.errolito.mycrud.security;

import com.errolito.mycrud.security.dto.RateLimit;
import com.errolito.mycrud.security.dto.RateLimitConsumeRequest;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Order(1)
@Slf4j
public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimitService rateLimitService;
    private final ObjectMapper objectMapper;
    private final RateLimitProperties rateLimitProperties;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String method = request.getMethod();
        String instance = request.getServletPath();
        String ipAddress = getClientKey(request);

        RateLimitConsumeRequest rateLimitConsumeRequest = RateLimitConsumeRequest.builder()
                .method(method)
                .instance(instance)
                .ipAddress(ipAddress)
                .build();

        String key = rateLimitConsumeRequest.getKey();

        log.debug("Rate limit check - key: {}", key);

        ConsumptionProbe consumptionProbe = rateLimitService.tryConsumeAndReturnRemaining(rateLimitConsumeRequest);

        if (!consumptionProbe.isConsumed()) {
            log.warn("Rate limit exceeded - key: {}", key);

            String path = request.getServletPath();
            if (!path.startsWith("/api/v1/")) {
                long resetSeconds = TimeUnit.NANOSECONDS.toSeconds(consumptionProbe.getNanosToWaitForReset());

                String queryString = request.getQueryString();
                String encodedPath = URLEncoder.encode(path, StandardCharsets.UTF_8);
                String encodedQuery = queryString != null ? URLEncoder.encode(queryString, StandardCharsets.UTF_8) : "";

                response.sendRedirect("/error/too-many-requests?reset=" + resetSeconds + "&path=" + encodedPath + "&query=" + encodedQuery);
                return;
            }

            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            Map<String, Object> body = new LinkedHashMap<>();
            body.put("title", "Too Many Requests");
            body.put("status", 429);
            body.put("detail", "Rate limit exceeded. Please try again later.");

            objectMapper.writeValue(response.getWriter(), body);
            return;
        }

        log.debug("Rate limit passed - key: {}", key);
        filterChain.doFilter(request, response);
    }

    private String getClientKey(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        return (forwarded != null) ? forwarded.split(",")[0].trim() : request.getRemoteAddr();
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        Set<String> urls = rateLimitProperties.getRateLimits()
                .stream()
                .map(RateLimit::getUrl)
                .collect(Collectors.toSet());

        return !urls.contains(request.getServletPath());
    }
}