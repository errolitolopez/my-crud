package com.errolito.mycrud.base;

import com.errolito.mycrud.shared.BaseCrudService;
import com.errolito.mycrud.shared.BaseCrudServiceImpl;
import com.errolito.mycrud.shared.SpecBuilder;
import io.github.uncaughterrol.commons.exception.ExceptionFactory;
import io.github.uncaughterrol.commons.exception.ResourceNotFoundException;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
class BaseCrudServiceImplTests {

    @Mock
    private TestRepository repository;

    @InjectMocks
    private TestServiceImpl service;

    private TestEntity entity;
    private TestQuery query;

    private static final Long EXISTING_ID = 1L;
    private static final Long NON_EXISTENT_ID = 999L;
    private static final String NAME = "test";

    @BeforeEach
    void setUp() {
        entity = new TestEntity();
        entity.setId(EXISTING_ID);
        entity.setName(NAME);

        query = new TestQuery();
        query.setName(NAME);
    }

    @Nested
    class FindById {
        @Test
        @DisplayName("existing id returns entity")
        void findById_found_returnsEntity() {
            given(repository.findById(EXISTING_ID)).willReturn(Optional.of(entity));

            Optional<TestEntity> result = service.findById(EXISTING_ID);
            assertThat(result).isPresent().contains(entity);
        }

        @Test
        @DisplayName("non existent id returns empty")
        void findById_notFound_returnsEmpty() {
            given(repository.findById(NON_EXISTENT_ID)).willReturn(Optional.empty());

            Optional<TestEntity> result = service.findById(NON_EXISTENT_ID);
            assertThat(result).isEmpty();
        }
    }

    @Nested
    class GetById {
        @Test
        @DisplayName("existing id returns entity")
        void getById_found_returnsEntity() {
            given(repository.findById(EXISTING_ID)).willReturn(Optional.of(entity));

            TestEntity result = service.getById(EXISTING_ID);
            assertThat(result.getId()).isEqualTo(EXISTING_ID);
        }

        @Test
        @DisplayName("non existent id throws exception")
        void getById_notFound_throwsException() {
            given(repository.findById(NON_EXISTENT_ID)).willReturn(Optional.empty());

            assertThatThrownBy(() -> service.getById(NON_EXISTENT_ID))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("not found");
        }
    }

    @Nested
    class GetByIdWithSupplier {
        @Test
        @DisplayName("valid id returns entity")
        void getByIdWithSupplier_found_returnsEntity() {
            given(repository.findById(EXISTING_ID)).willReturn(Optional.of(entity));

            TestEntity result = service.getById(EXISTING_ID, () -> new ResourceNotFoundException("not found"));
            assertThat(result.getId()).isEqualTo(EXISTING_ID);
        }

        @Test
        @DisplayName("invalid id throws exception")
        void getByIdWithSupplier_notFound_throwsException() {
            given(repository.findById(NON_EXISTENT_ID)).willReturn(Optional.empty());

            assertThatThrownBy(() -> service.getById(NON_EXISTENT_ID, () -> new ResourceNotFoundException("not found")))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("not found");
        }
    }

    @Nested
    class FindAll {
        @Test
        @DisplayName("returns paged entity")
        void findAll_returnsPage() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<TestEntity> page = new PageImpl<>(List.of(entity), pageable, 1);

            given(repository.findAll(Mockito.<Specification<TestEntity>>any(), any(Pageable.class)))
                    .willReturn(page);

            Page<TestEntity> result = service.findAll(query, pageable);
            assertThat(result.getTotalElements()).isEqualTo(1);
            assertThat(result.getContent()).containsExactly(entity);
        }

        @Test
        @DisplayName("returns empty page")
        void findAll_returnsEmptyPage() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<TestEntity> emptyPage = new PageImpl<>(List.of(), pageable, 0);

            given(repository.findAll(Mockito.<Specification<TestEntity>>any(), any(Pageable.class)))
                    .willReturn(emptyPage);

            Page<TestEntity> result = service.findAll(query, pageable);

