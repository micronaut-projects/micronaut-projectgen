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
package io.micronaut.starter.feature.migration;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.starter.feature.database.MariaDB;
import io.micronaut.starter.feature.database.MySQL;
import io.micronaut.starter.feature.database.Oracle;
import io.micronaut.starter.feature.database.PostgreSQL;
import io.micronaut.starter.feature.database.SQLServer;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

@Requires(property = "micronaut.starter.feature.flyway.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Flyway implements MigrationFeature, OpenRewriteFeature {

    public static final String NAME = "flyway";

    //https://documentation.red-gate.com/fd/oracle-184127602.html

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Flyway Database Migration";
    }

    @Override
    public String getDescription() {
        return "Adds support for Flyway database migrations";
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        List<String> recipes = new ArrayList<>();
        recipes.add("io.micronaut.starter.feature.flyway");
        if (generatorContext.isFeaturePresent(MySQL.class) || generatorContext.isFeaturePresent(MariaDB.class)) {
            recipes.add("io.micronaut.starter.feature.flyway-mysql");
        }
        if (generatorContext.isFeaturePresent(SQLServer.class)) {
            recipes.add("io.micronaut.starter.feature.flyway-sqlserver");
        }
        if (generatorContext.isFeaturePresent(Oracle.class)) {
            recipes.add("io.micronaut.starter.feature.flyway-database-oracle");
        }
        if (generatorContext.isFeaturePresent(PostgreSQL.class)) {
            recipes.add("io.micronaut.starter.feature.flyway-database-postgresql");
        }
        return recipes;
    }
}
