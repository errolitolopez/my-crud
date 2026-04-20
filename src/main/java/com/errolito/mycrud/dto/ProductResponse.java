package com.errolito.mycrud.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@ToString
public class ProductResponse {
    private String id;
    private String name;
    private BigDecimal price;
    private Map<String, Object> attributes;
}