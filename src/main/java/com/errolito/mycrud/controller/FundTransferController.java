package com.errolito.mycrud.controller;

import com.errolito.mycrud.dto.FundTransferRequest;
import com.errolito.mycrud.dto.FundTransferResponse;
import com.errolito.mycrud.event.FundTransferEvent;
import com.errolito.mycrud.mapper.FundTransferMapper;
import com.errolito.mycrud.producer.FundTransferProducer;
import com.errolito.mycrud.shared.BaseController;
import io.github.uncaughterrol.commons.model.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.errolito.mycrud.enums.AuditStatus.SUCCESS;
import static com.errolito.mycrud.enums.AuditType.DEBIT;
import static com.errolito.mycrud.enums.EventType.DEBITED;
import static io.github.uncaughterrol.commons.utils.TokenGenerator.secureAlphanumericToken;

@Validated
@RestController
@RequestMapping("/api/v1/fund-transfers")
@RequiredArgsConstructor
public class FundTransferController extends BaseController {

    private final FundTransferProducer producer;
    private final FundTransferMapper mapper;

    @PostMapping
    public ResponseEntity<ApiResponse<FundTransferResponse>> fundTransfer(@Valid @RequestBody FundTransferRequest request) {
        String referenceId = secureAlphanumericToken(22);

        FundTransferEvent event = mapper.toEvent(request);
        event.setReferenceId(referenceId);
        event.setAuditType(DEBIT);
        event.setAuditStatus(SUCCESS);

        producer.send(DEBITED.topic(), event);
        return success(FundTransferResponse.builder().referenceId(referenceId).build());
    }
}