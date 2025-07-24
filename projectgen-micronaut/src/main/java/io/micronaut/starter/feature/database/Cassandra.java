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
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.starter.buildtools.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.config.ApplicationConfiguration;
import io.micronaut.starter.feature.micrometer.MicrometerFeature;
import io.micronaut.starter.feature.testcontainers.ContributingTestContainerDependency;
import io.micronaut.starter.feature.testresources.TestResources;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Requires(property = "micronaut.starter.feature.cassandra.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Cassandra implements OpenRewriteFeature, ContributingTestContainerDependency {

    public static final String NAME = "cassandra";
    private static final String CASSANDRA_RECIPE = "io.micronaut.starter.feature.cassandra";
    private static final String CASSANDRA_RECIPE_CONFIG_MICROMETER = "io.micronaut.starter.feature.cassandra-config-micrometer";
    private static final String CASSANDRA_RECIPE_CONFIG_TESTRESOURCES = "io.micronaut.starter.feature.cassandra-config-testresources";
    private static final String CASSANDRA_RECIPE_CONFIG = "io.micronaut.starter.feature.cassandra-config";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Cassandra Driver";
    }

    @Override
    public String getDescription() {
        return "Adds support for Cassandra persistence";
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        List<String> recipes = new ArrayList<>();
        recipes.add(CASSANDRA_RECIPE);
        if (generatorContext.isFeaturePresent(MicrometerFeature.class)) {
            recipes.add(CASSANDRA_RECIPE_CONFIG_MICROMETER);
        }
        if (generatorContext.isFeaturePresent(TestResources.class)) {
            recipes.add(CASSANDRA_RECIPE_CONFIG_TESTRESOURCES);
        } else {
            recipes.add(CASSANDRA_RECIPE_CONFIG);
        }
        return recipes;
    }

    @Override
    public List<Dependency> testContainersDependencies() {
        return Collections.singletonList(ContributingTestContainerDependency.testContainerDependency("cassandra"));
    }
}
