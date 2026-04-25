package com.errolito.mycrud.base;

import com.errolito.mycrud.base.BaseCrudServiceImplTests.*;
import com.errolito.mycrud.cache.CacheStore;
import com.errolito.mycrud.shared.BaseCrudFacade;
import com.errolito.mycrud.shared.BaseCrudFacadeImpl;
import com.errolito.mycrud.shared.BaseMapper;
import io.github.uncaughterrol.commons.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class BaseCrudFacadeImplTests {

    @Mock
    private TestMapper mapper;

    @Mock
    private TestService service;

    @InjectMocks
    private TestFacadeImpl facade;

    private static final Long EXISTING_ID = 1L;
    private static final Long NON_EXISTENT_ID = 999L;
    private static final String NAME = "test";

    private TestEntity entity;
    private TestQuery query;
    private TestRequest request;
    private TestResponse response;

    @BeforeEach
    void setUp() {
        entity = new TestEntity();
        entity.setId(EXISTING_ID);
        entity.setName(NAME);

        query = new TestQuery();
        query.setName(NAME);

        request = new TestRequest();
        request.setName(NAME);

        response = new TestResponse();
        response.setId(EXISTING_ID);
        response.setName(NAME);
    }

    @Nested
    class FindById {
        @Test
        @DisplayName("existing id returns entity")
        void findById_found_returnsResponse() {
            given(service.findById(EXISTING_ID)).willReturn(Optional.of(entity));
            given(mapper.toResponse(entity)).willReturn(response);

            Optional<TestResponse> result = facade.findById(EXISTING_ID);
            assertThat(result).isPresent();
            assertThat(result.get().getId()).isEqualTo(EXISTING_ID);
        }

        @Test
        @DisplayName("non existent id returns empty")
        void findById_notFound_throwsResourceNotFoundException() {
            given(service.findById(NON_EXISTENT_ID)).willReturn(Optional.of(entity));

            Optional<TestResponse> result = facade.findById(NON_EXISTENT_ID);
            assertThat(result).isEmpty();
        }
    }

    @Nested
    class GetById {
        @Test
        @DisplayName("returns response")
        void getById_found_returnsResponse() {
            given(service.getById(EXISTING_ID)).willReturn(entity);
            given(mapper.toResponse(entity)).willReturn(response);

            TestResponse result = facade.getById(EXISTING_ID);
            assertThat(result.getId()).isEqualTo(EXISTING_ID);
        }

        @Test
        @DisplayName("non existent id throws exception")
        void getById_notFound_throwsException() {
            given(service.getById(NON_EXISTENT_ID)).willThrow(ResourceNotFoundException.class);

            assertThatThrownBy(() -> service.getById(NON_EXISTENT_ID)).isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    class FindAll {
        @Test
        @DisplayName("returns paged response")
        void findAll_returnsPagedResponse() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<TestEntity> page = new PageImpl<>(List.of(entity), pageable, 1);

            given(service.findAll(query, pageable)).willReturn(page);
            given(mapper.toResponse(entity)).willReturn(response);

            Page<TestResponse> result = facade.findAll(query, pageable);
            assertThat(result.getTotalElements()).isEqualTo(1);
        }

        @Test
        @DisplayName("returns empty page")
        void findAll_returnsEmptyPage() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<TestEntity> emptyPage = new PageImpl<>(List.of(), pageable, 0);

            given(service.findAll(query, pageable)).willReturn(emptyPage);

            Page<TestResponse> result = facade.findAll(query, pageable);
            assertThat(result.getTotalElements()).isZero();
        }
    }

    @Nested
    class Save {
        @Test
        @DisplayName("valid request returns created response")
        void save_success() {
            given(mapper.toEntity(request)).willReturn(entity);
            given(service.save(entity)).willReturn(entity);
            given(mapper.toResponse(entity)).willReturn(response);

            TestResponse result = facade.save(request);
            assertThat(result).isEqualTo(response);
        }
    }

    @Nested
    class Update {
        @Test
        @DisplayName("valid request returns updated response")
        void update_success() {
            given(service.getById(EXISTING_ID)).willReturn(entity);
            given(service.save(entity)).willReturn(entity);
            given(mapper.toResponse(entity)).willReturn(response);

            TestResponse result = facade.update(EXISTING_ID, request);
            assertThat(result).isEqualTo(response);
            then(service).should(never()).existsByQuery(any());
        }

        @Test
        @DisplayName("non existent id throws exception")
        void update_failed_throwsException() {
            given(service.getById(NON_EXISTENT_ID)).willThrow(ResourceNotFoundException.class);

            assertThatThrownBy(() -> service.getById(NON_EXISTENT_ID)).isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    class DeleteById {
        @Test
        @DisplayName("deletes by id")
        void delete_success() {
            willDoNothing().given(service).deleteById(EXISTING_ID);

            facade.deleteById(EXISTING_ID);
        }
    }

    interface TestFacade extends BaseCrudFacade<Long, TestQuery, TestRequest, TestResponse> {
    }

    static class TestFacadeImpl
            extends BaseCrudFacadeImpl<Long, TestQuery, TestRequest, TestEntity, TestResponse>
            implements TestFacade {
        protected TestFacadeImpl(TestMapper mapper, TestService service, CacheStore<TestResponse> cacheStore) {
            super(mapper, service, cacheStore);
        }
    }

    interface TestMapper extends BaseMapper<TestRequest, TestEntity, TestResponse> {
    }
}