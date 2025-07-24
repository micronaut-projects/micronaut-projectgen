package io.micronaut.starter.feature.database;

import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.starter.feature.database.*;

import java.util.List;

public abstract class DataDriverConfiguration {

    protected void addDatabaseConfigRecipe(GeneratorContext generatorContext, List<String> recipes) {
        if (generatorContext.isFeaturePresent(PostgreSQL.class)) {
            recipes.add("io.micronaut.starter.feature.data-config-postgresql");
        } else if (generatorContext.isFeaturePresent(MySQL.class)) {
            recipes.add("io.micronaut.starter.feature.data-config-mysql");
        } else if (generatorContext.isFeaturePresent(MariaDB.class)) {
            recipes.add("io.micronaut.starter.feature.data-config-mariadb");
        } else if (generatorContext.isFeaturePresent(SQLServer.class)) {
            recipes.add("io.micronaut.starter.feature.data-config-sqlserver");
        } else if (generatorContext.isFeaturePresent(Oracle.class)) {
            recipes.add("io.micronaut.starter.feature.data-config-oracle");
        } else {
            recipes.add("io.micronaut.starter.feature.data-config-h2");
        }
    }
}
