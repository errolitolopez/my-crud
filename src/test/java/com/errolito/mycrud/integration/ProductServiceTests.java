package com.errolito.mycrud.integration;

import com.errolito.mycrud.dto.ProductQuery;
import com.errolito.mycrud.entity.Product;
import com.errolito.mycrud.repository.ProductRepository;
import com.errolito.mycrud.service.impl.ProductServiceImpl;
import com.github.javafaker.Commerce;
import com.github.javafaker.Faker;
import io.github.uncaughterrol.commons.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceTests {

    private final Faker faker;
    private final ProductRepository repository;
    private final ProductServiceImpl service;

    private String name;
    private BigDecimal price;
    private String color;
    private int quantity;

    private ProductQuery query;
    private Product entity;

    @Autowired
    ProductServiceTests(
            Faker faker,
            ProductRepository repository,
            ProductServiceImpl service
    ) {
        this.faker = faker;
        this.repository = repository;
        this.service = service;
    }

    @BeforeEach
    void setUp() {
        Commerce commerce = faker.commerce();
        color = commerce.color();
        name = commerce.productName();
        price = new BigDecimal(commerce.price(0.01, 999.99));
        quantity = faker.number().numberBetween(1, 999);

        query = new ProductQuery();
        query.setName(name);

        entity = new Product();
        entity.setName(name);
        entity.setPrice(price);

        Map<String, Object> attributes = Map.ofEntries(
                Map.entry("color", color),
                Map.entry("quantity", quantity)
        );

        entity.setAttributes(attributes);
    }

    @Nested
    class Save {

        @Test
        @DisplayName("valid entity returns created entity")
        void save_success() {
            Product result = service.save(entity);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isNotNull();
            assertThat(result.getName()).isEqualTo(name);
            assertThat(result.getPrice()).isEqualTo(price);

            assertThat(result.getAttributes()).isNotNull();
            assertThat(result.getAttributes().get("color")).isEqualTo(color);
            assertThat(result.getAttributes().get("quantity")).isEqualTo(quantity);
        }
    }

    @Nested
    class FindAll {
        private static final Pageable PAGEABLE = PageRequest.of(0, 10);

        @Test
        @DisplayName("returns paged products")
        void findAll_returnsPagedProducts() {
            service.save(entity); // ensure DB has at least one record

            query.setName(null); // remove filters

            Page<Product> result = service.findAll(query, PAGEABLE);
            assertThat(result.getTotalElements()).isNotZero();
        }

        @Test
        @DisplayName("returns empty page when query matches nothing")
        void findAll_returnsEmptyPage_whenQueryIsInvalid() {
            query.setName("this-product-name-does-not-exist");

            Page<Product> result = service.findAll(query, PAGEABLE);
            assertThat(result.getTotalElements()).isZero();
        }

        @Test
        @DisplayName("returns filtered page matching name")
        void findAll_returnsFilteredPage() {
            Product saved = service.save(entity);
            assertThat(saved).isNotNull();

            query.setName(saved.getName());

            Page<Product> result = service.findAll(query, PAGEABLE);
            assertThat(result.getTotalElements()).isOne();
            assertThat(result.getContent().getFirst().getName())
                    .isEqualTo(query.getName());
        }
    }

    @Nested
    class FindById {
        @Test
        @DisplayName("existing id returns product")
        void findById_found_returnsProduct() {
            Product saved = service.save(entity);
            assertThat(saved).isNotNull();

            Optional<Product> result = service.findById(saved.getId());
            assertThat(result).isPresent();
            assertThat(result.get().getName()).isEqualTo(name);
            assertThat(result.get().getPrice()).isEqualTo(price);
        }

        @Test
        @DisplayName("non-existent id returns empty")
        void findById_notFound_returnsEmpty() {
            Optional<Product> result = service.findById("this-id-does-not-exists");
            assertThat(result).isEmpty();
        }
    }

    @Nested
    class GetById {
        @Test
        @DisplayName("existing id returns product")
        void getById_found_returnsProduct() {
            Product saved = service.save(entity);
            assertThat(saved).isNotNull();

            Product result = service.getById(saved.getId());
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(saved.getId());
            assertThat(result.getName()).isEqualTo(name);
            assertThat(result.getPrice()).isEqualTo(price);
        }

        @Test
        @DisplayName("non-existent id throws ResourceNotFoundException")
        void getById_notFound_throwsResourceNotFoundException() {
            assertThatThrownBy(() -> service.getById("this-id-does-not-exists"))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Product not found");
        }
    }

    @Nested
    class ExistsById {
        @Test
        @DisplayName("existing id returns true")
        void existsById_returnsTrue() {
            Product saved = service.save(entity);

            boolean result = service.existsById(saved.getId());
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("non-existent id returns false")
        void existsById_returnsFalse() {
            boolean result = service.existsById("this-id-does-not-exists");
            assertThat(result).isFalse();
        }
    }

    @Nested
    class ExistsByQuery {
        @Test
        @DisplayName("matching query returns true")
        void existsByQuery_returnsTrue() {
            Product saved = service.save(entity);

            query.setName(saved.getName());
            boolean result = service.existsByQuery(query);
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("non-matching query returns false")
        void existsByQuery_returnsFalse() {
            query.setName("this-product-name-does-not-exist");

            boolean result = service.existsByQuery(query);
            assertThat(result).isFalse();
        }
    }

    @Nested
    class Count {
        @Test
        @DisplayName("returns total count")
        void count_returnsCount() {
            service.save(entity);

            long result = service.count();
            assertThat(result).isPositive();
        }

        @Test
        @DisplayName("matching query returns filtered count")
        void countByQuery_returnsCount() {
            Product saved = service.save(entity);

            query.setName(saved.getName());
            long result = service.countByQuery(query);
            assertThat(result).isOne();
        }

        @Test
        @DisplayName("non-matching query returns zero")
        void countByQuery_returnsZero() {
            query.setName("this-product-name-does-not-exist");

            long result = service.countByQuery(query);
            assertThat(result).isZero();
        }
    }

    @Nested
    class DeleteById {
        @Test
        @DisplayName("deletes product by id")
        void deleteById_success() {
            Product saved = service.save(entity);
            assertThat(saved).isNotNull();

            service.deleteById(saved.getId());

            Optional<Product> deleted = repository.findById(saved.getId());
            assertThat(deleted).isEmpty();
        }
    }

    @Nested
    class Delete {
        @Test
        @DisplayName("deletes product by entity")
        void delete_success() {
            Product saved = service.save(entity);
            assertThat(saved).isNotNull();

            service.delete(saved);

            Optional<Product> deleted = repository.findById(saved.getId());
            assertThat(deleted).isEmpty();
        }
    }
}