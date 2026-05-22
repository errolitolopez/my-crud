package com.errolito.mycrud.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportDto {
    private byte[] data;
    private String filename;
    private String contentType;
}