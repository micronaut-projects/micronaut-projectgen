/*
 * Copyright 2017-2020 original authors
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
package io.micronaut.projectgen.micronaut.features.test;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.starter.feature.Category;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

@Requires(property = "micronaut.starter.feature.awaitility.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Awaitility implements OpenRewriteFeature {

    @Override
    @NonNull
    public String getName() {
        return "awaitility";
    }

    @Override
    public String getTitle() {
        return "Awaitility Framework";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Awaitility is a framework for testing asynchronous code";
    }

    @Override
    public String getCategory() {
        return Category.DEV_TOOLS;
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        List<String> recipes = new ArrayList<>();
        switch (generatorContext.getOptions().language()) {
            case JAVA -> recipes.add("io.micronaut.starter.feature.awaitility.dependencies.java");
            case KOTLIN -> recipes.add("io.micronaut.starter.feature.awaitility.dependencies.kotlin");
            case GROOVY -> recipes.add("io.micronaut.starter.feature.awaitility.dependencies.groovy");
        }
        recipes.add("io.micronaut.starter.feature.awaitility.documentation.thirdparty");
        return recipes;
    }
}
