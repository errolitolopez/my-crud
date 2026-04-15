package com.errolito.mycrud.mapper;

import com.errolito.mycrud.dto.FundTransferRequest;
import com.errolito.mycrud.event.FundTransferEvent;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface FundTransferMapper {
    FundTransferEvent toEvent(FundTransferRequest request);
}
