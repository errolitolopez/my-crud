package com.errolito.mycrud.mapper;

import com.errolito.mycrud.dto.UserRequest;
import com.errolito.mycrud.dto.UserResponse;
import com.errolito.mycrud.entity.User;
import com.errolito.mycrud.shared.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface UserMapper extends BaseMapper<UserRequest, User, UserResponse> {

    @Override
    @Mapping(target = "userProfile.fullName", source = "fullName")
    User toEntity(UserRequest request);

    @Override
    @Mapping(target = "userProfile.fullName", source = "fullName")
    void fromRequest(UserRequest source,  @MappingTarget User target);
}