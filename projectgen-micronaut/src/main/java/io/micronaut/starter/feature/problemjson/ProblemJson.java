/*
 * Copyright 2017-2021 original authors
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
package io.micronaut.starter.feature.problemjson;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.starter.feature.Category;

import jakarta.inject.Singleton;

import java.util.List;

/**
 * Feature that enables REST API error responses in the
 * application/problem+json format following the Problem Details specification.
 */
@Requires(property = "micronaut.starter.feature.problem.json.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class ProblemJson implements OpenRewriteFeature {

    @NonNull
    @Override
    public String getName() {
        return "problem-json";
    }

    @Override
    public String getTitle() {
        return "Problem JSON";
    }

    @Override
    public String getDescription() {
        return "Produce REST API error responses in application/problem+json format";
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return List.of("io.micronaut.starter.feature.problem-json");
    }

    @Override
    public String getCategory() {
        return Category.VALIDATION;
    }
}
