package com.errolito.mycrud.shared;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class BaseCrudServiceImpl
        <ID, QUERY, ENTITY, REPOSITORY extends JpaRepository<ENTITY, ID> & JpaSpecificationExecutor<ENTITY>>
        implements BaseCrudService<ID, QUERY, ENTITY> {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    protected final REPOSITORY repository;

    protected BaseCrudServiceImpl(REPOSITORY repository) {
        this.repository = repository;
    }

    protected abstract Specification<ENTITY> buildLikeSpec(QUERY query);

    protected abstract Specification<ENTITY> buildEqualSpec(QUERY query);

    protected abstract Supplier<RuntimeException> notFoundException();

    @Override
    public ENTITY save(ENTITY entity) {
        ENTITY saved = repository.save(entity);
        log.info("Entity saved successfully");
        return saved;
    }

    @Override
    public void delete(ENTITY entity) {
        repository.delete(entity);
        log.info("Entity deleted");
    }

    @Override
    public void deleteById(ID id) {
        repository.deleteById(id);
        log.info("Entity with ID {} deleted", id);
    }

    @Override
    public Optional<ENTITY> findById(ID id) {
        return repository.findById(id);
    }

    @Override
    public ENTITY getById(ID id) {
        return getById(id, notFoundException());
    }

    @Override
    public ENTITY getById(ID id, Supplier<RuntimeException> exception) {
        return repository.findById(id).orElseThrow(() -> {
            log.error("Entity ID {} not found", id);
            return exception.get();
        });
    }

    @Override
    public Page<ENTITY> findAll(QUERY query, Pageable pageable) {
        Page<ENTITY> page = repository.findAll(buildLikeSpec(query), pageable);
        log.debug("Found {} records", page.getTotalElements());
        return page;
    }

    @Override
    public boolean existsById(ID id) {
        return repository.existsById(id);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public long countByQuery(QUERY query) {
        return repository.count(buildLikeSpec(query));
    }

    @Override
    public boolean existsByQuery(QUERY query) {
        return repository.exists(buildEqualSpec(query));
    }
}