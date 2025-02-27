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

import io.micronaut.projectgen.core.buildtools.BuildPlugin;
import io.micronaut.projectgen.core.buildtools.dependencies.Coordinate;
import io.micronaut.projectgen.core.buildtools.dependencies.CoordinateResolver;
import io.micronaut.projectgen.core.buildtools.gradle.GradleSpecificFeature;
import io.micronaut.projectgen.core.feature.BuildPluginFeature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.options.TestFramework;
import io.micronaut.projectgen.core.utils.OptionUtils;
import jakarta.inject.Singleton;

import static io.micronaut.projectgen.micronaut.maven.MicronautParentPomFeature.ARTIFACT_ID_MICRONAUT_PARENT;

/**
 * Micronaut Application Gradle Plugin.
 */
@Singleton
public class MicronautApplicationGradlePluginFeature implements BuildPluginFeature, GradleSpecificFeature {
    private static final String TEST_RUNTIME_JUNIT_5 = "junit5";
    private static final String PROPERTY_MICRONAUT_VERSION = "micronautVersion";
    private final CoordinateResolver coordinateResolver;

    public MicronautApplicationGradlePluginFeature(CoordinateResolver coordinateResolver) {
        this.coordinateResolver = coordinateResolver;
    }

    @Override
    public String getName() {
        return "micronaut-application-gradle-plugin";
    }

    @Override
    public String getTitle() {
        return "Micronaut Application Gradle Plugin";
    }

    @Override
    public boolean supports(Options options) {
        return OptionUtils.hasGradleBuildTool(options);
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        Coordinate coordinate = coordinateResolver.resolve(ARTIFACT_ID_MICRONAUT_PARENT).orElseThrow();
        generatorContext.getBuildProperties().put(PROPERTY_MICRONAUT_VERSION, coordinate.getVersion());
        generatorContext.addBuildPlugin(micronautApplicationGradlePlugin(generatorContext));
    }

    private BuildPlugin micronautApplicationGradlePlugin(GeneratorContext generatorContext) {
        MicronautApplicationGradlePlugin.Builder builder = MicronautApplicationGradlePlugin.builder()
            .incremental(true)
            .id(MicronautApplicationGradlePlugin.Builder.APPLICATION)
            .packageName(generatorContext.getOptions().packageName());
        if (generatorContext.getOptions().testFramework() == TestFramework.JUNIT) {
            builder.testRuntime(TEST_RUNTIME_JUNIT_5);
        }
        return builder.build();

    }
}
