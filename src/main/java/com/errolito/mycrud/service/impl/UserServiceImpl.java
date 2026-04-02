package com.errolito.mycrud.service.impl;

import com.errolito.mycrud.dto.UserQuery;
import com.errolito.mycrud.entity.User;
import com.errolito.mycrud.repository.UserRepository;
import com.errolito.mycrud.service.UserService;
import com.errolito.mycrud.shared.BaseCrudServiceImpl;
import com.errolito.mycrud.shared.SpecBuilder;
import io.github.uncaughterrol.commons.exception.ExceptionFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
public class UserServiceImpl
        extends BaseCrudServiceImpl<Integer, UserQuery, User, UserRepository>
        implements UserService {

    protected UserServiceImpl(UserRepository repository) {
        super(repository);
    }

    @Override
    protected Specification<User> buildLikeSpec(UserQuery userQuery) {
        return (root, criteriaQuery, builder) ->
                SpecBuilder.of(root, builder)
                        .andLike("username", userQuery.getUsername())
                        .build();
    }

    @Override
    protected Specification<User> buildEqualSpec(UserQuery userQuery) {
        return (root, criteriaQuery, builder) ->
                SpecBuilder.of(root, builder)
                        .andEqual("username", userQuery.getUsername())
                        .build();
    }

    @Override
    protected Supplier<RuntimeException> notFoundException() {
        return () -> ExceptionFactory.notFound("User not found");
    }
}
