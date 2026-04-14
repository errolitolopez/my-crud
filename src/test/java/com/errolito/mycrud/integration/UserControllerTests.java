package com.errolito.mycrud.integration;

import com.errolito.mycrud.config.FakerConfig;
import com.errolito.mycrud.config.WebConfig;
import com.errolito.mycrud.controller.UserController;
import com.errolito.mycrud.dto.UserQuery;
import com.errolito.mycrud.dto.UserRequest;
import com.errolito.mycrud.dto.UserResponse;
import com.errolito.mycrud.exception.GlobalExceptionHandler;
import com.errolito.mycrud.facade.UserFacade;
import com.github.javafaker.Faker;
import io.github.uncaughterrol.commons.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(value = {GlobalExceptionHandler.class, WebConfig.class, FakerConfig.class})
class UserControllerTests {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    private final Faker faker;

    @MockitoBean
    private UserFacade facade;

    private UserResponse response;
    private UserRequest request;

    private static final String BASE_URL = "/api/v1/users";
    private static final Integer ID = 1;

    private static String USERNAME = "";

    @Autowired
    UserControllerTests(MockMvc mockMvc, ObjectMapper objectMapper, Faker faker) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.faker = faker;
    }

    @BeforeEach
    void setUp() {
        USERNAME = faker.name().username();

        request = new UserRequest();
        request.setUsername(USERNAME);
        request.setFullName(faker.name().fullName());

        response = new UserResponse();
        response.setId(ID);
        response.setUsername(USERNAME);
    }

    @Nested
    @DisplayName("GET /api/v1/users")
    class FindAll {
        @Test
        @DisplayName("200 — returns paged response")
        void findAll_200() throws Exception {
            Pageable pageable = PageRequest.of(0, 10);
            Page<UserResponse> page = new PageImpl<>(List.of(response), pageable, 1);

            given(facade.findAll(any(UserQuery.class), any(Pageable.class))).willReturn(page);

            mockMvc.perform(get(BASE_URL)
                            .param("page", "0")
                            .param("size", "10")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.data.content", hasSize(1)))
                    .andExpect(jsonPath("$.data.content[0].username").value(USERNAME))
                    .andExpect(jsonPath("$.data.page.totalElements").value(1));
        }

        @Test
        @DisplayName("200 — returns empty page")
        void findAll_empty_200() throws Exception {
            Page<UserResponse> emptyPage = Page.empty();

            given(facade.findAll(any(UserQuery.class), any(Pageable.class))).willReturn(emptyPage);

            mockMvc.perform(get(BASE_URL).accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.content", hasSize(0)))
                    .andExpect(jsonPath("$.data.page.totalElements").value(0));
        }
    }

    @Nested
    @DisplayName("GET /api/v1/users/{id}")
    class GetById {
        @Test
        @DisplayName("200 — returns response")
        void getById_200() throws Exception {
            given(facade.getById(ID)).willReturn(response);

            mockMvc.perform(get(BASE_URL + "/{id}", ID).accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.data.id").value(ID))
                    .andExpect(jsonPath("$.data.username").value(USERNAME));
        }

        @Test
        @DisplayName("404 — invalid id throws ResourceNotFoundException")
        void getById_404() throws Exception {
            given(facade.getById(ID)).willThrow(new ResourceNotFoundException("User not found"));

            mockMvc.perform(get(BASE_URL + "/{id}", ID).accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(404));
        }
    }

    @Nested
    @DisplayName("POST /api/v1/users")
    class Create {
        @Test
        @DisplayName("200 — valid request returns created response")
        void create_200() throws Exception {
            given(facade.save(any(UserRequest.class))).willReturn(response);

            mockMvc.perform(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.data.username").value(USERNAME));
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/users")
    class Update {
        @Test
        @DisplayName("200 — valid request returns updated response")
        void update_200() throws Exception {
            given(facade.update(eq(ID), any(UserRequest.class))).willReturn(response);

            mockMvc.perform(put(BASE_URL + "/{id}", ID).accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.data.username").value(USERNAME));
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/users/{id}")
    class DeleteById {

        @Test
        @DisplayName("200 — delete")
        void deleteById_200() throws Exception {
            willDoNothing().given(facade).deleteById(ID);

            mockMvc.perform(delete(BASE_URL + "/{id}", ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.data").doesNotExist());
        }
    }
}