package com.errolito.mycrud.mapper;

import com.errolito.mycrud.dto.PermissionRequest;
import com.errolito.mycrud.dto.PermissionResponse;
import com.errolito.mycrud.entity.Permission;
import com.errolito.mycrud.shared.BaseMapper;
import org.mapstruct.Mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface PermissionMapper extends BaseMapper<PermissionRequest, Permission, PermissionResponse> {
}