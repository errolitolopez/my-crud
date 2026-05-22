package com.errolito.mycrud.service.impl;

import com.errolito.mycrud.dto.*;
import com.errolito.mycrud.facade.UserFacade;
import com.errolito.mycrud.mapper.UserMapper;
import com.errolito.mycrud.service.UserReportService;
import io.github.uncaughterrol.commons.exception.ExceptionFactory;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserReportServiceImpl implements UserReportService {

    private final UserFacade facade;
    private final UserMapper mapper;

    @Override
    public ReportDto generate(UserReportQuery query, Pageable pageable) throws Exception {
        InputStream template = getClass().getResourceAsStream("/reports/users.jasper");

        JasperReport report = (JasperReport) JRLoader.loadObject(template);

        String username = query.getUsername();
        int pageNumber = pageable.getPageNumber() + 1;

        Page<UserResponse> page = facade.findAll(UserQuery.builder().username(username).build(), pageable);

        List<UserReportDto> reports = page.getContent()
                .stream()
                .map(mapper::toReportDto)
                .toList();

        Map<String, Object> params = new HashMap<>();
        params.put("username", username != null ? username : "all");
        params.put("pageNumber", pageNumber);
        params.put("pageSize", pageable.getPageSize());
        params.put("totalElements", (int) page.getTotalElements());
        params.put("generatedBy", "system");

        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(reports);
        JasperPrint print = JasperFillManager.fillReport(report, params, ds);

        String format = query.getFormat().toLowerCase();

        byte[] data = switch (format) {
            case "pdf" -> JasperExportManager.exportReportToPdf(print);
            case "xlsx" -> toXlsx(print);
            default -> throw ExceptionFactory.internal("Unsupported format: " + format);
        };

        String filename = (username != null && !username.isBlank())
                ? "users_" + username + "_p" + pageNumber + "." + format
                : "users_all_p" + pageNumber + "." + format;

        String contentType = format.equals("xlsx")
                ? "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                : "application/pdf";

        return ReportDto.builder()
                .data(data)
                .filename(filename)
                .contentType(contentType)
                .build();
    }

    private byte[] toXlsx(JasperPrint print) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JRXlsxExporter ex = new JRXlsxExporter();
        ex.setExporterInput(new SimpleExporterInput(print));
        ex.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
        ex.exportReport();
        return out.toByteArray();
    }
}