package com.errolito.mycrud.shared;

import io.github.uncaughterrol.commons.model.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Generic base controller for CRUD endpoints.
 *
 * @param <I> ID type
 * @param <Q> Query filter type
 * @param <R> Request body type
 * @param <T> Response body type
 */
@Validated
public abstract class BaseCrudController<I, Q, R, T> extends BaseController {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    protected final BaseCrudFacade<I, Q, R, T> facade;

    /**
     * @param facade Facade that handles business logic
     */
    protected BaseCrudController(BaseCrudFacade<I, Q, R, T> facade) {
        this.facade = facade;
    }

    /**
     * Fetch paginated list based on query.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<T>>> findAll(
            @ParameterObject @Parameter Q query,
            @ParameterObject @Parameter Pageable pageable) {
        log.debug("REST request to get a page of entities");
        return success(facade.findAll(query, pageable));
    }

    /**
     * Fetch single record by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<T>> getById(@PathVariable I id) {
        log.debug("REST request to get entity: {}", id);
        return success(facade.getById(id));
    }

    /**
     * Create new record.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<T>> create(@Valid @RequestBody R request) {
        log.info("REST request to create new entity");
        return success(facade.save(request));
    }

    /**
     * Update existing record by ID.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<T>> update(
            @PathVariable I id,
            @Valid @RequestBody R request) {
        log.info("REST request to update entity: {}", id);
        return success(facade.update(id, request));
    }

    /**
     * Delete record by ID.
     */
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> deleteById(@PathVariable I id) {
        log.info("REST request to delete entity: {}", id);
        facade.deleteById(id);
        return success();
    }
}