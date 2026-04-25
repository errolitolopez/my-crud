package com.errolito.mycrud.cache;

import com.errolito.mycrud.dto.UserResponse;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;

import java.lang.reflect.Type;
import java.util.List;

@Component
public class UserCacheStore extends CacheStore<UserResponse> {

    protected UserCacheStore(CacheService service) {
        super(service);
    }

    @Override
    protected TypeReference<UserResponse> getType() {
        return new TypeReference<>() {
            @Override
            public Type getType() {
                return super.getType();
            }
        };
    }

    @Override
    protected TypeReference<List<UserResponse>> getListType() {
        return new TypeReference<>() {
            @Override
            public Type getType() {
                return super.getType();
            }
        };
    }
}