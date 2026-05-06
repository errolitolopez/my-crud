package com.errolito.mycrud.mapper;

import com.errolito.mycrud.dto.RoleRequest;
import com.errolito.mycrud.dto.RoleResponse;
import com.errolito.mycrud.entity.Permission;
import com.errolito.mycrud.entity.Role;
import com.errolito.mycrud.shared.BaseMapper;
import org.mapstruct.Mapper;

import java.util.*;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface RoleMapper extends BaseMapper<RoleRequest, Role, RoleResponse> {

    default Collection<String> toRoleNames(Set<Role> roles) {
        if (roles == null) return new HashSet<>();

        return roles.stream()
                .map(Role::getName)
                .toList();
    }

    default Collection<String> toPermissionNames(Set<Role> roles) {
        if (roles == null) return new ArrayList<>();

        return roles.stream().map(Role::getPermissions)
                .flatMap(Collection::stream).map(Permission::getName)
                .toList();
    }
}