/*
 * Copyright 2017-2023 original authors
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

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.starter.feature.Category;
import io.micronaut.projectgen.core.feature.FeatureContext;
import jakarta.inject.Singleton;

import java.util.List;

/**
 * Feature providing support for Micronaut Data Spring JDBC integration.
 */
@Requires(property = "micronaut.starter.feature.data.spring.jdbc.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class DataSpringJdbcFeature implements OpenRewriteFeature {
    public static final String NAME = "data-spring-jdbc";

    private final DataJdbc dataJdbc;

    public DataSpringJdbcFeature(DataJdbc dataJdbc) {
        this.dataJdbc = dataJdbc;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Micronaut Data Spring JDBC";
    }

    @Override
    public String getDescription() {
        return "Adds support for Micronaut Data Spring JDBC";
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(DataJdbc.class)) {
            featureContext.addFeature(dataJdbc);
        }
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return List.of("io.micronaut.starter.feature.data-spring-jdbc");
    }

}
