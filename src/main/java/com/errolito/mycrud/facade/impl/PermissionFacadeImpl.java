package com.errolito.mycrud.facade.impl;

import com.errolito.mycrud.dto.PermissionQuery;
import com.errolito.mycrud.dto.PermissionRequest;
import com.errolito.mycrud.dto.PermissionResponse;
import com.errolito.mycrud.entity.Permission;
import com.errolito.mycrud.facade.PermissionFacade;
import com.errolito.mycrud.mapper.PermissionMapper;
import com.errolito.mycrud.service.PermissionService;
import com.errolito.mycrud.shared.BaseCrudFacadeImpl;
import io.github.uncaughterrol.commons.exception.ExceptionFactory;
import org.springframework.stereotype.Component;

@Component
public class PermissionFacadeImpl
        extends BaseCrudFacadeImpl<Integer, PermissionQuery, PermissionRequest, Permission, PermissionResponse>
        implements PermissionFacade {

    protected PermissionFacadeImpl(PermissionMapper mapper, PermissionService service) {
        super(mapper, service);
    }

    @Override
    public PermissionResponse save(PermissionRequest request) {
        validateUniqueness(request.getName().toLowerCase());

        return super.save(request);
    }

    @Override
    public PermissionResponse update(Integer id, PermissionRequest request) {
        log.info("Processing update request");

        Permission foundPermission = service.getById(id, () -> ExceptionFactory.notFound("Permission not found"));

        String name = request.getName();
        if (!foundPermission.getName().equalsIgnoreCase(name)) {
            validateUniqueness(name);
        }

        mapper.fromRequest(request, foundPermission);

        service.save(foundPermission);
        return mapper.toResponse(foundPermission);
    }

    private void validateUniqueness(String name) {
        if (service.existsByQuery(PermissionQuery.builder().name(name).build())) {
            throw ExceptionFactory.alreadyExists("Permission name already exists");
        }
    }
}