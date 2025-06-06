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
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Coordinate;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.core.rocker.RockerWritable;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.micronaut.template.view.exampleJTE;
import io.micronaut.projectgen.micronaut.template.view.gradlePluginJTE;
import io.micronaut.projectgen.micronaut.template.view.mvnPluginJTE;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.projectgen.core.buildtools.gradle.GradlePlugin;
import io.micronaut.projectgen.core.buildtools.gradle.GradleDsl;

import java.util.List;
import java.util.Optional;
import io.micronaut.projectgen.core.buildtools.maven.MavenPlugin;
import io.micronaut.projectgen.core.buildtools.BuildPlugin;
import io.micronaut.starter.feature.build.Kapt;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.starter.feature.server.MicronautServerDependent;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.rocker.RockerTemplate;
import jakarta.inject.Singleton;

@Requires(property = "micronaut.starter.feature.views.jte.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class JTE implements ViewFeature, MicronautServerDependent, OpenRewriteFeature {

    private static final String MAVEN_PLUGIN_ARTIFACT_ID = "jte-maven-plugin";
    private static final String JTE_SRC_DIR = "src/main/jte";

    @Override
    @NonNull
    public String getName() {
        return "views-jte";
    }

    @Override
    public String getTitle() {
        return "JTE Views";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds support for Server-Side View Rendering using JTE";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        OpenRewriteFeature.super.apply(generatorContext);
        ModuleContext module = generatorContext.getRootModule();
        if (OptionUtils.hasGradleBuildTool(generatorContext.getOptions())) {
            module.addBuildPlugin(gradlePlugin(generatorContext));
        } else if (OptionUtils.hasMavenBuildTool(generatorContext.getOptions())) {
            module.addBuildPlugin(mavenPlugin(generatorContext));
        }
        module.addTemplate("exampleJte", new RockerTemplate(JTE_SRC_DIR + "/example.jte", exampleJTE.template()));
    }

    private BuildPlugin gradlePlugin(GeneratorContext generatorContext) {
        boolean patchKapt = OptionUtils.hasGradleBuildTool(generatorContext.getOptions())
                && generatorContext.getLanguage() == Language.KOTLIN
                && generatorContext.hasFeature(Kapt.class);

        GradlePlugin.Builder builder = GradlePlugin.builder()
                .id("gg.jte.gradle")
                .extension(new RockerWritable(gradlePluginJTE.template(patchKapt, JTE_SRC_DIR)))
                .lookupArtifactId("jte-gradle-plugin");
        return builder.build();
    }

    private BuildPlugin mavenPlugin(GeneratorContext generatorContext) {
        Coordinate coordinate = generatorContext.resolveCoordinate(MAVEN_PLUGIN_ARTIFACT_ID);
        return MavenPlugin.builder()
                .artifactId(MAVEN_PLUGIN_ARTIFACT_ID)
                .extension(new RockerWritable(mvnPluginJTE.template(coordinate.getGroupId(),
                        coordinate.getArtifactId(),
                        coordinate.getVersion(),
                        JTE_SRC_DIR)))
                .build();
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return List.of("io.micronaut.starter.feature.views-jte");
    }
}
