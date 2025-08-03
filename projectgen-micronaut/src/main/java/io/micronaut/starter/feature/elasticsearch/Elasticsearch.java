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
package io.micronaut.starter.feature.elasticsearch;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.graalvm.GraalVM;

import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

/**
 * Feature that adds support for integrating Elasticsearch into a Micronaut application.
 */
@Requires(property = "micronaut.starter.feature.elasticsearch.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Elasticsearch implements OpenRewriteFeature {

    @Override
    public String getName() {
        return "elasticsearch";
    }

    @Override
    public String getTitle() {
        return "Elasticsearch Driver";
    }

    @Override
    public String getDescription() {
        return "Adds support for Elasticsearch";
    }

    @Override
    public String getCategory() {
        return Category.SEARCH;
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        List<String> recipes = new ArrayList<>();
        recipes.add("io.micronaut.starter.feature.elasticsearch");
        if (generatorContext.isFeaturePresent(GraalVM.class)) {
            recipes.add("io.micronaut.starter.feature.elasticsearch-log");
        }
        return recipes;
    }

}
