package com.errolito.mycrud.service;

import com.errolito.mycrud.dto.ProductQuery;
import com.errolito.mycrud.entity.Product;
import com.errolito.mycrud.shared.BaseCrudService;

public interface ProductService extends BaseCrudService<String, ProductQuery, Product> {
}