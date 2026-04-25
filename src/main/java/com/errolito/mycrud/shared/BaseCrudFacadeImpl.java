package com.errolito.mycrud.shared;

import com.errolito.mycrud.cache.CacheConfig;
import com.errolito.mycrud.cache.CacheKeyUtils;
import com.errolito.mycrud.cache.CacheStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public abstract class BaseCrudFacadeImpl<I, Q, R, E, T> implements BaseCrudFacade<I, Q, R, T> {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    protected final BaseMapper<R, E, T> mapper;
    protected final BaseCrudService<I, Q, E> service;
    protected final CacheStore<T> cacheStore;

    protected BaseCrudFacadeImpl(
            BaseMapper<R, E, T> mapper,
            BaseCrudService<I, Q, E> service,
            CacheStore<T> cacheStore
    ) {
        this.mapper = mapper;
        this.service = service;
        this.cacheStore = cacheStore;
    }

    protected CacheConfig cacheConfig() {
        return new CacheConfig();
    }

    @Override
    @Transactional
    public Page<T> findAll(Q query, Pageable pageable) {
        if (!cacheConfig().isCacheable()) {
            return service.findAll(query, pageable).map(mapper::toResponse);
        }

        String pageContentCacheName = cacheConfig().getName() + "PageContents";
        String totalElementsCacheName = cacheConfig().getName() + "PageTotalElements";
        String key = CacheKeyUtils.pageCacheKey(query, pageable);

        List<T> pageContent = cacheStore.getAsList(pageContentCacheName, key);
        if (pageContent != null) {
            long totalElements = cacheStore.get(totalElementsCacheName, key, Long.class);

            return new PageImpl<>(pageContent, pageable, totalElements);
        } else {
            Page<T> page = service.findAll(query, pageable).map(mapper::toResponse);

            cacheStore.save(pageContentCacheName, key, page.getContent());
            cacheStore.save(totalElementsCacheName, key, page.getTotalElements());

            return page;
        }
    }

    @Override
    @Transactional
    public Optional<T> findById(I id) {
        if (!cacheConfig().isCacheable()) {
            return service.findById(id).map(mapper::toResponse);
        }

        T response = cacheStore.get(cacheConfig().getName(), id);

        if (response == null) {
            response = service.findById(id).map(mapper::toResponse).orElse(null);
            cacheStore.save(cacheConfig().getName(), id, response);
        }

        return Optional.ofNullable(response);
    }

    @Override
    @Transactional
    public T getById(I id) {
        if (!cacheConfig().isCacheable()) {
            return mapper.toResponse(service.getById(id));
        }

        T response = cacheStore.get(cacheConfig().getName(), id);
        if (response == null) {
            response = mapper.toResponse(service.getById(id));
            cacheStore.save(cacheConfig().getName(), id, response);
        }
        return response;
    }

    @Override
    @Transactional
    public T save(R request) {
        log.info("Processing save request");

        E entity = mapper.toEntity(request);
        E createdEntity = service.save(entity);

        clearCache();

        return mapper.toResponse(createdEntity);
    }

    @Override
    @Transactional
    public T update(I id, R request) {
        log.info("Processing update request");

        E entity = service.getById(id);
        mapper.fromRequest(request, entity);

        E updatedEntity = service.save(entity);

        clearCache(id);

        return mapper.toResponse(updatedEntity);
    }

    @Override
    @Transactional
    public void deleteById(I id) {
        log.info("Processing deletion for I: {}", id);
        service.deleteById(id);
        clearCache(id);
    }

    private void clearCache() {
        clearCache(null);
    }

    private void clearCache(I id) {
        if (cacheConfig().isCacheable()) {
            if (id != null) {
                cacheStore.remove(cacheConfig().getName(), id);
            }

            cacheStore.clear(cacheConfig().getName() + "PageContents");
            cacheStore.clear(cacheConfig().getName() + "PageTotalElements");
        }
    }
}