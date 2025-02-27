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
package io.micronaut.projectgen.micronaut.features;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.micronaut.gradle.MicronautAotGradlePlugin;
import jakarta.inject.Singleton;

/**
 * Micronaut AOT Feature.
 */
@Singleton
public class MicronautAot implements Feature {
    private final MicronautAotGradlePlugin micronautAotGradlePlugin;

    public MicronautAot(MicronautAotGradlePlugin micronautAotGradlePlugin) {
        this.micronautAotGradlePlugin = micronautAotGradlePlugin;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeatureIfNotPresent(MicronautAotGradlePlugin.class, micronautAotGradlePlugin);
    }

    @Override
    @NonNull
    public String getName() {
        return "micronaut-aot";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Build time optimizations to provide faster startup times and smaller binaries.";
    }

    @Override
    @Nullable
    public String getFrameworkDocumentation(GeneratorContext generatorContext) {
        return "https://micronaut-projects.github.io/micronaut-aot/latest/guide/";
    }

    @Override
    public String getTitle() {
        return "Micronaut AOT";
    }
}
