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
package io.micronaut.starter.feature.spring;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.options.Language;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

/**
 * Feature that adds support for using Spring Boot Annotations in Micronaut applications.
 */
@Requires(property = "micronaut.starter.feature.spring.boot.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class SpringBoot extends SpringFeature implements OpenRewriteFeature {

    public static final String NAME = "spring-boot";

    public SpringBoot(Spring spring) {
        super(spring);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Spring Boot Annotations";
    }

    @Override
    public String getDescription() {
        return "Adds support for using Spring Boot Annotations";
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        List<String> recipes = new ArrayList<>();
        recipes.add("io.micronaut.starter.feature.spring-boot");
        if (OptionUtils.hasMavenBuildTool(generatorContext.getOptions()) && generatorContext.getLanguage() == Language.GROOVY) {
            recipes.add("io.micronaut.starter.feature.micronaut-spring-boot-maven");
        } else {
            recipes.add("io.micronaut.starter.feature.micronaut-spring-boot");
        }
        return recipes;
    }

}
