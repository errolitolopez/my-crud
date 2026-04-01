package com.errolito.mycrud.facade;

import com.errolito.mycrud.dto.UserQuery;
import com.errolito.mycrud.dto.UserRequest;
import com.errolito.mycrud.dto.UserResponse;
import com.errolito.mycrud.shared.BaseCrudFacade;

public interface UserFacade extends BaseCrudFacade<Integer, UserQuery, UserRequest, UserResponse> {
}
