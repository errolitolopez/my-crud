package com.errolito.mycrud.service.impl;

import com.errolito.mycrud.dto.ProductQuery;
import com.errolito.mycrud.entity.Product;
import com.errolito.mycrud.repository.ProductRepository;
import com.errolito.mycrud.service.ProductService;
import com.errolito.mycrud.shared.BaseMongoCrudServiceImpl;
import io.github.uncaughterrol.commons.exception.ExceptionFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
public class ProductServiceImpl
        extends BaseMongoCrudServiceImpl<String, ProductQuery, Product, ProductRepository>
        implements ProductService {

    protected ProductServiceImpl(ProductRepository repository) {
        super(repository);
    }

    @Override
    protected Supplier<RuntimeException> notFoundException() {
        return () -> ExceptionFactory.notFound("Product not found");
    }

    @Override
    protected Example<Product> buildExample(ProductQuery query) {
        Product product = new Product();
        product.setName(query.getName());

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
                .withIgnoreNullValues();

        return Example.of(product, matcher);
    }
}