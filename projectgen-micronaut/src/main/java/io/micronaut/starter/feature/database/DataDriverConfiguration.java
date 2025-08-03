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
package io.micronaut.starter.feature.database;

import io.micronaut.projectgen.core.generator.GeneratorContext;

import java.util.List;

/**
 * Abstract base class for configuring database-specific recipes
 * used in Micronaut Data features.
 */
public abstract class DataDriverConfiguration {

    /**
     * Adds a database configuration recipe to the given list based on the features present in the GeneratorContext.
     * The recipe added corresponds to the database type detected from the features.
     * If no specific database feature is present, it defaults to H2 database configuration.
     *
     * @param generatorContext the GeneratorContext to check for database features
     * @param recipes the list to which the database configuration recipe will be added
     */
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
