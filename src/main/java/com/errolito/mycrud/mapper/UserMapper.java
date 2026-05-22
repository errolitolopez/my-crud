package com.errolito.mycrud.mapper;

import com.errolito.mycrud.dto.UserReportDto;
import com.errolito.mycrud.dto.UserRequest;
import com.errolito.mycrud.dto.UserResponse;
import com.errolito.mycrud.entity.User;
import com.errolito.mycrud.shared.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface UserMapper extends BaseMapper<UserRequest, User, UserResponse> {

    @Override
    @Mapping(target = "userProfile.fullName", source = "fullName")
    User toEntity(UserRequest request);

    @Override
    @Mapping(target = "userProfile.fullName", source = "fullName")
    void fromRequest(UserRequest source, @MappingTarget User target);

    @Mapping(target = "fullName", source = "userProfile.fullName")
    @Mapping(target = "createdDate", source = "createdDate", qualifiedByName = "formatInstant")
    UserReportDto toReportDto(UserResponse response);

    @Named("formatInstant")
    default String formatInstant(Instant instant) {
        if (instant == null) return "";
        return DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm")
                .withZone(ZoneId.systemDefault())
                .format(instant);
    }
}