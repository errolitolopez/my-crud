package com.errolito.mycrud.repository;

import com.errolito.mycrud.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}