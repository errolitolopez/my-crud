package com.errolito.mycrud.facade;

import com.errolito.mycrud.dto.ProductQuery;
import com.errolito.mycrud.dto.ProductRequest;
import com.errolito.mycrud.dto.ProductResponse;
import com.errolito.mycrud.shared.BaseCrudFacade;

public interface ProductFacade extends BaseCrudFacade<String, ProductQuery, ProductRequest, ProductResponse> {
}