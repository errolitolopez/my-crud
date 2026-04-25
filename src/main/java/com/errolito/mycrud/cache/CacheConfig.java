package com.errolito.mycrud.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CacheConfig {
    private String name;
    private boolean isCacheable = true;

    public boolean isCacheable() {
        if (name == null || name.isBlank()) {
            isCacheable = false;
        }
        return isCacheable;
    }
}