package com.errolito.mycrud.cache;

import com.errolito.mycrud.security.dto.RateLimit;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;

import java.lang.reflect.Type;
import java.util.List;

@Component
public class RateLimitCacheStore extends CacheStore<RateLimit> {
    public RateLimitCacheStore(CacheService service) {
        super(service);
    }

    @Override
    protected TypeReference<RateLimit> getType() {
        return new TypeReference<>() {
            @Override
            public Type getType() {
                return super.getType();
            }
        };
    }

    @Override
    protected TypeReference<List<RateLimit>> getListType() {
        return new TypeReference<>() {
            @Override
            public Type getType() {
                return super.getType();
            }
        };
    }
}
