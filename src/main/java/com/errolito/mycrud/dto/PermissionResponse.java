package com.errolito.mycrud.dto;

import lombok.*;
import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class PermissionResponse  {
    private Integer id;
    private Instant createdDate;
    private String name;
}