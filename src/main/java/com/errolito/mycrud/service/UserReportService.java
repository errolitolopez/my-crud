package com.errolito.mycrud.service;

import com.errolito.mycrud.dto.ReportDto;
import com.errolito.mycrud.dto.UserQuery;
import com.errolito.mycrud.dto.UserReportQuery;
import org.springframework.data.domain.Pageable;

public interface UserReportService {
    ReportDto generate(UserReportQuery query, Pageable pageable) throws Exception;
}