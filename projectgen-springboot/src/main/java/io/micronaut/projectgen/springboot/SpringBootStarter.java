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
package io.micronaut.projectgen.springboot;

import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.options.Options;
import jakarta.inject.Singleton;

/**
 * Spring Boot starter.
 */
@Singleton
public class SpringBootStarter implements Feature {
    private static final String NAME = "spring-boot-starter";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Spring Boot Starter";
    }

    @Override
    public String getDescription() {
        return "Adds Spring Boot Starter dependencies";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(SpringBootDependencies.DEPENDENCY_SPRINGBOOT_STARTER);
        generatorContext.addDependency(SpringBootDependencies.DEPENDENCY_SPRINGBOOT_STARTER_TEST);
    }

    @Override
    public boolean supports(Options options) {
        return true;
    }
}
