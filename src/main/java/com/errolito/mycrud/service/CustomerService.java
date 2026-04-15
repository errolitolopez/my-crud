package com.errolito.mycrud.service;

import com.errolito.mycrud.dto.CustomerQuery;
import com.errolito.mycrud.entity.Customer;
import com.errolito.mycrud.shared.BaseCrudService;

public interface CustomerService extends BaseCrudService<Integer, CustomerQuery, Customer> {
}
