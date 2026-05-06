package com.errolito.mycrud.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public final class OAuth2Utils {
    public OAuth2Utils() {
    }

    public static String extractProviderId(Map<String, Object> attrs, String registrationId) {
        return switch (registrationId) {
            case "google" -> String.valueOf(attrs.get("sub"));
            case "facebook", "github" -> String.valueOf(attrs.get("id"));
            default -> throw new IllegalStateException("Unknown provider: " + registrationId);
        };
    }

    public static String extractName(Map<String, Object> atrributes) {
        if (atrributes == null || atrributes.isEmpty()) {
            log.debug("Failed to extract name atrributes is null or empty");
            return null;
        }

        String name = (String) atrributes.get("name");
        if (name == null || name.isBlank()) {
            name = (String) atrributes.get("login");
        }
        return name;
    }

    public static String extractEmail(Map<String, Object> atrributes) {
        if (atrributes == null || atrributes.isEmpty()) {
            log.debug("Failed to extract email atrributes is null or empty");
            return null;
        }

        String email = (String) atrributes.get("email");

        if (email == null || email.isBlank()) {
            email = atrributes.get("login") + "@github.local";
        }
        return email;
    }
}
