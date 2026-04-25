package com.errolito.mycrud.cache;

import org.springframework.data.domain.Pageable;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

public final class CacheKeyUtils {

    public CacheKeyUtils() {
    }

    public static String pageCacheKey(Object query, Pageable pageable) {
        Field[] fields = query.getClass().getDeclaredFields();

        String queryParams = Arrays.stream(fields)
                .sorted(Comparator.comparing(Field::getName))
                .peek(f -> f.setAccessible(true))
                .map(f -> {
                    try {
                        return f.getName() + "=" + f.get(query);
                    } catch (IllegalAccessException e) {
                        return "";
                    }
                })
                .collect(Collectors.joining("&"));

        return queryParams
                + "&pageNumber=" + pageable.getPageNumber()
                + "&pageSize=" + pageable.getPageSize()
                + "&pageSort=" + pageable.getSort();
    }
}