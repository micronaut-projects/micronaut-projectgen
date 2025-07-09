/*
 * Copyright 2017-2024 original authors
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
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.starter.buildtools.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.starter.feature.database.DatabaseDriverFeature;
import io.micronaut.starter.feature.database.jdbc.Hikari;
import io.micronaut.starter.feature.database.jdbc.JdbcFeature;
import io.micronaut.starter.feature.migration.MigrationFeature;
import io.micronaut.starter.feature.testresources.TestResources;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Requires(property = "micronaut.starter.feature.r2dbc.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class R2dbc extends R2dbcConfigurationUtils implements R2dbcFeature, OpenRewriteFeature {

    private static final String RECIPE_R2DBC_DOCS = "io.micronaut.starter.feature.r2dbc-docs";
    private static final String RECIPE_R2DBC_DEPENDENCY = "io.micronaut.starter.feature.r2dbc-dependency";

    public static final String NAME = "r2dbc";

    private static final String PREFIX = "r2dbc.datasources.default.";
    private static final String URL_KEY = PREFIX + "url";

    private final DatabaseDriverFeature defaultDbFeature;
    private final Hikari hikari;

    public R2dbc(DatabaseDriverFeature defaultDbFeature, Hikari hikari) {
        this.defaultDbFeature = defaultDbFeature;
        this.hikari = hikari;
    }

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "R2DBC";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "R2DBC - Reactive Database Connectivity";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(DatabaseDriverFeature.class)) {
            featureContext.addFeature(defaultDbFeature);
        }
        if (featureContext.isPresent(MigrationFeature.class) && !featureContext.isPresent(JdbcFeature.class)) {
            featureContext.addFeature(hikari);
        }
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        List<String> recipes = new ArrayList<>();
        recipes.add(RECIPE_R2DBC_DOCS);
        if (!generatorContext.isFeaturePresent(DataR2dbc.class)) {
            recipes.add(RECIPE_R2DBC_DEPENDENCY);
        }
        addDatabaseConfigRecipe(generatorContext, recipes);
        return recipes;
    }

    public String getUrlKey() {
        return URL_KEY;
    }
}
