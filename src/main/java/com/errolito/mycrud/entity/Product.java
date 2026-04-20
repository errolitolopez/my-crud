package com.errolito.mycrud.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@ToString
@Document("products")
public class Product {
    @Id
    private String id;
    private String name;
    private BigDecimal price;
    private Map<String, Object> attributes;
}