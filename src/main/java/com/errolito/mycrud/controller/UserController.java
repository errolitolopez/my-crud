package com.errolito.mycrud.controller;

import com.errolito.mycrud.dto.UserQuery;
import com.errolito.mycrud.dto.UserRequest;
import com.errolito.mycrud.dto.UserResponse;
import com.errolito.mycrud.facade.UserFacade;
import com.errolito.mycrud.shared.BaseCrudController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1/users")
public class UserController extends BaseCrudController<Integer, UserQuery, UserRequest, UserResponse> {

    protected UserController(UserFacade facade) {
        super(facade);
    }
}