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
package io.micronaut.starter.feature.jdbi;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.starter.feature.Category;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.starter.feature.database.jdbc.JdbcFeature;

import jakarta.inject.Singleton;

import java.util.List;

@Requires(property = "micronaut.starter.feature.sql.jdbi.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class JdbiFeature implements OpenRewriteFeature {

    private final JdbcFeature jdbcFeature;

    public JdbiFeature(JdbcFeature jdbcFeature) {
        this.jdbcFeature = jdbcFeature;
    }

    @NonNull
    @Override
    public String getName() {
        return "sql-jdbi";
    }

    @Override
    public String getTitle() {
        return "Jdbi";
    }

    @Override
    public String getDescription() {
        return "Adds support for persistence with Jdbi";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(JdbcFeature.class)) {
            featureContext.addFeature(jdbcFeature);
        }
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return List.of("io.micronaut.starter.feature.sql-jdbi");
    }

}
