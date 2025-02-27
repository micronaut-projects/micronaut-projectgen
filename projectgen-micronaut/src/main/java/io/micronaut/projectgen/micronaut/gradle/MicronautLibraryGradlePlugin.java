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
package io.micronaut.projectgen.micronaut.gradle;

import io.micronaut.projectgen.core.buildtools.gradle.GradlePlugin;
import io.micronaut.projectgen.core.buildtools.gradle.GradleSpecificFeature;
import io.micronaut.projectgen.core.feature.BuildPluginFeature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import jakarta.inject.Singleton;

/**
 * Micronaut AOT Gradle Plugin.
 */
@Singleton
public class MicronautLibraryGradlePlugin implements BuildPluginFeature, GradleSpecificFeature {
    private static final String MICRONAUT_LIBRARY_GRADLE_PLUGIN_ID = "io.micronaut.library";
    private static final int GRADLE_PLUGIN_ORDER = 10;

    @Override
    public String getName() {
        return "micronaut-library-gradle-plugin";
    }

    @Override
    public String getTitle() {
        return "Micronaut Library Gradle Plugin";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addBuildPlugin(GradlePlugin.builder()
            .id(MICRONAUT_LIBRARY_GRADLE_PLUGIN_ID)
            .lookupArtifactId(MicronautApplicationGradlePlugin.Builder.ARTIFACT_ID)
            .order(GRADLE_PLUGIN_ORDER)
            .build());
    }
}
