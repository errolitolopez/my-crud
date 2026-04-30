package com.errolito.mycrud.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class FileQuery {
    private String slug;
    private String name;
}