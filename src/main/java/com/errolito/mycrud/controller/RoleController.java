package com.errolito.mycrud.controller;

import com.errolito.mycrud.dto.RolePermissionsRequest;
import com.errolito.mycrud.dto.RoleQuery;
import com.errolito.mycrud.dto.RoleRequest;
import com.errolito.mycrud.dto.RoleResponse;
import com.errolito.mycrud.facade.RoleFacade;
import com.errolito.mycrud.shared.BaseCrudController;
import io.github.uncaughterrol.commons.model.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/v1/roles")
public class RoleController extends BaseCrudController<Integer, RoleQuery, RoleRequest, RoleResponse> {

    private final RoleFacade facade;

    protected RoleController(RoleFacade facade) {
        super(facade);
        this.facade = facade;
    }

    @PostMapping("/{id}/permissions")
    public ResponseEntity<ApiResponse<RoleResponse>> addRolePermissions(@PathVariable Integer id,
                                                                        @RequestBody RolePermissionsRequest request) {
        RoleResponse response = facade.addRolePermissions(id, request);
        return success(response);
    }

    @DeleteMapping("/{id}/permissions")
    public ResponseEntity<ApiResponse<RoleResponse>> removeRolePermissions(@PathVariable Integer id,
                                                                           @RequestBody RolePermissionsRequest request) {
        RoleResponse response = facade.removeRolePermissions(id, request);
        return success(response);
    }
}