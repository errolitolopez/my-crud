package com.errolito.mycrud.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheService {

    private final CacheManager cacheManager;

    public Object get(String name, String key) {
        return get(name, key, Object.class);
    }

    public <T> T get(String name, String key, Class<T> type) {
        Cache cache = getCache(name);
        if (cache == null) {
            log.debug("Cache '{}' not found while getting key: {}", name, key);
            return null;
        }

        Cache.ValueWrapper wrapper = cache.get(key);
        if (wrapper == null) {
            log.debug("Cache miss for {} with key: {}", name, key);
            return null;
        }

        Object value = wrapper.get();
        if (value == null) return null;

        log.debug("Cache hit for {} with key: {}", name, key);
        return type.cast(value);
    }

    public <T> void save(String name, String key, T data) {
        Cache cache = getCache(name);
        if (cache == null) {
            log.debug("Cache '{}' not found while saving key: {}", name, key);
            return;
        }

        try {
            cache.put(key, data);
            log.debug("Cached '{}' with key: {}", name, key);
        } catch (Exception e) {
            log.debug("Cache save failed for key: {} - {}", key, e.getMessage());
        }
    }

    public void clear(String name, String key) {
        Cache cache = getCache(name);
        if (cache == null) {
            log.debug("Cache '{}' not found while clearing key: {}", name, key);
            return;
        }

        cache.evict(key);
        log.debug("Cache entry cleared for key: {}", key);
    }

    public void clear(String name) {
        Cache cache = getCache(name);
        if (cache == null) {
            log.debug("Cache '{}' not found", name);
            return;
        }

        cache.clear();
        log.debug("Cache entry cleared for: {}", name);
    }

    public Cache getCache(String name) {
        return cacheManager.getCache(name);
    }
}