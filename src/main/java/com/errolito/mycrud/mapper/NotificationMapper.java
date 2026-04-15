package com.errolito.mycrud.mapper;

import com.errolito.mycrud.dto.NotificationRequest;
import com.errolito.mycrud.dto.NotificationResponse;
import com.errolito.mycrud.entity.Notification;
import com.errolito.mycrud.shared.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface NotificationMapper extends BaseMapper<NotificationRequest, Notification, NotificationResponse> {
}