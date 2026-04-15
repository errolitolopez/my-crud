package com.errolito.mycrud.service.impl;

import com.errolito.mycrud.dto.CustomerQuery;
import com.errolito.mycrud.entity.Customer;
import com.errolito.mycrud.repository.CustomerRepository;
import com.errolito.mycrud.service.CustomerService;
import com.errolito.mycrud.shared.BaseCrudServiceImpl;
import com.errolito.mycrud.shared.SpecBuilder;
import io.github.uncaughterrol.commons.exception.ExceptionFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
public class CustomerServiceImpl
        extends BaseCrudServiceImpl<Integer, CustomerQuery, Customer, CustomerRepository>
        implements CustomerService {

    protected CustomerServiceImpl(CustomerRepository repository) {
        super(repository);
    }

    @Override
    protected Specification<Customer> buildLikeSpec(CustomerQuery userQuery) {
        return (root, criteriaQuery, builder) ->
                SpecBuilder.of(root, builder)
                        .build();
    }

    @Override
    protected Specification<Customer> buildEqualSpec(CustomerQuery userQuery) {
        return (root, criteriaQuery, builder) ->
                SpecBuilder.of(root, builder)
                        .build();
    }

    @Override
    protected Supplier<RuntimeException> notFoundException() {
        return () -> ExceptionFactory.notFound("Customer not found");
    }
}
