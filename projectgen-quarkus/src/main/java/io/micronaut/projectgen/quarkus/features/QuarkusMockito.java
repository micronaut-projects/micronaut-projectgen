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
package io.micronaut.projectgen.quarkus.features;

import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import jakarta.inject.Singleton;

@Singleton
public class QuarkusMockito implements QuarkusFeature {

    private static final Dependency DEPENDENCY_QUARKUS_JUNIT_MOCKITO =
        Dependency.builder()
            .groupId("io.quarkus")
            .artifactId("quarkus-junit5-mockito")
            .test()
            .build();

    @Override
    public String getTitle() {
        return "Quarkus JUnit 5 Mockito";
    }

    @Override
    public String getName() {
        return "quarkus-junit5-mockito";
    }

    @Override
    public String getDescription() {
        return "Utilities, classes, and annotations that make Mockito easier to use within Quarkus";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_QUARKUS_JUNIT_MOCKITO);
    }
}