            assertThat(result.isEmpty()).isTrue();
            assertThat(result.getTotalElements()).isZero();
        }
    }

    @Nested
    class Count {
        @Test
        @DisplayName("returns count")
        void count_returnsCount() {
            given(repository.count()).willReturn(100L);

            long result = service.count();
            assertThat(result).isEqualTo(100);
        }

        @Test
        @DisplayName("valid query returns filtered count")
        void countByQuery_returnsCount() {
            given(repository.count(Mockito.<Specification<TestEntity>>any())).willReturn(3L);

            long result = service.countByQuery(query);
            assertThat(result).isEqualTo(3);
        }

        @Test
        @DisplayName("invalid query returns zero count")
        void countByQuery_notFound_returnsZeroCount() {
            given(repository.count(Mockito.<Specification<TestEntity>>any())).willReturn(0L);

            long result = service.countByQuery(query);
            assertThat(result).isZero();
        }
    }

    @Nested
    class Exist {
        @Test
        @DisplayName("valid id returns true")
        void existsById_returnsTrue() {
            given(repository.existsById(EXISTING_ID)).willReturn(true);

            boolean result = service.existsById(EXISTING_ID);
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("invalid id returns false")
        void existsById_returnsFalse() {
            given(repository.existsById(NON_EXISTENT_ID)).willReturn(false);

            boolean result = service.existsById(NON_EXISTENT_ID);
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("valid query returns true")
        void existsByQuery_returnsTrue() {
            given(repository.exists(Mockito.<Specification<TestEntity>>any())).willReturn(true);

            boolean result = service.existsByQuery(query);
            assertThat(result).isTrue();

        }

        @Test
        @DisplayName("invalid query returns false")
        void existsByQuery_returnsFalse() {
            given(repository.exists(Mockito.<Specification<TestEntity>>any())).willReturn(false);

            boolean result = service.existsByQuery(query);
            assertThat(result).isFalse();
        }
    }

    @Nested
    class Delete {
        @Test
        @DisplayName("deletes by entity")
        void delete_success() {
            willDoNothing().given(repository).delete(entity);
            given(repository.findById(entity.getId())).willReturn(Optional.empty());

            service.delete(entity);

            Optional<TestEntity> result = service.findById(entity.getId());
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("deletes by id")
        void deleteById_success() {
            willDoNothing().given(repository).deleteById(EXISTING_ID);
            given(repository.findById(EXISTING_ID)).willReturn(Optional.empty());

            service.deleteById(EXISTING_ID);

            Optional<TestEntity> result = service.findById(EXISTING_ID);
            assertThat(result).isEmpty();
        }
    }

    @Nested
    class Save {
        @Test
        @DisplayName("persists and returns saved entity")
        void save_success() {
            given(repository.save(entity)).willReturn(entity);

            TestEntity result = service.save(entity);
            assertThat(result).isEqualTo(entity);
        }
    }

    @Data
    static class TestQuery {
        private String name;
    }

    @Data
    static class TestRequest {
        private String name;
    }

    @Data
    static class TestResponse {
        private Long id;
        private String name;
    }

    @Data
    static class TestEntity {
        private Long id;
        private String name;
    }

    @Service
    static class TestServiceImpl
            extends BaseCrudServiceImpl<Long, TestQuery, TestEntity, TestRepository>
            implements TestService {
        protected TestServiceImpl(TestRepository repository) {
            super(repository);
        }

        @Override
        protected Specification<TestEntity> buildLikeSpec(TestQuery query) {
            return (root, criteriaQuery, builder) ->
                    SpecBuilder.of(root, builder)
                            .andLike("name", query.getName())
                            .build();
        }

        @Override
        protected Specification<TestEntity> buildEqualSpec(TestQuery query) {
            return (root, criteriaQuery, builder) ->
                    SpecBuilder.of(root, builder)
                            .andEqual("name", query.getName())
                            .build();
        }

        @Override
        protected Supplier<RuntimeException> notFoundException() {
            return () -> ExceptionFactory.notFound("Test not found");
        }
    }

    interface TestService extends BaseCrudService<Long, TestQuery, TestEntity> {
    }

    interface TestRepository extends JpaRepository<TestEntity, Long>, JpaSpecificationExecutor<TestEntity> {
    }
}