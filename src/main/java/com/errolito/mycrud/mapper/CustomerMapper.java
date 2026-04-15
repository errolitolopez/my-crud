package com.errolito.mycrud.mapper;

import com.errolito.mycrud.dto.CustomerRequest;
import com.errolito.mycrud.dto.CustomerResponse;
import com.errolito.mycrud.entity.Customer;
import com.errolito.mycrud.shared.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CustomerMapper extends BaseMapper<CustomerRequest, Customer, CustomerResponse> {
}