package com.errolito.mycrud.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class PermissionRequest {
    @Pattern(regexp = "^(?!_)(?!.*__)[A-Za-z0-9]+(?:_[A-Za-z0-9]+)*$", message = "Invalid name")
    @NotBlank(message = "Name is required")
    @Schema(description = "Permissions's name", defaultValue = "test_permission_name")
    private String name;
}