package com.errolito.mycrud.controller;

import com.errolito.mycrud.dto.PermissionQuery;
import com.errolito.mycrud.dto.PermissionRequest;
import com.errolito.mycrud.dto.PermissionResponse;
import com.errolito.mycrud.facade.PermissionFacade;
import com.errolito.mycrud.shared.BaseCrudController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1/permissions")
public class PermissionController extends BaseCrudController<Integer, PermissionQuery, PermissionRequest, PermissionResponse> {

    protected PermissionController(PermissionFacade facade) {
        super(facade);
    }
}