package com.errolito.mycrud.shared;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Generic service contract for CRUD operations.
 *
 * @param <I> ID type
 * @param <Q> Query filter type
 * @param <E> Entity type
 */
public interface BaseCrudService<I, Q, E> {

    /**
     * Save or update an entity.
     */
    E save(E entity);

    /**
     * Delete an entity.
     */
    void delete(E entity);

    /**
     * Delete entity by ID.
     */
    void deleteById(I id);

    /**
     * Find entity by ID. Returns empty if not found.
     */
    Optional<E> findById(I id);

    /**
     * Count all entities.
     */
    long count();

    /**
     * Count entities using filter.
     */
    long countByQuery(Q query);

    /**
     * Get entity by ID. Throws if not found.
     */
    E getById(I id);

    /**
     * Get entity by ID with custom exception.
     */
    E getById(I id, Supplier<RuntimeException> exception);

    /**
     * Fetch paginated entities using filter.
     */
    Page<E> findAll(Q query, Pageable pageable);

    /**
     * Check if entity exists by ID.
     */
    boolean existsById(I id);

    /**
     * Check if entity exists using filter.
     */
    boolean existsByQuery(Q query);
}