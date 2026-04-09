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

@Validated
public abstract class BaseCrudController<ID, QUERY, REQUEST, RESPONSE>
        extends BaseController {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    protected final BaseCrudFacade<ID, QUERY, REQUEST, RESPONSE> facade;

    protected BaseCrudController(BaseCrudFacade<ID, QUERY, REQUEST, RESPONSE> facade) {
        this.facade = facade;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<RESPONSE>>> findAll(@ParameterObject @Parameter QUERY query, @ParameterObject @Parameter Pageable pageable) {
        log.debug("REST request to get a page of entities");
        return success(facade.findAll(query, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RESPONSE>> getById(@PathVariable("id") ID id) {
        log.debug("REST request to get entity: {}", id);
        return success(facade.getById(id));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RESPONSE>> create(@Valid @RequestBody REQUEST request) {
        log.info("REST request to create new entity");
        return success(facade.save(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RESPONSE>> update(@Valid @PathVariable ID id, @RequestBody REQUEST request) {
        log.info("REST request to update entity: {}", id);
        return success(facade.update(id, request));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> deleteById(@PathVariable ID id) {
        log.info("REST request to delete entity: {}", id);
        facade.deleteById(id);
        return success();
    }
}