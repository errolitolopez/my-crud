package com.errolito.mycrud.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserReportDto {
    private Integer id;
    private String username;
    private String fullName;
    private String createdDate;
}