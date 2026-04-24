package com.errolito.mycrud.facade;

import com.errolito.mycrud.dto.RolePermissionsRequest;
import com.errolito.mycrud.dto.RoleQuery;
import com.errolito.mycrud.dto.RoleRequest;
import com.errolito.mycrud.dto.RoleResponse;
import com.errolito.mycrud.shared.BaseCrudFacade;

public interface RoleFacade extends BaseCrudFacade<Integer, RoleQuery, RoleRequest, RoleResponse> {
    RoleResponse addRolePermissions(Integer id, RolePermissionsRequest request);

    RoleResponse removeRolePermissions(Integer id, RolePermissionsRequest request);
}