package com.errolito.mycrud.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class CustomerQuery {
    private Long id;
    private String fullName;
}