package com.errolito.mycrud.mapper;

import com.errolito.mycrud.dto.RoleRequest;
import com.errolito.mycrud.dto.RoleResponse;
import com.errolito.mycrud.entity.Role;
import com.errolito.mycrud.shared.BaseMapper;
import org.mapstruct.Mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface RoleMapper extends BaseMapper<RoleRequest, Role, RoleResponse> {
}