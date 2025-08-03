/*
 * Copyright 2017-2022 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.feature.database.jdbc;

import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.starter.feature.Category;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.core.feature.FeaturePhase;
import io.micronaut.projectgen.core.feature.OneOfFeature;
import io.micronaut.starter.feature.database.*;

import java.util.List;

/**
 * Abstract base feature for JDBC-related functionality.
 * <p>
 * This class provides common configuration keys and behavior for
 * JDBC connection setup, including default database driver selection,
 * configuration property keys, and integration with supported database types.
 * </p>
 */
public abstract class JdbcFeature implements OneOfFeature, DatabaseDriverConfigurationFeature {

    private static final String PREFIX = "datasources.default.";
    public static final String PROPERTY_DATASOURCES_DEFAULT_DB_TYPE = PREFIX + "db-type";
    private static final String URL_KEY = PREFIX + "url";
    private static final String DRIVER_KEY = PREFIX + "driver-class-name";
    private static final String USERNAME_KEY = PREFIX + "username";
    private static final String PASSWORD_KEY = PREFIX + "password";

    private final DatabaseDriverFeature defaultDbFeature;

    protected JdbcFeature(DatabaseDriverFeature defaultDbFeature) {
        this.defaultDbFeature = defaultDbFeature;
    }

    @Override
    public int getOrder() {
        return FeaturePhase.LOW.getOrder();
    }

    @Override
    public Class<?> getFeatureClass() {
        return JdbcFeature.class;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(DatabaseDriverFeature.class)) {
            featureContext.addFeature(defaultDbFeature);
        }
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }

    @Override
    public String getUrlKey() {
        return URL_KEY;
    }

    @Override
    public String getDriverKey() {
        return DRIVER_KEY;
    }

    @Override
    public String getUsernameKey() {
        return USERNAME_KEY;
    }

    @Override
    public String getPasswordKey() {
        return PASSWORD_KEY;
    }

    protected final void addDatabaseConfigRecipe(GeneratorContext generatorContext,
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
