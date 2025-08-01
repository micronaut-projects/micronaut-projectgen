/*
 * Copyright 2017-2023 original authors
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
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.test.JunitCompanionFeature;
import jakarta.inject.Singleton;

import java.util.List;

/**
 * Feature that provides JUnit Platform Suite Engine support for Micronaut applications.
 * This feature adds the JUnit Platform Suite Engine dependency for declarative test suites.
 */
@Requires(property = "micronaut.starter.feature.junit.platform.suite.engine.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class JunitPlatformSuiteEngine implements JunitCompanionFeature, OpenRewriteFeature {

    public static final String NAME = "junit-platform-suite-engine";

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "JUnit Platform Suite Engine";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds the junit-platform-suite-engine dependency, an implementation of the TestEngine API for declarative test suites.";
    }

    @Override
    public String getCategory() {
        return Category.TEST;
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return List.of("io.micronaut.starter.feature.junit-platform-suite-engine");
    }
}
