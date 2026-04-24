package com.errolito.mycrud.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.*;


public final class JwtUtils {

    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);

    private static final long DEFAULT_EXPIRATION_SECONDS = 900L;

    private JwtUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static String generateSecretKey() {
        SecretKey key = Jwts.SIG.HS256.key().build();
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static String generateToken(String secret, Map<String, Object> claims, Long expirationInSeconds) {
        Instant now = Instant.now();

        JwtBuilder builder = Jwts.builder()
                .claims()
                .issuedAt(Date.from(now))
                .and()
                .signWith(buildSecretKey(secret), Jwts.SIG.HS256);

        if (claims != null && !claims.isEmpty()) builder.claims().add(claims);

        Date expiration = Date.from(now.plusSeconds(DEFAULT_EXPIRATION_SECONDS));
        if (expirationInSeconds != null) expiration = Date.from(now.plusSeconds(expirationInSeconds));
        builder.claims().expiration(expiration);

        return builder.compact();
    }

    public static String generateToken(String secret, Map<String, Object> claims) {
        return generateToken(secret, claims, null);
    }

    public static String generateToken(String secret, Long expirationInSeconds) {
        return generateToken(secret, null, expirationInSeconds);
    }

    public static String generateToken(String secret) {
        return generateToken(secret, null, null);
    }

    public static String extractToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }

        return authorizationHeader.substring(7);
    }

    public static boolean isTokenValid(String token, String secret) {
        if (token == null || token.isBlank()) {
            return false;
        }

        try {
            Jwts.parser().verifyWith(buildSecretKey(secret)).build().parseSignedClaims(token);

            return true;
        } catch (JwtException ex) {
            log.debug("JWT validation failed: {}", ex.getMessage());
            return false;
        }
    }

    public static Claims extractClaims(String token, String secret) {
        return Jwts.parser().verifyWith(buildSecretKey(secret)).build().parseSignedClaims(token).getPayload();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> T extract(Claims claims, String key, Class<T> clazz) {
        try {
            Object raw = claims.get(key);
            if (raw == null) {
                log.warn("'{}' claim is missing in JWT", key);
                return null;
            }

            if (clazz.isInstance(raw)) {
                return clazz.cast(raw);
            }

            return switch (raw) {
                case Collection collection when List.class.isAssignableFrom(clazz) -> (T) new ArrayList<>(collection);
                case List list when Set.class.isAssignableFrom(clazz) -> (T) new LinkedHashSet<>(list);
                case String s when clazz.isEnum() -> (T) Enum.valueOf((Class<Enum>) clazz, s);
                default -> claims.get(key, clazz);
            };

        } catch (Exception ex) {
            log.error("Failed to extract '{}' from JWT claims", key, ex);
            return null;
        }
    }

    private static SecretKey buildSecretKey(String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}