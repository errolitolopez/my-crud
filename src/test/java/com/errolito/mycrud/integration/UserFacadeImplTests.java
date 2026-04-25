package com.errolito.mycrud.integration;

import com.errolito.mycrud.cache.CacheConfig;
import com.errolito.mycrud.dto.UserQuery;
import com.errolito.mycrud.dto.UserRequest;
import com.errolito.mycrud.dto.UserResponse;
import com.errolito.mycrud.entity.User;
import com.errolito.mycrud.facade.impl.UserFacadeImpl;
import com.errolito.mycrud.repository.UserRepository;
import com.github.javafaker.Faker;
import io.github.uncaughterrol.commons.exception.ResourceAlreadyExistsException;
import io.github.uncaughterrol.commons.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static io.github.uncaughterrol.commons.utils.TokenGenerator.secureAlphanumericToken;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UserFacadeImplTests {

    private final Faker faker;
    private final UserFacadeImpl facade;
    private final UserRepository repository;

    private String username;
    private String fullName;

    private UserQuery query;
    private UserRequest request;

    @Autowired
    UserFacadeImplTests(
            Faker faker,
            UserFacadeImpl facade,
            UserRepository repository
    ) {
        this.facade = facade;
        this.faker = faker;
        this.repository = repository;
    }

    private static String generateUsername() {
        return secureAlphanumericToken(16);
    }

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(facade, "cacheConfig", new CacheConfig("user", false));
        fullName = faker.name().fullName();

        username = generateUsername();

        query = new UserQuery();
        query.setUsername(username);

        request = new UserRequest();
        request.setUsername(username);
        request.setFullName(fullName);
    }

    @Nested
    class Create {
        @Test
        @DisplayName("valid request returns created response")
        void save_success() {
            UserResponse result = facade.save(request);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isNotNull();
            assertThat(result.getUsername()).isEqualToIgnoringCase(username);
            assertThat(result.getUserProfile()).isNotNull();
            assertThat(result.getUserProfile().getFullName()).isEqualTo(fullName);
        }

        @Test
        @DisplayName("duplicate username throws ResourceAlreadyExistsException")
        void save_failed_throwsResourceAlreadyExistsException() {
            UserResponse user = facade.save(request);
            assertThat(user).isNotNull();

            assertThatThrownBy(() -> facade.save(request))
                    .isInstanceOf(ResourceAlreadyExistsException.class)
                    .hasMessage("Username already exists");
        }
    }

    @Nested
    class Update {
        @Test
        @DisplayName("valid request returns updated response")
        void update_success() {
            UserResponse user = facade.save(request);
            assertThat(user).isNotNull();

            request.setUsername(generateUsername());
            request.setFullName(faker.name().fullName());

            UserResponse result = facade.update(user.getId(), request);
            assertThat(result.getUsername()).isEqualToIgnoringCase(request.getUsername());
            assertThat(result.getUserProfile().getFullName()).isEqualTo(request.getFullName());
        }

        @Test
        @DisplayName("duplicate username throws ResourceAlreadyExistsException")
        void update_failed_throwsResourceAlreadyExistsException() {
            UserResponse user1 = facade.save(request);
            assertThat(user1).isNotNull();

            request.setUsername(generateUsername());
            request.setFullName(faker.name().fullName());

            UserResponse user2 = facade.save(request);
            assertThat(user2).isNotNull();

            assertThat(user1.getUsername()).isNotEqualTo(user2.getUsername());
            assertThat(user1.getUserProfile().getFullName()).isNotEqualTo(user2.getUserProfile().getFullName());

            request.setUsername(user1.getUsername());
            Integer id = user2.getId();
            assertThatThrownBy(() -> facade.update(id, request))
                    .isInstanceOf(ResourceAlreadyExistsException.class)
                    .hasMessage("Username already exists");
        }
    }

    @Nested
    class FindAll {
        private static final Pageable PAGEABLE = PageRequest.of(0, 10);

        @Test
        @DisplayName("returns paged response")
        void findAll_returnsPagedResponse() {
            UserResponse user = facade.save(request); // ensures that DB has records
            assertThat(user).isNotNull();

            query.setUsername(null); // remove filters

            Page<UserResponse> result = facade.findAll(query, PAGEABLE);
            assertThat(result.getTotalElements()).isNotZero();
        }

        @Test
        @DisplayName("returns empty page when query is invalid")
        void findAll_returnsEmptyPage_when_queryIsInvalid() {
            query.setUsername("this-username-does-not-exists");

            Page<UserResponse> result = facade.findAll(query, PAGEABLE);
            assertThat(result.getTotalElements()).isZero();
        }

        @Test
        @DisplayName("returns filtered paged response")
        void findAll_filteredPagedResponse() {
            UserResponse user = facade.save(request);
            assertThat(user).isNotNull();

            query.setUsername(user.getUsername());

            Page<UserResponse> result = facade.findAll(query, PAGEABLE);
            assertThat(result.getTotalElements()).isOne();
            assertThat(result.getContent().getFirst().getUsername()).isEqualTo(query.getUsername());
        }
    }

    @Nested
    class GetById {
        @Test
        @DisplayName("returns response")
        void getById_found_returnsResponse() {
            UserResponse user = facade.save(request);
            assertThat(user).isNotNull();

            UserResponse result = facade.getById(user.getId());
            assertThat(result).isNotNull();
            assertThat(user)
                    .usingRecursiveComparison()
                    .ignoringFields("createdDate")
                    .isEqualTo(result);
        }

        @Test
        @DisplayName("invalid id throws ResourceNotFoundException")
        void getById_notFound_throwsResourceNotFoundException() {
            assertThatThrownBy(() -> facade.getById(999_999_999))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("User not found");
        }
    }

    @Nested
    class DeleteById {
        @Test
        @DisplayName("deletes by id")
        void delete_success() {
            UserResponse user = facade.save(request);
            assertThat(user).isNotNull();

            facade.deleteById(user.getId());

            Optional<User> deleted = repository.findById(user.getId());
            assertThat(deleted).isEmpty();
        }
    }

    @Nested
    class FindById {
        @Test
        @DisplayName("returns response")
        void getById_found_returnsResponse() {
            UserResponse user = facade.save(request);
            assertThat(user).isNotNull();

            Optional<UserResponse> result = facade.findById(user.getId());
            assertThat(result).isNotEmpty();
            assertThat(user)
                    .usingRecursiveComparison()
                    .ignoringFields("createdDate")
                    .isEqualTo(result.get());
        }
    }
}