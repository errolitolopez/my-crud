package com.errolito.mycrud.controller;

import com.errolito.mycrud.dto.ReportDto;
import com.errolito.mycrud.dto.UserReportQuery;
import com.errolito.mycrud.service.UserReportService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@Validated
@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@Slf4j
public class ReportController {

    private final UserReportService userReportService;

    @GetMapping("/users")
    public ResponseEntity<byte[]> downloadUserReport(@Valid @ParameterObject @Parameter UserReportQuery reportQuery,
                                                     @ParameterObject @Parameter Pageable pageable) throws Exception {
        ReportDto report = userReportService.generate(reportQuery, pageable);

        return ResponseEntity.ok()
                .header(CONTENT_DISPOSITION, "attachment; filename=\"" + report.getFilename() + "\"")
                .contentType(MediaType.parseMediaType(report.getContentType()))
                .body(report.getData());
    }
}