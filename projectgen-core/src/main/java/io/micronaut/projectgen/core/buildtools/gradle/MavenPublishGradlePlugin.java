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
import io.micronaut.projectgen.core.buildtools.BuildProperties;
import io.micronaut.projectgen.core.feature.BuildPluginFeature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.rocker.RockerWritable;
import jakarta.inject.Singleton;
import io.micronaut.projectgen.core.template.mavenPublishGradlePlugin;
import java.time.LocalDate;

/**
 * @see <a href="https://docs.gradle.org/current/userguide/publishing_maven.html">Maven Publish Plugin</a>
 */
@Singleton
public class MavenPublishGradlePlugin implements BuildPluginFeature, GradleSpecificFeature {
    public static final BuildPlugin MAVEN_PUBLISH_GRADLE_PLUGIN = GradlePlugin.builder()
        .id("io.micronaut.library.maven-publish")
        .extension(new RockerWritable(mavenPublishGradlePlugin.template()))
        .build();

    @Override
    public String getName() {
        return "maven-publish-gradle-plugin";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addBuildPlugin(MAVEN_PUBLISH_GRADLE_PLUGIN);
        populateBuildProperties(generatorContext);
    }

    private static void populateBuildProperties(GeneratorContext generatorContext) {
        BuildProperties props = generatorContext.getBuildProperties();
        props.put("projectVersion", "0.0.1-SNAPSHOT");
        props.put("inceptionYear", "" + LocalDate.now().getYear());
        props.put("projectName", "TODO");
        props.put("projectDesc", "TODO");
        props.put("projectUrl", "TODO");
        props.put("githubOrg", "TODO");
        props.put("githubRepo", "TODO");
        props.put("organizationName", "TODO");
        props.put("organizationWebsite", "TODO");
        props.put("developerId", "TODO");
        props.put("developerName", "TODO");
        props.put("developerEmail", "TODO");
    }
}
