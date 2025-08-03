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
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.starter.feature.Category;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.starter.feature.MinJdkFeature;
import io.micronaut.starter.feature.database.jdbc.JdbcFeature;

import io.micronaut.projectgen.core.options.JdkVersion;
import jakarta.inject.Singleton;

import java.util.List;

/**
 * Feature that adds support for jOOQ, a fluent API for typesafe SQL query construction and execution.
 * <p>
 * Requires at least JDK 11.
 * </p>
 */
@Requires(property = "micronaut.starter.feature.jooq.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Jooq implements OpenRewriteFeature, MinJdkFeature {

    private final JdbcFeature jdbcFeature;

    public Jooq(JdbcFeature jdbcFeature) {
        this.jdbcFeature = jdbcFeature;
    }

    @Override
    public String getName() {
        return "jooq";
    }

    @Override
    public String getTitle() {
        return "jOOQ";
    }

    @Override
    public String getDescription() {
        return "Use the jOOQ fluent API for typesafe SQL query construction and execution";
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
    @NonNull
    public JdkVersion minJdk() {
        return JdkVersion.JDK_11;
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return List.of("io.micronaut.starter.feature.jooq");
    }

}
