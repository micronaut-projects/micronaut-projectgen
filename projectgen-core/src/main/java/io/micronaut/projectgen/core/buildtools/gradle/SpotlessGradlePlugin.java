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

import io.micronaut.projectgen.core.feature.BuildPluginFeature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.rocker.RockerTemplate;
import io.micronaut.projectgen.core.rocker.RockerWritable;
import io.micronaut.projectgen.core.template.spotlesslicensejava;
import io.micronaut.projectgen.core.template.spotlessGradlePlugin;
import jakarta.inject.Singleton;

/**
 * Spotless Gradle Plugin.
 * @see <a href="https://github.com/diffplug/spotless">Spotless</a>
 */
@Singleton
public class SpotlessGradlePlugin implements BuildPluginFeature, GradleSpecificFeature {
    private static final String SPOTLESS_GRADLE_PLUGIN_ID = "com.diffplug.spotless";
    private static final String SPOTLESS_PLUGIN_GRADLE_ARTIFACT_ID = "spotless-plugin-gradle";

    @Override
    public String getName() {
        return "spotless-gradle-plugin";
    }

    @Override
    public String getTitle() {
        return "Gradle Plugin to help you keep your code spotless";
    }

    @Override
    public String getDescription() {
        return BuildPluginFeature.super.getDescription();
    }

    @Override
    public String getThirdPartyDocumentation(GeneratorContext generatorContext) {
        return "https://github.com/diffplug/spotless";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addBuildPlugin(GradlePlugin.builder()
            .id(SPOTLESS_GRADLE_PLUGIN_ID)
            .lookupArtifactId(SPOTLESS_PLUGIN_GRADLE_ARTIFACT_ID)
            .extension(new RockerWritable(spotlessGradlePlugin.template()))
            .build());

        generatorContext.addTemplate("spotlessLicenseJava", new RockerTemplate("config/spotless.license.java", spotlesslicensejava.template()));
    }
}
