package com.errolito.mycrud.facade.impl;

import com.errolito.mycrud.dto.NotificationQuery;
import com.errolito.mycrud.dto.NotificationRequest;
import com.errolito.mycrud.dto.NotificationResponse;
import com.errolito.mycrud.entity.Notification;
import com.errolito.mycrud.facade.NotificationFacade;
import com.errolito.mycrud.mapper.NotificationMapper;
import com.errolito.mycrud.service.NotificationService;
import com.errolito.mycrud.shared.BaseCrudFacadeImpl;
import org.springframework.stereotype.Component;

@Component
public class NotificationFacadeImpl
        extends BaseCrudFacadeImpl<Integer, NotificationQuery, NotificationRequest, Notification, NotificationResponse>
        implements NotificationFacade {

    protected NotificationFacadeImpl(NotificationMapper mapper, NotificationService service) {
        super(mapper, service);
    }
}
