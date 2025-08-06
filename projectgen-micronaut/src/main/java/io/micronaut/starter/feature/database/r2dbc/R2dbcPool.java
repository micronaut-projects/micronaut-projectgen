/*
 * Copyright 2017-2025 original authors
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

import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.starter.feature.Category;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

/**
 * Feature that adds support for R2DBC connection pooling.
 */
@Singleton
public class R2dbcPool extends R2dbcConfigurationUtils implements OpenRewriteFeature, R2dbcFeature {
    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        List<String> recipes = new ArrayList<>();
        recipes.add("io.micronaut.starter.feature.r2dbc-pool");
        addDatabaseConfigRecipe(generatorContext, recipes);
        return recipes;
    }

    @Override
    public String getName() {
        return "r2dbc-pool";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }
}
