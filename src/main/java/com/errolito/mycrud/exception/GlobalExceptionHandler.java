package com.errolito.mycrud.exception;

import io.github.uncaughterrol.commons.exception.*;
import io.github.uncaughterrol.commons.model.ApiResponse;
import io.github.uncaughterrol.commons.model.InvalidParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.status;

@RestControllerAdvice
public class GlobalExceptionHandler {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Void>> handleApiException(ApiException e, HttpServletRequest request) {
        Collection<InvalidParam> invalidParams = e.getInvalidParams();

        HttpStatus status = switch (e) {
            case ForbiddenException ignored -> FORBIDDEN;
            case ResourceAlreadyExistsException ignored -> CONFLICT;
            case ResourceNotFoundException ignored -> NOT_FOUND;
            case UnauthorizedException ignored -> UNAUTHORIZED;
            case ValidationException ignored -> BAD_REQUEST;
            default -> INTERNAL_SERVER_ERROR;
        };

        logException(e, request);

        return status(status).body(ApiResponse.error(e.getTitle(), e.getDetail(), status.value(), invalidParams));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e, HttpServletRequest request) {
        HttpStatus status;

        String className = e.getClass().getSimpleName();

        String title = "Internal Server Error";
        String detail = "An unexpected error occurred, please try again later";

        if (className.equals("NoResourceFoundException")) {
            status = NOT_FOUND;

            title = "Resource Not Found";
            detail = "The requested resource was not found";
        } else {
            status = INTERNAL_SERVER_ERROR;
        }

        logException(e, request);

        return status(status).body(ApiResponse.error(title, detail, status.value()));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public Object handleNoResourceFound(NoResourceFoundException e, HttpServletRequest request) {
        String path = request.getServletPath();

        if (path.startsWith("/api")) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            return ResponseEntity
                    .status(status)
                    .body(ApiResponse.error("Resource Not Found", "The requested resource was not found", status.value()));
        }

        return new ModelAndView("component/error/page-not-found");
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse<Void>> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e, HttpServletRequest request) {
        HttpStatus status = CONTENT_TOO_LARGE;
        String title = "File Too Large";
        String detail = "File exceeds the 10 MB limit.";
        logException(e, request);
        return status(status).body(ApiResponse.error(title, detail, status.value()));
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class,
            ConstraintViolationException.class
    })
    public ResponseEntity<ApiResponse<Void>> handleParameterException(Exception e, HttpServletRequest request) {
        HttpStatus status = BAD_REQUEST;

        String title = "Validation Failed";
        String detail = "Request contains one or more invalid parameters";

        Collection<InvalidParam> invalidParams = switch (e) {
            case MethodArgumentNotValidException cause -> getInvalidParams(cause);
            case IllegalArgumentException cause -> getInvalidParams(cause);
            case ConstraintViolationException cause -> getInvalidParams(cause);
            case MethodArgumentTypeMismatchException cause -> Collections.singletonList(
                    new InvalidParam(cause.getName(), "Invalid input for field " + cause.getName())
            );
            case MissingServletRequestParameterException cause -> Collections.singletonList(
                    new InvalidParam(
                            cause.getParameterName(), "Invalid input for field " + cause.getParameterName()
                    )
            );
            // null case added as a safeguard to avoid NPE if this handler is called unexpectedly with null
            case null, default -> Collections.emptyList();
        };

        if (e != null) {
            logException(e, request);
        }

        return status(status).body(ApiResponse.error(title, detail, status.value(), invalidParams));
    }

    private void logException(Exception e, HttpServletRequest request) {
        log.error("exception: '{}' message: '{}' path: '{}'", e.getClass().getSimpleName(), e.getMessage(), request.getServletPath());
    }

    private Collection<InvalidParam> getInvalidParams(Exception e) {
        if (e instanceof MethodArgumentNotValidException ex) {
            return ex.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(field -> new InvalidParam(field.getField(), field.getDefaultMessage()))
                    .toList();
        }

        if (e instanceof ConstraintViolationException ex) {
            return ex.getConstraintViolations()
                    .stream()
                    .map(v -> {
                        String field = Optional.ofNullable(v.getPropertyPath())
                                .map(Object::toString)
                                .map(p -> {
                                    int i = p.lastIndexOf('.');
                                    return i >= 0 ? p.substring(i + 1) : p;
                                })
                                .orElse("unknown");

                        return new InvalidParam(field, v.getMessage());
                    })
                    .toList();
        }

        return List.of();
    }
}