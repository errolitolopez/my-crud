package com.errolito.mycrud.cache;

import com.errolito.mycrud.dto.FileResponse;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;

import java.lang.reflect.Type;
import java.util.List;

@Component
public class FileCacheStore extends CacheStore<FileResponse> {

    public FileCacheStore(CacheService service) {
        super(service);
    }

    @Override
    protected TypeReference<FileResponse> getType() {
        return new TypeReference<>() {
            @Override
            public Type getType() {
                return super.getType();
            }
        };
    }

    @Override
    protected TypeReference<List<FileResponse>> getListType() {
        return new TypeReference<>() {
            @Override
            public Type getType() {
                return super.getType();
            }
        };
    }
}
