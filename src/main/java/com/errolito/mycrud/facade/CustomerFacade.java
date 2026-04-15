package com.errolito.mycrud.facade;

import com.errolito.mycrud.dto.*;
import com.errolito.mycrud.shared.BaseCrudFacade;

public interface CustomerFacade
        extends BaseCrudFacade<Integer, CustomerQuery, CustomerRequest, CustomerResponse> {
    CustomerResponse openAccount(AccountOpenRequest request);
    CustomerResponse addAccount(AccountAddRequest request);
}