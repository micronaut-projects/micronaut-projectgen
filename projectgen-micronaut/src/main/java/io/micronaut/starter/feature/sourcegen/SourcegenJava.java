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
package io.micronaut.starter.feature.sourcegen;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.starter.feature.Category;
import io.micronaut.projectgen.core.options.Language;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

/**
 * Feature for Micronaut source code generation supporting Java and Kotlin.
 * Exposes a language-neutral API for source code generation.
 */
@Requires(property = "micronaut.starter.feature.sourcegen.generator.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class SourcegenJava implements OpenRewriteFeature {

    public static final String NAME = "sourcegen-generator";

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Micronaut source code generator for Java and Kotlin";
    }

    @Override
    public String getDescription() {
        return "Micronaut SourceGen exposes a language-neutral API for source code generation";
    }

    @Override
    public String getCategory() {
        return Category.API;
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        List<String> recipes = new ArrayList<>();
        recipes.add("io.micronaut.starter.feature.sourcegen-generator");
        if (generatorContext.getLanguage() == Language.JAVA) {
            recipes.add("io.micronaut.starter.feature.sourcegen-generator-annotation-java");
        } else if (generatorContext.getLanguage() == Language.KOTLIN) {
            recipes.add("io.micronaut.starter.feature.sourcegen-generator-annotation-kotlin");
        }
        return recipes;
    }

}
