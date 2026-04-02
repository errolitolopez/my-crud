package com.errolito.mycrud.shared;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.function.Supplier;

public interface BaseCrudService<ID, QUERY, ENTITY> {
    ENTITY save(ENTITY entity);

    void delete(ENTITY entity);

    void deleteById(ID id);

    Optional<ENTITY> findById(ID id);

    long count();

    long countByQuery(QUERY query);

    ENTITY getById(ID id);

    ENTITY getById(ID id, Supplier<RuntimeException> exception);

    Page<ENTITY> findAll(QUERY query, Pageable pageable);

    boolean existsById(ID id);

    boolean existsByQuery(QUERY query);
}