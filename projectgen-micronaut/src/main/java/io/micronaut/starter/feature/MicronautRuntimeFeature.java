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
package io.micronaut.starter.feature;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.generator.ModuleContext;

/**
 * Defines a feature that specifies the Micronaut runtime to be used in the project.
 * Provides a mechanism to resolve and add the Micronaut runtime property to the build configuration.
 */
public interface MicronautRuntimeFeature {

    String PROPERTY_MICRONAUT_RUNTIME = "micronaut.runtime";

    @NonNull
    String resolveMicronautRuntime(@NonNull GeneratorContext generatorContext);

    default void addMicronautRuntimeBuildProperty(@NonNull GeneratorContext generatorContext) {
        ModuleContext module = generatorContext.getRootModule();
        module.buildProperties().put(PROPERTY_MICRONAUT_RUNTIME, resolveMicronautRuntime(generatorContext));
    }
}
