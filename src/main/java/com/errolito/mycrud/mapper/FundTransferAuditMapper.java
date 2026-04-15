package com.errolito.mycrud.mapper;

import com.errolito.mycrud.dto.FundTransferAuditRequest;
import com.errolito.mycrud.dto.FundTransferAuditResponse;
import com.errolito.mycrud.entity.FundTransferAudit;
import com.errolito.mycrud.event.FundTransferEvent;
import com.errolito.mycrud.shared.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface FundTransferAuditMapper extends BaseMapper<FundTransferAuditRequest, FundTransferAudit, FundTransferAuditResponse> {
    FundTransferAudit toEntity(FundTransferEvent event);
}