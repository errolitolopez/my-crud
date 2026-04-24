package com.errolito.mycrud.service;

import com.errolito.mycrud.dto.PermissionQuery;
import com.errolito.mycrud.entity.Permission;
import com.errolito.mycrud.shared.BaseCrudService;

import java.util.Set;

public interface PermissionService extends BaseCrudService<Integer, PermissionQuery, Permission> {
    Set<Permission> findAllByIds(Iterable<Integer> ids);
}