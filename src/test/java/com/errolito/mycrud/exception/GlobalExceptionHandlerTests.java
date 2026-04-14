package com.errolito.mycrud.exception;

import io.github.uncaughterrol.commons.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.metadata.ConstraintDescriptor;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTests {

    private GlobalExceptionHandler handler;
    private HttpServletRequest request;

    @BeforeEach
    void setup() {
        handler = new GlobalExceptionHandler();
        request = mock(HttpServletRequest.class);
        when(request.getServletPath()).thenReturn("/test");
    }

    @Test
    void shouldHandleForbiddenException() {
        var ex = new ForbiddenException("Forbidden");
        var res = handler.handleApiException(ex, request);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void shouldHandleResourceAlreadyExistsException() {
        var ex = new ResourceAlreadyExistsException("Conflict");
        var res = handler.handleApiException(ex, request);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void shouldHandleResourceNotFoundException() {
        var ex = new ResourceNotFoundException("Not found");
        var res = handler.handleApiException(ex, request);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldHandleUnauthorizedException() {
        var ex = new UnauthorizedException("Unauthorized");
        var res = handler.handleApiException(ex, request);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldHandleValidationException() {
        var ex = new ValidationException("Validation failed", List.of());
        var res = handler.handleApiException(ex, request);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldHandleDefaultApiExceptionAsInternalError() {
        var ex = new ApiException("Error", "Error") {};
        var res = handler.handleApiException(ex, request);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void shouldHandleGenericRuntimeException() {
        var res = handler.handleException(new RuntimeException(), request);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void shouldHandleNoResourceFoundException() {
        var ex = new NoResourceFoundException(HttpMethod.GET, "/test", "");
        var res = handler.handleException(ex, request);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldHandleIllegalArgumentException() {
        var res = handler.handleParameterException(new IllegalArgumentException(), request);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldHandleMissingServletRequestParameterException() {
        var ex = new MissingServletRequestParameterException("id", "String");
        var res = handler.handleParameterException(ex, request);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldHandleMethodArgumentTypeMismatchException() throws NoSuchMethodException {
        var param = new MethodParameter(Object.class.getDeclaredMethod("toString"), -1);
        var ex = new MethodArgumentTypeMismatchException("val", String.class, "id", param, null);

        var res = handler.handleParameterException(ex, request);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldHandleMethodArgumentNotValidException() throws NoSuchMethodException {
        var param = new MethodParameter(Object.class.getDeclaredMethod("toString"), -1);
        var bindingResult = new BeanPropertyBindingResult(new Object(), "obj");
        var ex = new MethodArgumentNotValidException(param, bindingResult);

        var res = handler.handleParameterException(ex, request);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldHandleNullParameterException() {
        var res = handler.handleParameterException(null, request);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldHandleConstraintViolationException() {
        ConstraintViolation<String> violation = new StubViolation("username", "must not be blank");
        var ex = new ConstraintViolationException(Set.of(violation));
        var res = handler.handleParameterException(ex, request);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    private record StubViolation(String field, String message) implements ConstraintViolation<String> {
        @Override
        public String getMessage() {
            return message;
        }

        @Override
        public jakarta.validation.Path getPropertyPath() {
            return PathImpl.createPathFromString(field);
        }

        // Unused interface methods
        @Override
        public String getMessageTemplate() {
            return null;
        }

        @Override
        public String getRootBean() {
            return null;
        }

        @Override
        public Class<String> getRootBeanClass() {
            return null;
        }

        @Override
        public Object getLeafBean() {
            return null;
        }

        @Override
        public Object[] getExecutableParameters() {
            return null;
        }

        @Override
        public Object getExecutableReturnValue() {
            return null;
        }

        @Override
        public Object getInvalidValue() {
            return null;
        }

        @Override
        public ConstraintDescriptor<?> getConstraintDescriptor() {
            return null;
        }

        @Override
        public <U> U unwrap(Class<U> type) {
            return null;
        }
    }
}