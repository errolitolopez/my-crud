package com.errolito.mycrud.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class RoleResponse {
    private Integer id;
    private Instant createdDate;
    private String name;

    private Set<RolePermissionResponse> permissions = new HashSet<>();
}