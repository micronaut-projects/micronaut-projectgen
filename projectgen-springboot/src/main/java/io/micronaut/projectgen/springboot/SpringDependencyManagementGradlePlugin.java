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
package io.micronaut.projectgen.springboot;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.buildtools.gradle.GradlePlugin;
import io.micronaut.projectgen.core.buildtools.gradle.GradleSpecificFeature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.utils.OptionUtils;
import jakarta.inject.Singleton;

/**
 * Spring Dependency Management Gradle Plugin.
 */
@Requires(property = "micronaut.starter.feature.spring.dependency.management.gradle.plugin.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class SpringDependencyManagementGradlePlugin implements GradleSpecificFeature {
    private static final String ARTIFACT_ID = "dependency-management-plugin";
    private static final String GRADLE_PLUGIN_ID_SPRING_DEPENDENCY_MANAGEMENT = "io.spring.dependency-management";

    @Override
    public String getName() {
        return "spring-dependency-management-gradle-plugin";
    }

    @Override
    public String getTitle() {
        return "Spring Dependency Management Plugin Gradle Plugin";
    }

    @Override
    public String getDescription() {
        return "Adds the Spring Dependency Management Plugin Gradle Plugin that provides Maven-like dependency management and exclusions.";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.spring.io/dependency-management-plugin/docs/current-SNAPSHOT/reference/html/";
    }

    @Override
    public boolean supports(String applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (OptionUtils.hasGradleBuildTool(generatorContext.getOptions())) {
            generatorContext.addBuildPlugin(GradlePlugin.builder()
                    .id(GRADLE_PLUGIN_ID_SPRING_DEPENDENCY_MANAGEMENT)
                    .lookupArtifactId(ARTIFACT_ID)
                    .build());
        }
    }
}
