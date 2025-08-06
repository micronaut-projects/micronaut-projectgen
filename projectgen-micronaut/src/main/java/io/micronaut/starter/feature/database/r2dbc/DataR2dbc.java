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
package io.micronaut.starter.feature.database.r2dbc;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.starter.buildtools.dependencies.MicronautDependencyUtils;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.starter.feature.database.Data;
import io.micronaut.starter.feature.database.DataFeature;
import io.micronaut.starter.feature.database.TransactionalNotSupported;
import io.micronaut.starter.feature.migration.MigrationFeature;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

/**
 * Feature that adds Micronaut Data support for Reactive Database Connectivity (R2DBC).
 * This feature ensures that Micronaut Data and R2DBC features are included,
 * providing reactive database access with Micronaut Data abstractions.
 */
@Requires(property = "micronaut.starter.feature.data.r2dbc.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class DataR2dbc extends R2dbcConfigurationUtils implements R2dbcFeature, DataFeature, TransactionalNotSupported, OpenRewriteFeature {

    public static final String NAME = "data-r2dbc";

    private static final Dependency DEPENDENCY_MICRONAUT_DATA_R2DBC = MicronautDependencyUtils.dataDependency()
        .artifactId("micronaut-data-r2dbc")
        .compile()
        .build();

    private final Data data;
    private final R2dbc r2dbc;

    public DataR2dbc(Data data, R2dbc r2dbc) {
        this.data = data;
        this.r2dbc = r2dbc;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeature(data);
        if (!featureContext.isPresent(R2dbc.class)) {
            featureContext.addFeature(r2dbc);
        }
    }

    @Override
    public int getOrder() {
        return r2dbc.getOrder() - 1;
    }

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Micronaut Data R2DBC";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Micronaut Data support for Reactive Database Connectivity (R2DBC)";
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        List<String> recipes = new ArrayList<>();
        recipes.add("io.micronaut.starter.feature.data-r2dbc");
        if (!generatorContext.isFeaturePresent(MigrationFeature.class)) {
            recipes.add("io.micronaut.starter.feature.data-r2dbc.conf");
        }
        addDatabaseConfigRecipe(generatorContext, recipes);
        return recipes;
    }

}
