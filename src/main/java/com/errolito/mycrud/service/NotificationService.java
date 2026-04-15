package com.errolito.mycrud.service;

import com.errolito.mycrud.dto.NotificationQuery;
import com.errolito.mycrud.entity.Notification;
import com.errolito.mycrud.shared.BaseCrudService;

import java.util.List;

public interface NotificationService extends BaseCrudService<Integer, NotificationQuery, Notification> {
    void saveAll(List<Notification> notifications);
}
