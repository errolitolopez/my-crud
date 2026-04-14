package com.errolito.mycrud.shared;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Generic facade contract for CRUD operations.
 *
 * @param <I> ID type
 * @param <Q> Query filter type
 * @param <R> Request body type
 * @param <T> Response body type
 */
public interface BaseCrudFacade<I, Q, R, T> {

    /**
     * Fetch paginated results using filter.
     */
    Page<T> findAll(Q query, Pageable pageable);

    /**
     * Find record by ID. Returns empty if not found.
     */
    Optional<T> findById(I id);

    /**
     * Get record by ID. Throws if not found.
     */
    T getById(I id);

    /**
     * Create a new record.
     */
    T save(R request);

    /**
     * Update existing record.
     */
    T update(I id, R request);

    /**
     * Delete record by ID.
     */
    void deleteById(I id);
}