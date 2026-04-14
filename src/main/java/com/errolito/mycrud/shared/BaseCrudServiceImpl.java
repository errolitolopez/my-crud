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

public abstract class BaseCrudServiceImpl<I, Q, E, D extends JpaRepository<E, I> & JpaSpecificationExecutor<E>>
        implements BaseCrudService<I, Q, E> {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    protected final D repository;

    protected BaseCrudServiceImpl(D repository) {
        this.repository = repository;
    }

    protected abstract Specification<E> buildLikeSpec(Q query);

    protected abstract Specification<E> buildEqualSpec(Q query);

    protected abstract Supplier<RuntimeException> notFoundException();

    @Override
    public E save(E entity) {
        E saved = repository.save(entity);
        log.info("Entity saved successfully");
        return saved;
    }

    @Override
    public void delete(E entity) {
        repository.delete(entity);
        log.info("Entity deleted");
    }

    @Override
    public void deleteById(I id) {
        repository.deleteById(id);
        log.info("Entity with I {} deleted", id);
    }

    @Override
    public Optional<E> findById(I id) {
        return repository.findById(id);
    }

    @Override
    public E getById(I id) {
        return getById(id, notFoundException());
    }

    @Override
    public E getById(I id, Supplier<RuntimeException> exception) {
        return repository.findById(id).orElseThrow(() -> {
            log.error("Entity I {} not found", id);
            return exception.get();
        });
    }

    @Override
    public Page<E> findAll(Q query, Pageable pageable) {
        Page<E> page = repository.findAll(buildLikeSpec(query), pageable);
        log.debug("Found {} records", page.getTotalElements());
        return page;
    }

    @Override
    public boolean existsById(I id) {
        return repository.existsById(id);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public long countByQuery(Q query) {
        return repository.count(buildLikeSpec(query));
    }

    @Override
    public boolean existsByQuery(Q query) {
        return repository.exists(buildEqualSpec(query));
    }
}