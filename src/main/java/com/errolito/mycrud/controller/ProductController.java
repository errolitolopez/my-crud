package com.errolito.mycrud.controller;

import com.errolito.mycrud.dto.ProductQuery;
import com.errolito.mycrud.dto.ProductRequest;
import com.errolito.mycrud.dto.ProductResponse;
import com.errolito.mycrud.facade.ProductFacade;
import com.errolito.mycrud.shared.BaseCrudController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1/products")
public class ProductController extends BaseCrudController<String, ProductQuery, ProductRequest, ProductResponse> {

    protected ProductController(ProductFacade facade) {
        super(facade);
    }
}