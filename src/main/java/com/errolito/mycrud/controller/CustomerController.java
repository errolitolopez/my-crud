package com.errolito.mycrud.controller;

import com.errolito.mycrud.dto.*;
import com.errolito.mycrud.facade.CustomerFacade;
import com.errolito.mycrud.shared.BaseCrudController;
import io.github.uncaughterrol.commons.model.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController extends BaseCrudController<Integer, CustomerQuery, CustomerRequest, CustomerResponse> {

    private final CustomerFacade facade;

    protected CustomerController(CustomerFacade facade) {
        super(facade);
        this.facade = facade;
    }

    @PostMapping("/open/account")
    public ResponseEntity<ApiResponse<CustomerResponse>> openAccount(@Valid @RequestBody AccountOpenRequest request) {
        return success(facade.openAccount(request));
    }

    @PostMapping("/add/account")
    public ResponseEntity<ApiResponse<CustomerResponse>> openAccount(@Valid @RequestBody AccountAddRequest request) {
        return success(facade.addAccount(request));
    }
}