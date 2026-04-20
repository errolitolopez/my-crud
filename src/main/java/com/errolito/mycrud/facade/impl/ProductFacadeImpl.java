package com.errolito.mycrud.facade.impl;

import com.errolito.mycrud.dto.ProductQuery;
import com.errolito.mycrud.dto.ProductRequest;
import com.errolito.mycrud.dto.ProductResponse;
import com.errolito.mycrud.entity.Product;
import com.errolito.mycrud.facade.ProductFacade;
import com.errolito.mycrud.mapper.ProductMapper;
import com.errolito.mycrud.service.ProductService;
import com.errolito.mycrud.shared.BaseCrudFacadeImpl;
import org.springframework.stereotype.Component;

@Component
public class ProductFacadeImpl
        extends BaseCrudFacadeImpl<String, ProductQuery, ProductRequest, Product, ProductResponse>
        implements ProductFacade {

    protected ProductFacadeImpl(ProductMapper mapper, ProductService service) {
        super(mapper, service);
    }
}
