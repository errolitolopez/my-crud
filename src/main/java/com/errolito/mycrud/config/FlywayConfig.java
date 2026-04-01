package com.errolito.mycrud.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.flyway.autoconfigure.FlywayMigrationStrategy;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile({"dev"})
@Configuration
public class FlywayConfig implements FlywayMigrationStrategy {

    @Override
    public void migrate(Flyway flyway) {
        flyway.clean();
        flyway.migrate();
    }
}