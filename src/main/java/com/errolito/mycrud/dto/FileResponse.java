package com.errolito.mycrud.dto;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class FileResponse {
    private Integer id;
    private String slug;
    private String name;
    private String url;
    private Instant createdDate;
}