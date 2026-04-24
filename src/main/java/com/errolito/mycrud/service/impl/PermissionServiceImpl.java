package com.errolito.mycrud.service.impl;

import com.errolito.mycrud.dto.PermissionQuery;
import com.errolito.mycrud.entity.Permission;
import com.errolito.mycrud.repository.PermissionRepository;
import com.errolito.mycrud.service.PermissionService;
import com.errolito.mycrud.shared.BaseCrudServiceImpl;
import com.errolito.mycrud.shared.SpecBuilder;
import io.github.uncaughterrol.commons.exception.ExceptionFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

@Service
public class PermissionServiceImpl
        extends BaseCrudServiceImpl<Integer, PermissionQuery, Permission, PermissionRepository>
        implements PermissionService {

    protected PermissionServiceImpl(PermissionRepository repository) {
        super(repository);
    }

    @Override
    protected Specification<Permission> buildLikeSpec(PermissionQuery query) {
        return (root, criteriaQuery, builder) ->
                SpecBuilder.of(root, builder)
                        .andLike("name", query.getName())
                        .build();
    }

    @Override
    protected Specification<Permission> buildEqualSpec(PermissionQuery query) {
        return (root, criteriaQuery, builder) ->
                SpecBuilder.of(root, builder)
                        .andEqual("name", query.getName())
                        .build();
    }

    @Override
    protected Supplier<RuntimeException> notFoundException() {
        return () -> ExceptionFactory.notFound("Permission not found");
    }

    @Override
    public Set<Permission> findAllByIds(Iterable<Integer> ids) {
        return new HashSet<>(repository.findAllById(ids));
    }
}