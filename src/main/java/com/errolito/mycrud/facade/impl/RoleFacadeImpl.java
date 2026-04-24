package com.errolito.mycrud.facade.impl;

import com.errolito.mycrud.dto.RolePermissionsRequest;
import com.errolito.mycrud.dto.RoleQuery;
import com.errolito.mycrud.dto.RoleRequest;
import com.errolito.mycrud.dto.RoleResponse;
import com.errolito.mycrud.entity.Permission;
import com.errolito.mycrud.entity.Role;
import com.errolito.mycrud.facade.RoleFacade;
import com.errolito.mycrud.mapper.RoleMapper;
import com.errolito.mycrud.service.PermissionService;
import com.errolito.mycrud.service.RoleService;
import com.errolito.mycrud.shared.BaseCrudFacadeImpl;
import io.github.uncaughterrol.commons.exception.ExceptionFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
public class RoleFacadeImpl
        extends BaseCrudFacadeImpl<Integer, RoleQuery, RoleRequest, Role, RoleResponse>
        implements RoleFacade {

    private final PermissionService permissionService;

    protected RoleFacadeImpl(
            PermissionService permissionService,
            RoleMapper mapper,
            RoleService service
    ) {
        super(mapper, service);
        this.permissionService = permissionService;
    }

    @Override
    public RoleResponse save(RoleRequest request) {
        validateUniqueness(request.getName().toLowerCase());

        return super.save(request);
    }

    @Override
    public RoleResponse update(Integer id, RoleRequest request) {
        log.info("Processing update request");

        Role foundRole = service.getById(id, () -> ExceptionFactory.notFound("Role not found"));

        String name = request.getName();
        if (!foundRole.getName().equalsIgnoreCase(name)) {
            validateUniqueness(name);
        }

        mapper.fromRequest(request, foundRole);

        service.save(foundRole);
        return mapper.toResponse(foundRole);
    }

    private void validateUniqueness(String name) {
        if (service.existsByQuery(RoleQuery.builder().name(name).build())) {
            throw ExceptionFactory.alreadyExists("Role name already exists");
        }
    }

    @Override
    @Transactional
    public RoleResponse addRolePermissions(Integer id, RolePermissionsRequest request) {
        Role foundRole = service.getById(id, () -> ExceptionFactory.notFound("Role not found"));

        Set<Integer> permissionIds = request.getPermissionIds();
        Set<Permission> foundPermissions = permissionService.findAllByIds(permissionIds);

        if (!foundPermissions.isEmpty()) {
            foundRole.getPermissions().addAll(foundPermissions);
        }

        service.save(foundRole);
        return mapper.toResponse(foundRole);
    }

    @Override
    @Transactional
    public RoleResponse removeRolePermissions(Integer id, RolePermissionsRequest request) {
        Role foundRole = service.getById(id, () -> ExceptionFactory.notFound("Role not found"));

        Set<Integer> permissionIds = request.getPermissionIds();
        if (!permissionIds.isEmpty()) {
            foundRole.getPermissions().removeIf(permission -> permissionIds.contains(permission.getId()));
        }

        return mapper.toResponse(foundRole);
    }
}