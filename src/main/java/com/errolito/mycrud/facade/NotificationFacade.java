package com.errolito.mycrud.facade;

import com.errolito.mycrud.dto.NotificationQuery;
import com.errolito.mycrud.dto.NotificationRequest;
import com.errolito.mycrud.dto.NotificationResponse;
import com.errolito.mycrud.shared.BaseCrudFacade;

public interface NotificationFacade extends BaseCrudFacade<Integer, NotificationQuery, NotificationRequest, NotificationResponse> {
}
