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
package io.micronaut.starter.feature.other;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.starter.feature.Category;
import io.micronaut.projectgen.core.feature.LanguageSpecificFeature;
import io.micronaut.projectgen.core.options.Language;
import jakarta.inject.Singleton;

import java.util.List;

/**
 * Provides support for Project Lombok in Java projects.
 *
 * This feature integrates Lombok annotations to reduce boilerplate code.
 * It requires Java as the language.
 */
@Requires(property = "micronaut.starter.feature.lombok.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class ProjectLombok implements LanguageSpecificFeature, OpenRewriteFeature {

    @Override
    public String getName() {
        return "lombok";
    }

    @Override
    public String getTitle() {
        return "Project Lombok";
    }

    @Override
    public String getDescription() {
        return "Adds support for Project Lombok";
    }

    @Override
    public String getCategory() {
        return Category.DEV_TOOLS;
    }

    @Override
    public Language getRequiredLanguage() {
        return Language.JAVA;
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return List.of("io.micronaut.starter.feature.lombok");
    }

}
