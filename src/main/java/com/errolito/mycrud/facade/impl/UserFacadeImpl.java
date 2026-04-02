package com.errolito.mycrud.facade.impl;

import com.errolito.mycrud.dto.UserQuery;
import com.errolito.mycrud.dto.UserRequest;
import com.errolito.mycrud.dto.UserResponse;
import com.errolito.mycrud.entity.User;
import com.errolito.mycrud.facade.UserFacade;
import com.errolito.mycrud.mapper.UserMapper;
import com.errolito.mycrud.service.UserService;
import com.errolito.mycrud.shared.BaseCrudFacadeImpl;
import io.github.uncaughterrol.commons.exception.ExceptionFactory;
import io.micrometer.tracing.annotation.NewSpan;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserFacadeImpl
        extends BaseCrudFacadeImpl<Integer, UserQuery, UserRequest, User, UserResponse>
        implements UserFacade {

    protected UserFacadeImpl(UserMapper mapper, UserService service) {
        super(mapper, service);
    }

    @Override
    @Transactional
    @NewSpan
    public UserResponse save(UserRequest userRequest) {
        log.info("Processing save request");

        validateUsername(userRequest.getUsername());

        User user = mapper.toEntity(userRequest);

        service.save(user);
        return mapper.toResponse(user);
    }

    @Override
    @Transactional
    @NewSpan
    public UserResponse update(Integer id, UserRequest userRequest) {
        log.info("Processing update request");

        User foundUser = service.getById(id, () -> ExceptionFactory.notFound("User not found"));

        String username = userRequest.getUsername();
        if (!foundUser.getUsername().equalsIgnoreCase(username)) {
            validateUsername(username);
        }
        mapper.fromRequest(userRequest, foundUser);

        service.save(foundUser);
        return mapper.toResponse(foundUser);
    }


    private void validateUsername(String username) {
        if (service.existsByQuery(UserQuery.builder().username(username).build())) {
            throw ExceptionFactory.alreadyExists("Username already exists");
        }
    }
}
