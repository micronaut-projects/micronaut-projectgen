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
package io.micronaut.projectgen.features.gradle;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.buildtools.gradle.GradlePlugin;
import io.micronaut.projectgen.core.buildtools.gradle.GradleSpecificFeature;
import io.micronaut.projectgen.core.feature.BuildPluginFeature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.utils.OptionUtils;
import jakarta.inject.Singleton;

/**
 * Adds a shaded JAR feature.
 */
@Requires(property = "micronaut.starter.feature.shade.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class ShadePlugin implements BuildPluginFeature, GradleSpecificFeature {

    @NonNull
    @Override
    public String getName() {
        return "shade";
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public boolean supports(Options applicationType) {
        return true;
    }

    @Override
    public String getTitle() {
        return "Fat/Shaded JAR Support";
    }

    @Override
    public String getDescription() {
        return "Adds the ability to build a Fat/Shaded JAR";
    }

    @Override
    public String getCategory() {
        return "Packaging";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (OptionUtils.hasGradleBuildTool(generatorContext.getOptions())) {
            GradlePlugin.Builder builder = GradlePlugin.builder()
                .id("com.github.johnrengelman.shadow")
                .lookupArtifactId("shadow");
            generatorContext.addBuildPlugin(builder.build());
        }
    }

    @Override
    public String getThirdPartyDocumentation(GeneratorContext generatorContext) {
        return "https://plugins.gradle.org/plugin/com.github.johnrengelman.shadow";
    }
}
