package com.errolito.mycrud.facade.impl;

import com.errolito.mycrud.cache.CacheConfig;
import com.errolito.mycrud.cache.CacheStore;
import com.errolito.mycrud.dto.UserQuery;
import com.errolito.mycrud.dto.UserRequest;
import com.errolito.mycrud.dto.UserResponse;
import com.errolito.mycrud.entity.User;
import com.errolito.mycrud.facade.UserFacade;
import com.errolito.mycrud.mapper.UserMapper;
import com.errolito.mycrud.service.UserService;
import com.errolito.mycrud.shared.BaseCrudFacadeImpl;
import io.github.uncaughterrol.commons.exception.ExceptionFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class UserFacadeImpl
        extends BaseCrudFacadeImpl<Integer, UserQuery, UserRequest, User, UserResponse>
        implements UserFacade {

    private final UserService service;

    protected UserFacadeImpl(UserMapper mapper, UserService service, CacheStore<UserResponse> cacheStore) {
        super(mapper, service, cacheStore);
        this.service = service;
    }

    private final CacheConfig cacheConfig = new CacheConfig("user", true);

    @Override
    protected CacheConfig cacheConfig() {
        return cacheConfig;
    }

    @Override
    @Transactional
    public UserResponse save(UserRequest userRequest) {
        log.info("Processing save request");

        validateUsername(userRequest.getUsername());

        User user = mapper.toEntity(userRequest);

        service.save(user);
        clearCache();
        return mapper.toResponse(user);
    }

    @Override
    @Transactional
    public UserResponse update(Integer id, UserRequest userRequest) {
        log.info("Processing update request");

        User foundUser = service.getById(id, () -> ExceptionFactory.notFound("User not found"));

        String username = userRequest.getUsername();
        if (!foundUser.getUsername().equalsIgnoreCase(username)) {
            validateUsername(username);
        }
        mapper.fromRequest(userRequest, foundUser);

        service.save(foundUser);
        clearCache(id);
        return mapper.toResponse(foundUser);
    }


    private void validateUsername(String username) {
        if (service.existsByQuery(UserQuery.builder().username(username).build())) {
            throw ExceptionFactory.alreadyExists("Username already exists");
        }
    }

    @Override
    @Transactional
    public Optional<UserResponse> findByUsername(String username) {
        return service.findByUsername(username)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional
    public UserResponse update(UserRequest request) {
        return service.findByUsername(request.getUsername())
                .map(u -> update(u.getId(), request))
                .orElseThrow(() -> ExceptionFactory.notFound("User not found"));
    }

    @Override
    public List<UserResponse> findAll() {
        return service.findAll(new UserQuery(), Pageable.ofSize(1000)).getContent()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}
