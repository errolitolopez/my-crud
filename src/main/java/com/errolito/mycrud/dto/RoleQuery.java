package com.errolito.mycrud.dto;

import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class RoleQuery {
    @Pattern(regexp = "^(?!_)(?!.*__)[A-Za-z0-9]+(?:_[A-Za-z0-9]+)*$", message = "Invalid role")
    private String name;
}