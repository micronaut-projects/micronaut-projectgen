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
 * Spring Boot Gradle Plugin.
 */
@Requires(property = "micronaut.projectgen.springboot.features.springboot.gradle.plugin.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class SpringBootGradlePlugin implements GradleSpecificFeature {
    private static final String ARTIFACT_ID = "spring-boot-gradle-plugin";
    private static final String GRADLE_PLUGIN_ID_SPRINGFRAMEWORK_BOOT = "org.springframework.boot";

    @Override
    public String getName() {
        return "springboot-gradle-plugin";
    }

    @Override
    public String getTitle() {
        return "Spring Boot Gradle Plugin";
    }

    @Override
    public String getDescription() {
        return "Adds the Spring Boot Gradle Plugin provides Spring Boot support in Gradle.";
    }

    @Override
    public boolean supports(String applicationType) {
        return true;
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/htmlsingle/";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (OptionUtils.hasGradleBuildTool(generatorContext.getOptions())) {
            generatorContext.addBuildPlugin(GradlePlugin.builder()
                .id(GRADLE_PLUGIN_ID_SPRINGFRAMEWORK_BOOT)
                .lookupArtifactId(ARTIFACT_ID)
                .build());
        }
    }
}
