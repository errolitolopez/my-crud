package com.errolito.mycrud.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import static java.lang.Boolean.TRUE;

@Service
@Slf4j
public class GitHubService {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String fetchPrimaryEmail(String accessToken) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.github.com/user/emails"))
                .header("Authorization", "Bearer " + accessToken)
                .header("Accept", "application/vnd.github+json")
                .header("X-GitHub-Api-Version", "2022-11-28")
                .GET()
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                log.error("GitHub /user/emails returned status: {}", response.statusCode());
                return null;
            }

            List<Map<String, Object>> emails = objectMapper.readValue(
                response.body(),
                new TypeReference<>() {}
            );

            return emails.stream()
                .filter(e -> TRUE.equals(e.get("primary")) && TRUE.equals(e.get("verified")))
                .map(e -> (String) e.get("email"))
                .findFirst()
                .orElseGet(() -> emails.stream()
                    .filter(e -> TRUE.equals(e.get("verified")))
                    .map(e -> (String) e.get("email"))
                    .findFirst()
                    .orElse(null));

        } catch (Exception e) {
            log.error("Failed to fetch GitHub primary email", e);
            return null;
        }
    }
}