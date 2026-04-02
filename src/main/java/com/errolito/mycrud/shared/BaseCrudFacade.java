package com.errolito.mycrud.shared;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BaseCrudFacade<ID, QUERY, REQUEST, RESPONSE> {
    Page<RESPONSE> findAll(QUERY query, Pageable pageable);

    Optional<RESPONSE> findById(ID id);

    RESPONSE getById(ID id);

    RESPONSE save(REQUEST request);

    RESPONSE update(ID id, REQUEST request);

    void deleteById(ID id);
}
