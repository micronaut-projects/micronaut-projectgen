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
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.starter.buildtools.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.starter.feature.server.MicronautServerDependent;
import io.micronaut.projectgen.core.options.Language;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

import static io.micronaut.starter.buildtools.dependencies.MicronautDependencyUtils.GROUP_ID_IO_MICRONAUT_OPENAPI;

@Requires(property = "micronaut.starter.feature.openapi.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class OpenApi implements OpenRewriteFeature, MicronautServerDependent {

    @Override
    @NonNull
    public String getName() {
        return "openapi";
    }

    @Override
    public String getTitle() {
        return "OpenAPI Support";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds support for OpenAPI (Swagger)";
    }

    @Override
    public boolean supports(Options options) {
        ApplicationType type = ApplicationType.of(options.template());
        return type == ApplicationType.DEFAULT;
    }

    @Override
    public String getCategory() {
        return Category.API;
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        List<String> recipes = new ArrayList<>();
        recipes.add("io.micronaut.starter.feature.openapi");
        if (OptionUtils.hasMavenBuildTool(generatorContext.getOptions()) && generatorContext.getLanguage() == Language.GROOVY) {
            recipes.add("io.micronaut.starter.feature.openapi-maven");
        }
            return recipes;
    }

}
