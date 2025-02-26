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
package io.micronaut.projectgen.features.gradle;

import io.micronaut.projectgen.core.buildtools.gradle.GradlePlugin;
import io.micronaut.projectgen.core.buildtools.gradle.GradleSpecificFeature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import jakarta.inject.Singleton;

@Singleton
public class KotlinGradlePlugin implements GradleSpecificFeature {
    @Override
    public String getName() {
        return "kotlin-gradle-plugin";
    }

    @Override
    public String getTitle() {
        return "Kotlin Gradle Plugin";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://plugins.gradle.org/plugin/org.jetbrains.kotlin.jvm";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public boolean supports(String applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addBuildPlugin(GradlePlugin.of("org.jetbrains.kotlin.jvm", "kotlin-gradle-plugin"));
    }
}
