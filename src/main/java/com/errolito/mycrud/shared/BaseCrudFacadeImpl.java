package com.errolito.mycrud.shared;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public abstract class BaseCrudFacadeImpl<ID, QUERY, REQUEST, ENTITY, RESPONSE>
        implements BaseCrudFacade<ID, QUERY, REQUEST, RESPONSE> {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    protected final BaseMapper<REQUEST, ENTITY, RESPONSE> mapper;
    protected final BaseCrudService<ID, QUERY, ENTITY> service;

    protected BaseCrudFacadeImpl(
            BaseMapper<REQUEST, ENTITY, RESPONSE> mapper,
            BaseCrudService<ID, QUERY, ENTITY> service
    ) {
        this.mapper = mapper;
        this.service = service;
    }

    @Override
    @Transactional
    public Page<RESPONSE> findAll(QUERY query, Pageable pageable) {
        return service.findAll(query, pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional
    public Optional<RESPONSE> findById(ID id) {
        return service
                .findById(id)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional
    public RESPONSE getById(ID id) {
        return mapper.toResponse(service.getById(id));
    }

    @Override
    @Transactional
    public RESPONSE save(REQUEST request) {
        log.info("Processing save request");

        ENTITY entity = mapper.toEntity(request);
        ENTITY createdEntity = service.save(entity);

        return mapper.toResponse(createdEntity);
    }

    @Override
    @Transactional
    public RESPONSE update(ID id, REQUEST request) {
        log.info("Processing update request");

        ENTITY entity = service.getById(id);
        mapper.fromRequest(request, entity);

        ENTITY updatedEntity = service.save(entity);

        return mapper.toResponse(updatedEntity);
    }

    @Override
    @Transactional
    public void deleteById(ID id) {
        log.info("Processing deletion for ID: {}", id);
        service.deleteById(id);
    }
}
