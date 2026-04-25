package com.errolito.mycrud.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
public abstract class CacheStore<T> {

    protected final CacheService service;
    private final ObjectMapper objectMapper = new ObjectMapper();

    protected abstract TypeReference<T> getType();

    protected abstract TypeReference<List<T>> getListType();

    public <V> V get(String name, Object key, Class<V> clazz) {
        Object obj = service.get(name, key.toString());
        if (obj == null) return null;
        return objectMapper.convertValue(obj, clazz);
    }

    public T get(String name, Object key) {
        Object obj = service.get(name, key.toString());
        if (obj == null) return null;
        return objectMapper.convertValue(obj, getType());
    }

    public List<T> getAsList(String name, Object key) {
        Object obj = service.get(name, key.toString());
        if (obj == null) return null;
        return objectMapper.convertValue(obj, getListType());
    }

    public <V> void save(String name, Object key, V data) {
        service.save(name, key.toString(), data);
    }

    public void remove(String name, Object key) {
        service.clear(name, key.toString());
    }

    public void clear(String name) {
        service.clear(name);
    }
}
