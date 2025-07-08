package io.micronaut.starter.feature.database.r2dbc;

import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.starter.feature.database.*;

import java.util.List;

public abstract class R2dbcConfigurationUtils {

    protected void addDatabaseConfigRecipe(GeneratorContext generatorContext,
                                           List<String> recipes) {

        if (generatorContext.isFeaturePresent(PostgreSQL.class)) {
            recipes.add("io.micronaut.starter.feature.jdbc-config-postgresql");
        } else if (generatorContext.isFeaturePresent(MySQL.class)) {
            recipes.add("io.micronaut.starter.feature.jdbc-config-mysql");
        } else if (generatorContext.isFeaturePresent(MariaDB.class)) {
            recipes.add("io.micronaut.starter.feature.jdbc-config-mariadb");
        } else if (generatorContext.isFeaturePresent(SQLServer.class)) {
            recipes.add("io.micronaut.starter.feature.jdbc-config-sqlserver");
        } else if (generatorContext.isFeaturePresent(Oracle.class)) {
            recipes.add("io.micronaut.starter.feature.jdbc-config-oracle");
        } else {
            recipes.add("io.micronaut.starter.feature.jdbc-config-h2");
        }
    }
}
