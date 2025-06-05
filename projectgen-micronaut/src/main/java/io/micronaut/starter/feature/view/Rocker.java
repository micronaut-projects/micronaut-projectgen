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
package io.micronaut.starter.feature.view;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Coordinate;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.core.rocker.RockerWritable;
import io.micronaut.projectgen.micronaut.template.view.gradlePluginRocker;
import io.micronaut.projectgen.micronaut.template.view.mvnPluginRocker;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.projectgen.core.buildtools.gradle.GradlePlugin;
import io.micronaut.projectgen.core.buildtools.maven.MavenPlugin;
import io.micronaut.starter.feature.server.MicronautServerDependent;
import jakarta.inject.Singleton;

import java.util.List;

@Requires(property = "micronaut.starter.feature.views.rocker.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Rocker implements ViewFeature, MicronautServerDependent, OpenRewriteFeature {

    @Override
    public String getName() {
        return "views-rocker";
    }

    @Override
    public String getTitle() {
        return "Rocker Views";
    }

    @Override
    public String getDescription() {
        return "Adds support for Server-Side View Rendering using Rocker";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        ModuleContext module = generatorContext.getRootModule();
        OpenRewriteFeature.super.apply(generatorContext);
        module.addBuildPlugin(GradlePlugin.builder()
                .id("nu.studer.rocker")
                .extension(new RockerWritable(gradlePluginRocker.template(rockerSrcDir(module))))
                .lookupArtifactId("gradle-rocker-plugin")
                .build());
        String mavenPluginArtifactId = "rocker-maven-plugin";
        Coordinate coordinate = generatorContext.resolveCoordinate(mavenPluginArtifactId);
        module.addBuildPlugin(MavenPlugin.builder()
                .artifactId(mavenPluginArtifactId)
                .extension(new RockerWritable(mvnPluginRocker.template(coordinate.getGroupId(),
                        coordinate.getArtifactId(),
                        coordinate.getVersion(),
                        rockerSrcDir(module))))
                .build());
    }

    private String rockerSrcDir(ModuleContext module) {
        String path = module.configuration().getPath();
        if (path.endsWith("/")) {
            path = path.substring(0, path.lastIndexOf('/'));
        }
        return path;
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return List.of("io.micronaut.starter.feature.views-rocker");
    }

}
