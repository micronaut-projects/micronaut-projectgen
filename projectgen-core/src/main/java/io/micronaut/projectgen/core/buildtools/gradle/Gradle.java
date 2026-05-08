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

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.BuildToolUtils;
import io.micronaut.projectgen.core.buildtools.Property;
import io.micronaut.projectgen.core.feature.BuildFeature;
import io.micronaut.projectgen.core.feature.DefaultFeature;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.rocker.RockerTemplate;
import io.micronaut.projectgen.core.template.BinaryTemplate;
import io.micronaut.projectgen.core.template.URLTemplate;
import io.micronaut.projectgen.core.utils.OptionUtils;
import jakarta.inject.Singleton;
import io.micronaut.projectgen.core.template.gradleProperties;
import io.micronaut.projectgen.core.template.settingsGradle;

import java.util.List;
import java.util.Set;

/**
 * Gradle Feature.
 */
@Requires(property = "micronaut.projectgen.core.features.gradle.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Gradle implements BuildFeature, DefaultFeature {
    private static final String SLASH = "/";
    private static final String GRADLE = "gradle";
    private static final String WRAPPER = "wrapper";
    private static final String GRADLE_WRAPPER_JAR = "gradle-wrapper.jar";
    private static final String GRADLE_WRAPPER_PROPERTIES = "gradle-wrapper.properties";
    private static final String GRADLEW_PATH = "gradlew";
    private static final String GRADLEW_BAT_PATH = "gradlew.bat";
    private static final String WRAPPER_JAR_PATH = GRADLE + SLASH + WRAPPER + SLASH + GRADLE_WRAPPER_JAR;
    private static final String GRADLE_9_5_0 = "9.5.0";
    private static final String GRADLE_VERSION = GRADLE_9_5_0;
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
    private static final String GRADLE_PROPERTIES = "gradle.properties";

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public boolean shouldApply(Options options, Set<Feature> selectedFeatures) {
        return OptionUtils.hasGradleBuildTool(options);
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        ModuleContext rootModule = generatorContext.getRootModule();
        addGradleInitFiles(rootModule);
        generateBuildFiles(generatorContext, rootModule);
        addGradleProperties(rootModule);
    }

    @Override
    public boolean isGradle() {
        return true;
    }

    /**
     *
     * @param generatorContext Generator Context
     * @param rootModule Root Module
     */
    protected void generateBuildFiles(GeneratorContext generatorContext, ModuleContext rootModule) {
        for (String module : generatorContext.getModuleNames()) {
            ModuleContext moduleContext = generatorContext.getModuleByName(module);
            generateBuildFiles(generatorContext, moduleContext, module);
        }
        generateBuildFiles(generatorContext, rootModule, "");

        BuildTool buildTool = generatorContext.getOptions().buildTools().stream()
            .filter(bt -> bt == BuildTool.GRADLE).findFirst().orElseThrow();
        GradleBuild build = GradleBuildCreator.create(generatorContext, rootModule, generatorContext.getOptions());
        addSettingsFile(buildTool, generatorContext, build, rootModule);
    }

    /**
     *
     * @param generatorContext Generator Context
     * @param moduleContext Module context
     * @param module Module name
     */
    protected void generateBuildFiles(GeneratorContext generatorContext, ModuleContext moduleContext, String module) {
        moduleContext.addTemplate(module + NAME_BUILD_GRADLE,
            GradleBuildCreator.buildFileTemplate(generatorContext, moduleContext, module));
    }

    /**
     *
     * @param module Module
     */
    protected void addGradleInitFiles(ModuleContext module) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        module.addTemplate(NAME_GRADLE_WRAPPER_JAR, new BinaryTemplate(WRAPPER_JAR_PATH, classLoader.getResource(WRAPPER_JAR)));
        module.addTemplate(NAME_GRADLE_WRAPPER_PROPERTIES, new URLTemplate(WRAPPER_PROPS_PATH, classLoader.getResource(WRAPPER_PROPS)));
        module.addTemplate(NAME_GRADLE_WRAPPER, new URLTemplate(GRADLEW_PATH, classLoader.getResource(GRADLEW), true));
        module.addTemplate(NAME_GRADLE_WRAPPER_BAT, new URLTemplate(GRADLEW_BAT_PATH, classLoader.getResource(GRADLEW_BAT), false));
    }

    /**
     *
     * @param module Module
     */
    protected void addGradleProperties(ModuleContext module) {
        List<Property> properties = module.buildProperties().getProperties();
        if (!properties.isEmpty()) {
            module.addTemplate("projectProperties", new RockerTemplate(GRADLE_PROPERTIES, gradleProperties.template(properties)));
        }
    }

    /**
     * @param buildTool Gradle Build Tool
     * @param generatorContext  Generator Context
     * @param build Gradle Build
     * @param module Module
     */
    protected void addSettingsFile(BuildTool buildTool, GeneratorContext generatorContext, GradleBuild build, ModuleContext module) {
        boolean hasMultiProjectFeature = generatorContext.getFeatures().hasMultiProjectFeature();
        String settingsFile = BuildToolUtils.settingsFileName(buildTool, generatorContext.getOptions().gradleDsl());
        module.addTemplate("gradleSettings",
            new RockerTemplate(settingsFile,
                settingsGradle.template(generatorContext.getProject(), build, hasMultiProjectFeature, generatorContext.getModuleNames())));
    }
}

