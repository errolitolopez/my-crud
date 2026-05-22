package com.errolito.mycrud.dto;

import com.errolito.mycrud.enums.ReportFormat;
import com.errolito.mycrud.validator.ValidEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseReportQuery {
    @Schema(defaultValue = "PDF")
    @NotBlank(message = "Format is required")
    @ValidEnum(value = ReportFormat.class, message = "Invalid format. Must be PDF or XLSX.")
    private String format;
}