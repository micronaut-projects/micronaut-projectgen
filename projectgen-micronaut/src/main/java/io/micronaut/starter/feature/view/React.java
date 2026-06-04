/*
 * Copyright 2017-2024 original authors
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
import io.micronaut.projectgen.build.dependencies.StarterCoordinates;
import io.micronaut.projectgen.core.buildtools.dependencies.Coordinate;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.core.rocker.RockerWritable;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.micronaut.template.view.mvnPluginReact;
import io.micronaut.projectgen.micronaut.template.view.reactControllerJava;
import io.micronaut.projectgen.micronaut.template.view.reactControllerKotlin;
import io.micronaut.projectgen.core.buildtools.gradle.GradleFile;
import io.micronaut.projectgen.core.buildtools.gradle.GradlePlugin;
import io.micronaut.projectgen.core.buildtools.maven.MavenPlugin;
import io.micronaut.starter.feature.server.MicronautServerDependent;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.rocker.RockerTemplate;
import io.micronaut.projectgen.core.template.URLTemplate;
import jakarta.inject.Singleton;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Adds support for server-side rendering of ReactJS components using GraalJS.
 * Configures necessary build plugins and frontend resources for Gradle and Maven.
 */

@Requires(property = "micronaut.starter.feature.views.react.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class React implements ViewFeature, MicronautServerDependent, OpenRewriteFeature {
    public static final String NODE_GRADLE_PLUGIN_VERSION = "7.0.2";
    private static final String ARTIFACT_ID = "micronaut-views-react";
    private static final String[] FRONTEND_FILES = new String[]{
        "package.json",
        "client.js",
        "server.js",
        "webpack.client.js",
        "webpack.server.js",
        "components/App.js"
    };

    @Override
    public String getName() {
        return "views-react";
    }

    @Override
    public String getTitle() {
        return "React SSR";
    }

    @Override
    public String getDescription() {
        return "Adds support for Server-Side View Rendering of ReactJS components using the GraalJS engine.";
    }

    @Override
    public boolean isPreview() {
        // June 2024: Module is brand new, it may still need to change once it's been used in anger.
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        ModuleContext module = generatorContext.getRootModule();
        OpenRewriteFeature.super.apply(generatorContext);
        try {

            if (OptionUtils.hasGradleBuildTool(generatorContext.getOptions())) {

                // This plugin teaches Gradle to download NodeJS and use it to run JS programs.
                //todo recipe to add build import
                module.addBuildPlugin(
                    GradlePlugin.builder()
                        .id("com.github.node-gradle.node")
                        .version(NODE_GRADLE_PLUGIN_VERSION)
                        .buildImports("import com.github.gradle.node.npm.task.NpxTask")
                        .build()
                );

                // This teaches Gradle to download the right GraalVM automatically (community edition).
                // For some reason Gradle won't do it out of the box :(
                //todo add openrewrite support for Add Gradle settings plugin
                module.addBuildPlugin(
                    GradlePlugin.builder()
                        .id("org.gradle.toolchains.foojay-resolver-convention")
                        .version("0.8.0")
                        .gradleFile(GradleFile.SETTINGS)
                        .build()
                );
            } else
                if (OptionUtils.hasMavenBuildTool(generatorContext.getOptions())) {
                    // We spell out the individual dependencies here because the Starter dependency management code for
                    // Maven builds can't express the direct pom dependency needed by Truffle.

                    Coordinate coordinate = generatorContext.resolveCoordinate("frontend-maven-plugin");
                    module.addBuildPlugin(
                        MavenPlugin.builder()
                            .artifactId(StarterCoordinates.FRONTEND_MAVEN_PLUGIN.getArtifactId())
                            .extension(new RockerWritable(mvnPluginReact.template(java.util.Objects.requireNonNull(coordinate.getGroupId()), coordinate.getArtifactId(), java.util.Objects.requireNonNull(coordinate.getVersion()))))
                            .build()
                    );
                }

            // Set up the frontend project. These are *not* resources under views/ because they're raw inputs that will
            // be minified and transpiled as part of the build pipeline.
            var ourResourceURL = Thread.currentThread().getContextClassLoader().getResource("views/react").toString();
            for (var fileName : FRONTEND_FILES) {
                module.addTemplate(
                    fileName,
                    new URLTemplate("src/main/js/" + fileName, new URL(ourResourceURL + "/" + fileName))
                );
            }
            var sourceFile = generatorContext.getSourcePath("/{packagePath}/AppController");

            if (generatorContext.getLanguage() == Language.JAVA) {
                module.addTemplate("AppController.java",
                    new RockerTemplate(sourceFile, reactControllerJava.template(generatorContext.getProject())));
            } else if (generatorContext.getLanguage() == Language.KOTLIN) {
                module.addTemplate("AppController.kt",
                    new RockerTemplate(sourceFile, reactControllerKotlin.template(generatorContext.getProject())));
            }

            // This will stop being necessary in Truffle 24.1
            module.configuration().addNested("micronaut.executors.blocking.virtual", "false");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);   // Cannot happen.
        }
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        List<String> recipes = new ArrayList<>();
        if (OptionUtils.hasGradleBuildTool(generatorContext.getOptions())) {
            recipes.add("io.micronaut.starter.feature.views-react-gradle");
        } else if (OptionUtils.hasMavenBuildTool(generatorContext.getOptions())) {
            recipes.add("io.micronaut.starter.feature.views-react-maven");
        }
        recipes.add("io.micronaut.starter.feature.views-react");
        return recipes;
    }
}
