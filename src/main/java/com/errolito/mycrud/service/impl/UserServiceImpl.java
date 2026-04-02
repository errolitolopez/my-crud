package com.errolito.mycrud.service.impl;

import com.errolito.mycrud.dto.UserQuery;
import com.errolito.mycrud.entity.User;
import com.errolito.mycrud.repository.UserRepository;
import com.errolito.mycrud.service.UserService;
import com.errolito.mycrud.shared.SpecBuilder;
import com.errolito.mycrud.shared.BaseCrudServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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
}
