package com.errolito.mycrud.facade;

import com.errolito.mycrud.dto.PermissionQuery;
import com.errolito.mycrud.dto.PermissionRequest;
import com.errolito.mycrud.dto.PermissionResponse;
import com.errolito.mycrud.shared.BaseCrudFacade;

public interface PermissionFacade extends BaseCrudFacade<Integer, PermissionQuery, PermissionRequest, PermissionResponse> {
}