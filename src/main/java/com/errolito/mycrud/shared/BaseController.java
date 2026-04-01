package com.errolito.mycrud.shared;

import io.github.uncaughterrol.commons.model.ApiResponse;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {
    private static final String SUCCESS_TITLE = "Success";
    private static final String SUCCESS_DETAIL = "Operation completed successfully";

    protected <DATA> ResponseEntity<ApiResponse<DATA>> success() {
        return ResponseEntity.ok(ApiResponse.success(SUCCESS_TITLE, SUCCESS_DETAIL, 200, null));
    }

    protected <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return ResponseEntity.ok(ApiResponse.success(SUCCESS_TITLE, SUCCESS_DETAIL, 200, data));
    }
}