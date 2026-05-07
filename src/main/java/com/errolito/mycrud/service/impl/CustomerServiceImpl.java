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
    protected Specification<Customer> buildLikeSpec(CustomerQuery query) {
        return (root, criteriaQuery, builder) ->
                SpecBuilder.of(root, builder)
                        .andLike("fullName", query.getFullName())
                        .build();
    }

    @Override
    protected Specification<Customer> buildEqualSpec(CustomerQuery query) {
        return (root, criteriaQuery, builder) ->
                SpecBuilder.of(root, builder)
                        .andEqual("fullName", query.getFullName())
                        .build();
    }

    @Override
    protected Supplier<RuntimeException> notFoundException() {
        return () -> ExceptionFactory.notFound("Customer not found");
    }
}
