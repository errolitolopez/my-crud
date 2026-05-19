package com.errolito.mycrud.facade;

import com.errolito.mycrud.dto.UserQuery;
import com.errolito.mycrud.dto.UserRequest;
import com.errolito.mycrud.dto.UserResponse;
import com.errolito.mycrud.shared.BaseCrudFacade;

import java.util.List;
import java.util.Optional;

public interface UserFacade extends BaseCrudFacade<Integer, UserQuery, UserRequest, UserResponse> {
    Optional<UserResponse> findByUsername(String username);

    UserResponse update(UserRequest request);

    List<UserResponse> findAll();
}
