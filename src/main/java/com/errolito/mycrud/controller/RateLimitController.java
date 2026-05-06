package com.errolito.mycrud.controller;

import com.errolito.mycrud.security.RateLimitService;
import com.errolito.mycrud.security.dto.RateLimit;
import com.errolito.mycrud.security.dto.RateLimitRequest;
import com.errolito.mycrud.shared.BaseController;
import io.github.uncaughterrol.commons.model.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/rate-limits")
@RequiredArgsConstructor
public class RateLimitController extends BaseController {

    private final RateLimitService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<RateLimit>>> getRateLimits() {
        return success(service.getRateLimits());
    }

    @PutMapping
    public ResponseEntity<ApiResponse<RateLimit>> update(@Valid @RequestBody RateLimitRequest request) {
        return success(service.update(request));
    }

    @PostMapping("/reset")
    public ResponseEntity<ApiResponse<Void>> reset() {
        service.reset();
        return success();
    }
}