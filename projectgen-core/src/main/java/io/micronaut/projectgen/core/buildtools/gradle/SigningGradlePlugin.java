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
package io.micronaut.projectgen.core.buildtools.gradle;

import io.micronaut.projectgen.core.buildtools.BuildPlugin;
import io.micronaut.projectgen.core.feature.BuildPluginFeature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.rocker.RockerWritable;
import jakarta.inject.Singleton;
import io.micronaut.projectgen.core.template.signingGradlePlugin;

/**
 * Signing Gradle Plugin.
 * <a href="https://docs.gradle.org/current/userguide/signing_plugin.html">Signing Gradle Plugin</a>
 */
@Singleton
public class SigningGradlePlugin implements BuildPluginFeature, GradleSpecificFeature {
    public static final BuildPlugin SIGNING_GRADLE_PLUGIN = GradlePlugin.builder()
        .id("signing")
        .extension(new RockerWritable(signingGradlePlugin.template()))
        .build();

    @Override
    public String getName() {
        return "signing-gradle-plugin";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addBuildPlugin(SIGNING_GRADLE_PLUGIN);
    }
}
