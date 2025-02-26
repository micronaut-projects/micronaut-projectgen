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
package io.micronaut.projectgen.core.buildtools.gradle;

import com.fizzed.rocker.RockerModel;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.RepositoryResolver;
import io.micronaut.projectgen.core.feature.BuildFeature;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.rocker.RockerTemplate;
import io.micronaut.projectgen.core.template.BinaryTemplate;
import io.micronaut.projectgen.core.template.Template;
import io.micronaut.projectgen.core.template.URLTemplate;
import jakarta.inject.Singleton;
import io.micronaut.projectgen.core.template.genericBuildGradle;
import io.micronaut.projectgen.core.template.gradleProperties;
import io.micronaut.projectgen.core.template.settingsGradle;
import java.util.Set;

/**
 * Gradle Feature.
 */
@Requires(property = "micronaut.projectgen.core.features.gradle.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Gradle implements BuildFeature {
    private static final String SLASH = "/";
    private static final String GRADLE = "gradle";
    private static final String WRAPPER = "wrapper";
    private static final boolean DEFAULT_USER_VERSION_CATALOGUE = false;
    private static final String GRADLE_WRAPPER_JAR = "gradle-wrapper.jar";
    private static final String GRADLE_WRAPPER_PROPERTIES = "gradle-wrapper.properties";
    private static final String GRADLEW_PATH = "gradlew";
    private static final String GRADLEW_BAT_PATH = "gradlew.bat";
    private static final String WRAPPER_JAR_PATH = GRADLE + SLASH + WRAPPER + SLASH + GRADLE_WRAPPER_JAR;
    private static final String GRADLE_8_11_1 = "8.11.1";
    private static final String GRADLE_VERSION = GRADLE_8_11_1;
    private static final String WRAPPER_JAR = GRADLE + SLASH + GRADLE_VERSION + SLASH + WRAPPER_JAR_PATH;
    private static final String WRAPPER_PROPS_PATH = GRADLE + SLASH + WRAPPER + SLASH + GRADLE_WRAPPER_PROPERTIES;
    private static final String WRAPPER_PROPS = GRADLE + SLASH  + GRADLE_VERSION + SLASH + WRAPPER_PROPS_PATH;
    private static final String GRADLEW = GRADLE + SLASH  + GRADLE_VERSION + SLASH + GRADLEW_PATH;
    private static final String GRADLEW_BAT = GRADLE + SLASH  + GRADLE_VERSION + SLASH + GRADLEW_BAT_PATH;
    private static final String NAME_GRADLE_WRAPPER_JAR = "gradleWrapperJar";
    private static final String NAME_GRADLE_WRAPPER_PROPERTIES = "gradleWrapperProperties";
    private static final String NAME_GRADLE_WRAPPER = "gradleWrapper";
    private static final String NAME_GRADLE_WRAPPER_BAT = "gradleWrapperBat";
    private static final String NAME = "gradle";
    private static final String NAME_BUILD_GRADLE = "build.gradle";
    private static final String SETTINGS_GRADLE = "settings.gradle";
    private static final String SETTINGS_GRADLE_KTS = "settings.gradle.kts";
    private static final String GRADLE_PROPERTIES = "gradle.properties";

    protected final GradleBuildCreator dependencyResolver;
    protected final RepositoryResolver repositoryResolver;

    public Gradle(GradleBuildCreator dependencyResolver,
                  RepositoryResolver repositoryResolver) {
        this.dependencyResolver = dependencyResolver;
        this.repositoryResolver = repositoryResolver;
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addGradleInitFiles(generatorContext);
        generateBuildFiles(generatorContext);
    }

    @Override
    public boolean isGradle() {
        return true;
    }

    @Override
    public boolean shouldApply(String applicationType,
                               Options options,
                               Set<Feature> selectedFeatures) {
        return options.buildTools().stream().anyMatch(BuildTool::isGradle);
    }

    /**
     *
     * @param generatorContext Generator Context
     */
    protected void generateBuildFiles(GeneratorContext generatorContext) {
        BuildTool buildTool = generatorContext.getOptions().buildTools().stream().filter(BuildTool::isGradle).findFirst().orElseThrow();
        GradleBuild build = createBuild(generatorContext);
        RockerModel rockerModel = genericBuildGradle.template(generatorContext.getProject(),
            build,
            generatorContext.getFeatures().mainClass().orElse(null),
            generatorContext.getOptions().version(),
            StringUtils.isNotEmpty(generatorContext.getOptions().group()) ? generatorContext.getOptions().group() : generatorContext.getProject().getPackageName());
        generatorContext.addTemplate(NAME_BUILD_GRADLE,
            new RockerTemplate(Template.ROOT, buildTool.getBuildFileName(), rockerModel));
        addSettingsFile(buildTool, generatorContext, build);
    }

    /**
     *
     * @param generatorContext  Generator Context
     */
    protected void addGradleInitFiles(GeneratorContext generatorContext) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        generatorContext.addTemplate(NAME_GRADLE_WRAPPER_JAR, new BinaryTemplate(Template.ROOT, WRAPPER_JAR_PATH, classLoader.getResource(WRAPPER_JAR)));
        generatorContext.addTemplate(NAME_GRADLE_WRAPPER_PROPERTIES, new URLTemplate(Template.ROOT, WRAPPER_PROPS_PATH, classLoader.getResource(WRAPPER_PROPS)));
        generatorContext.addTemplate(NAME_GRADLE_WRAPPER, new URLTemplate(Template.ROOT, GRADLEW_PATH, classLoader.getResource(GRADLEW), true));
        generatorContext.addTemplate(NAME_GRADLE_WRAPPER_BAT, new URLTemplate(Template.ROOT, GRADLEW_BAT_PATH, classLoader.getResource(GRADLEW_BAT), false));
    }

    /**
     *
     * @param generatorContext  Generator Context
     * @return Gradle Build
     */
    protected GradleBuild createBuild(GeneratorContext generatorContext) {
        return dependencyResolver.create(generatorContext, repositoryResolver.resolveRepositories(generatorContext), Gradle.DEFAULT_USER_VERSION_CATALOGUE);
    }

    /**
     *
     * @param generatorContext  Generator Context
     */
    protected void addGradleProperties(GeneratorContext generatorContext) {
        generatorContext.addTemplate("projectProperties", new RockerTemplate(Template.ROOT, GRADLE_PROPERTIES, gradleProperties.template(generatorContext.getBuildProperties().getProperties())));
    }

    /**
     * @param buildTool Gradle Build Tool
     * @param generatorContext  Generator Context
     * @param build Gradle Build
     */
    protected void addSettingsFile(BuildTool buildTool, GeneratorContext generatorContext, GradleBuild build) {
        boolean hasMultiProjectFeature = generatorContext.getFeatures().hasMultiProjectFeature();
        String settingsFile = buildTool == BuildTool.GRADLE ? SETTINGS_GRADLE : SETTINGS_GRADLE_KTS;
        generatorContext.addTemplate("gradleSettings",
            new RockerTemplate(Template.ROOT, settingsFile,
                settingsGradle.template(generatorContext.getProject(), build, hasMultiProjectFeature, generatorContext.getModuleNames())));
    }
}

