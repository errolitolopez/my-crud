package com.errolito.mycrud.mapper;

import com.errolito.mycrud.dto.AccountRequest;
import com.errolito.mycrud.dto.AccountResponse;
import com.errolito.mycrud.entity.Account;
import com.errolito.mycrud.shared.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface AccountMapper extends BaseMapper<AccountRequest, Account, AccountResponse> {
}