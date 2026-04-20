package com.errolito.mycrud.mapper;

import com.errolito.mycrud.dto.ProductRequest;
import com.errolito.mycrud.dto.ProductResponse;
import com.errolito.mycrud.entity.Product;
import com.errolito.mycrud.shared.BaseMapper;
import org.mapstruct.Mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface ProductMapper extends BaseMapper<ProductRequest, Product, ProductResponse> {
}