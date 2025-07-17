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
package io.micronaut.projectgen.micronaut.features.cli;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.gradle.GradleDsl;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.options.GenericOptionsBuilder;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.options.TestFramework;
import io.micronaut.projectgen.core.template.YamlTemplate;
import io.micronaut.projectgen.core.utils.OptionUtils;
import jakarta.inject.Singleton;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Singleton
public class MicronautCli implements Feature {
    private static final String KEY_APPLICATION_TYPE = "applicationType";
    private static final String KEY_TEST_FRAMEWORK = "testFramework";
    private static final String KEY_DEFAULT_PACKAGE = "defaultPackage";
    private static final String KEY_SOURCE_LANGUAGE = "sourceLanguage";
    private static final String KEY_FEATURES = "features";
    private static final String KEY_BUILD_TOOL = "buildTool";
    public static final String LEGACY_BUILD_TOOL_GRADLE_KOTLIN = "gradle_kotlin";
    public static final String LEGACY_BUILD_TOOL_GRADLE_GROOVY = "gradle";
    public static final String LEGACY_BUILD_TOOL_MAVEN = "maven";
    public static final String PATH = "micronaut-cli.yml";

    @Override
    public String getName() {
        return "micronaut-cli";
    }

    @Override
    public String getTitle() {
        return "Micronaut CLI";
    }

    @Override
    public String getDescription() {
        return "Adds a micronaut-cli.yml in the root of the project";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        Options options = generatorContext.getOptions();
        ModuleContext module = generatorContext.getRootModule();
        module.addTemplate("micronautCli",
            new YamlTemplate(PATH, config(options)));
    }

    public static Options load(File projectFolder) {
        if (!projectFolder.exists() || !projectFolder.isDirectory()) {
            throw new IllegalArgumentException("Project folder does not exist");
        }
        File cliFile = new File(projectFolder, PATH);
        if (!cliFile.exists()) {
            throw new IllegalArgumentException("micronaut-cli.yml file not found in project folder");
        }
        try (FileInputStream fis = new FileInputStream(cliFile)) {
            Yaml yaml = new Yaml();
            return load(yaml.loadAs(fis, MicronautCliConfig.class));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read micronaut-cli.yml", e);
        }
    }

    private static Options load(MicronautCliConfig config)  {
        var builder = GenericOptionsBuilder.builder();

        if (config.getApplicationType() != null) {
            builder.template(config.getApplicationType());
        }

        if (config.getTestFramework() != null) {
            builder.testFramework(TestFramework.valueOf(config.getTestFramework().toUpperCase()));
        }

        if (config.getDefaultPackage() != null) {
            builder.packageName(config.getDefaultPackage());
        }

        if (config.getSourceLanguage() != null) {
            builder.language(Language.valueOf(config.getSourceLanguage().toUpperCase()));
        }

        if (config.getFeatures() != null) {
            builder.features(config.getFeatures());
        }

        if (config.getBuildTool() != null) {
            switch (config.getBuildTool()) {
                case LEGACY_BUILD_TOOL_GRADLE_KOTLIN -> {
                    builder.buildTools(List.of(BuildTool.GRADLE));
                    builder.gradleDsl(GradleDsl.KOTLIN);
                }
                case LEGACY_BUILD_TOOL_GRADLE_GROOVY -> {
                    builder.buildTools(List.of(BuildTool.GRADLE));
                    builder.gradleDsl(GradleDsl.GROOVY);
                }
                case LEGACY_BUILD_TOOL_MAVEN -> builder.buildTools(List.of(BuildTool.MAVEN));
            }
        }

        return builder.build();
    }

    private static Map<String, Object> getStringObjectMap(File projectFolder) {
        if (!projectFolder.exists() || !projectFolder.isDirectory()) {
            throw new IllegalArgumentException("Project folder does not exist");
        }

        File cliFile = new File(projectFolder, PATH);
        if (!cliFile.exists()) {
            throw new IllegalArgumentException("micronaut-cli.yml file not found in project folder");
        }

        Yaml yaml = new Yaml();
        Map<String, Object> yamlValues;
        try (FileInputStream fileInputStream = new FileInputStream(cliFile)) {
            yamlValues = yaml.load(fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read micronaut-cli.yml", e);
        }
        return yamlValues;
    }

    static Map<String, Object> config(Options options) {
        Map<String, Object> config = new HashMap<>();
        config.put(KEY_APPLICATION_TYPE, options.template());
        config.put(KEY_TEST_FRAMEWORK, options.testFramework().toString());
        config.put(KEY_DEFAULT_PACKAGE, options.packageName());
        config.put(KEY_SOURCE_LANGUAGE, options.language().toString());
        config.put(KEY_FEATURES, options.features());
        legacyBuildToolName(options).ifPresent(name -> config.put(KEY_BUILD_TOOL, name));
        return config;
    }

    @NonNull
    static Optional<String> legacyBuildToolName(@NonNull Options options) {
        if (options.gradleDsl() == GradleDsl.KOTLIN && OptionUtils.hasGradleBuildTool(options)) {
            return Optional.of(LEGACY_BUILD_TOOL_GRADLE_KOTLIN);
        } else if (options.gradleDsl() == GradleDsl.GROOVY && OptionUtils.hasGradleBuildTool(options)) {
            return Optional.of(LEGACY_BUILD_TOOL_GRADLE_GROOVY);
        } else if (OptionUtils.hasMavenBuildTool(options)) {
            return Optional.of(LEGACY_BUILD_TOOL_MAVEN);
        }
        return Optional.empty();
    }

    @Override
    public boolean isVisible() {
        return false;
    }
}
