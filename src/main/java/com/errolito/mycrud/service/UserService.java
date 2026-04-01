package com.errolito.mycrud.service;

import com.errolito.mycrud.dto.UserQuery;
import com.errolito.mycrud.entity.User;
import com.errolito.mycrud.shared.BaseCrudService;

public interface UserService extends BaseCrudService<Integer, UserQuery, User> {
}
