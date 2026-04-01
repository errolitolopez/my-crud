package com.errolito.mycrud.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.uncaughterrol.commons.model.ApiResponse;
import io.github.uncaughterrol.commons.model.InvalidParam;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.MapperFeature;

import java.util.Collection;

@Configuration
public class JacksonConfig {

    @Bean
    public JsonMapperBuilderCustomizer jacksonCustomizer() {
        return builder -> {
            builder.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, false);
            builder.addMixIn(ApiResponse.class, ApiResponseMixIn.class);
        };
    }

    public static abstract class ApiResponseMixIn<T> {
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private T data;

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private Collection<InvalidParam> invalidParams;
    }
}