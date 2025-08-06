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
package io.micronaut.starter.feature.tracing;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.starter.feature.server.MicronautServerDependent;

import io.micronaut.projectgen.core.options.Language;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

/**
 * Adds support for distributed tracing using Jaeger.
 */
@Requires(property = "micronaut.starter.feature.tracing.jaeger.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Jaeger implements TracingFeature, MicronautServerDependent, OpenRewriteFeature {

    public static final String NAME = "tracing-jaeger";

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Jaeger Tracing";
    }

    @NonNull
    @Override
    public String getDescription() {
        return "Adds support for distributed tracing with Jaeger";
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        List<String> recipes = new ArrayList<>();
        recipes.add("io.micronaut.starter.feature.tracing-jaeger");
        if (OptionUtils.hasMavenBuildTool(generatorContext.getOptions()) && generatorContext.getLanguage() == Language.GROOVY) {
            recipes.add("io.micronaut.starter.feature.micronaut-core-processor-maven");
        }
        return recipes;
    }

}
