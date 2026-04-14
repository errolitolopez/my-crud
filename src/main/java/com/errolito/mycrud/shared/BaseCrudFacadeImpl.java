package com.errolito.mycrud.shared;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public abstract class BaseCrudFacadeImpl<I, Q, R, E, T> implements BaseCrudFacade<I, Q, R, T> {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    protected final BaseMapper<R, E, T> mapper;
    protected final BaseCrudService<I, Q, E> service;

    protected BaseCrudFacadeImpl(
            BaseMapper<R, E, T> mapper,
            BaseCrudService<I, Q, E> service
    ) {
        this.mapper = mapper;
        this.service = service;
    }

    @Override
    @Transactional
    public Page<T> findAll(Q query, Pageable pageable) {
        return service.findAll(query, pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional
    public Optional<T> findById(I id) {
        return service
                .findById(id)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional
    public T getById(I id) {
        return mapper.toResponse(service.getById(id));
    }

    @Override
    @Transactional
    public T save(R request) {
        log.info("Processing save request");

        E entity = mapper.toEntity(request);
        E createdEntity = service.save(entity);

        return mapper.toResponse(createdEntity);
    }

    @Override
    @Transactional
    public T update(I id, R request) {
        log.info("Processing update request");

        E entity = service.getById(id);
        mapper.fromRequest(request, entity);

        E updatedEntity = service.save(entity);

        return mapper.toResponse(updatedEntity);
    }

    @Override
    @Transactional
    public void deleteById(I id) {
        log.info("Processing deletion for I: {}", id);
        service.deleteById(id);
    }
}
