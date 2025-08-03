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
package io.micronaut.starter.feature.graallanguages;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.rocker.RockerWritable;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.CoordinateResolver;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.starter.buildtools.dependencies.MicronautDependencyUtils;
import io.micronaut.projectgen.core.buildtools.maven.MavenPlugin;
import io.micronaut.starter.feature.Category;
import io.micronaut.projectgen.core.buildtools.maven.MavenSpecificFeature;
import io.micronaut.starter.feature.MinJdkFeature;
import io.micronaut.projectgen.micronaut.template.graallanguages.graalPyMavenPlugin;
import io.micronaut.projectgen.core.options.JdkVersion;
import jakarta.inject.Singleton;

import java.util.Collections;
import java.util.List;

/**
 * Graalpy feature that adds support for Python using GraalPy within Micronaut projects.
 * Implements MinJdkFeature to specify minimum JDK version and MavenSpecificFeature for Maven build integration.
 */
@Requires(property = "micronaut.starter.feature.graalpy.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Graalpy implements MinJdkFeature, MavenSpecificFeature {
    public static final String NAME = "graalpy";

    private static final String GROUP_ID_GRAALVM_PYTHON = "org.graalvm.python";
    private static final String ARTIFACT_ID_GRAALPY_MAVEN_PLUGIN = "graalpy-maven-plugin";
    private static final String ARTIFACT_ID_MICRONAUT_GRAALPY = "micronaut-graalpy";
    private static final Dependency MICRONAUT_GRAALPY_DEPENDENCY = MicronautDependencyUtils.graalLanguagesDependency()
        .artifactId(ARTIFACT_ID_MICRONAUT_GRAALPY)
        .compile()
        .build();

    private final CoordinateResolver coordinateResolver;

    public Graalpy(CoordinateResolver coordinateResolver) {
        this.coordinateResolver = coordinateResolver;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Micronaut GraalPy Extension";
    }

    @Override
    public String getDescription() {
        return "Adds support for Python using GraalPy";
    }

    @Override
    public String getCategory() {
        return Category.LANGUAGES;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        ModuleContext module = generatorContext.getRootModule();
        addDependencies(module);
        if (OptionUtils.hasMavenBuildTool(generatorContext.getOptions())) {
            addGraalPyMavenPlugin(module);
        }
    }

    private void addGraalPyMavenPlugin(ModuleContext module) {
        module.addBuildPlugin(graalpyMavenPlugin());
    }

    protected final MavenPlugin graalpyMavenPlugin() {
        return MavenPlugin.builder()
            .groupId(GROUP_ID_GRAALVM_PYTHON)
            .artifactId(ARTIFACT_ID_GRAALPY_MAVEN_PLUGIN)
            .extension(new RockerWritable(graalPyMavenPlugin.template(pythonPackages())))
            .build();
    }

    protected final List<String> pythonPackages() {
        return Collections.emptyList();
    }

    protected final void addDependencies(ModuleContext module) {
        module.addDependency(MICRONAUT_GRAALPY_DEPENDENCY);
    }

    @Override
    public String getThirdPartyDocumentation(GeneratorContext generatorContext) {
        return "https://graalvm.org/python";
    }

    @Override
    public String getFrameworkDocumentation(GeneratorContext generatorContext) {
        return "https://micronaut-projects.github.io/micronaut-graal-languages/latest/guide/";
    }

    @Override
    public JdkVersion minJdk() {
        return JdkVersion.JDK_21;
    }
}
