package com.errolito.mycrud.service.impl;

import com.errolito.mycrud.dto.RoleQuery;
import com.errolito.mycrud.entity.Role;
import com.errolito.mycrud.repository.RoleRepository;
import com.errolito.mycrud.service.RoleService;
import com.errolito.mycrud.shared.BaseCrudServiceImpl;
import com.errolito.mycrud.shared.SpecBuilder;
import io.github.uncaughterrol.commons.exception.ExceptionFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
public class RoleServiceImpl
        extends BaseCrudServiceImpl<Integer, RoleQuery, Role, RoleRepository>
        implements RoleService {

    protected RoleServiceImpl(RoleRepository repository) {
        super(repository);
    }

    @Override
    protected Specification<Role> buildLikeSpec(RoleQuery query) {
        return (root, criteriaQuery, builder) ->
                SpecBuilder.of(root, builder)
                        .andLike("name", query.getName())
                        .build();
    }

    @Override
    protected Specification<Role> buildEqualSpec(RoleQuery query) {
        return (root, criteriaQuery, builder) ->
                SpecBuilder.of(root, builder)
                        .andEqual("name", query.getName())
                        .build();
    }

    @Override
    protected Supplier<RuntimeException> notFoundException() {
        return () -> ExceptionFactory.notFound("Role not found");
    }
}