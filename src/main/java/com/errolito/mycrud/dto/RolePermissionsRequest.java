package com.errolito.mycrud.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
public class RolePermissionsRequest {
    private Set<Integer> permissionIds = new HashSet<>();
}