package com.errolito.mycrud.config;

import com.zaxxer.hikari.HikariDataSource;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.jdbc.datasource.JdbcTelemetry;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class OpenTelemetryJdbcConfig {

    @Bean
    public DataSource dataSource(DataSourceProperties properties, OpenTelemetry openTelemetry) {

        HikariDataSource hikari = properties
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();

        return JdbcTelemetry.builder(openTelemetry)
                .build()
                .wrap(hikari);
    }
}