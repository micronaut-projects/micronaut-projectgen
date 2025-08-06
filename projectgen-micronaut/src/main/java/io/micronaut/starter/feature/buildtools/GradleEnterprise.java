/*
 * Copyright 2017-2021 original authors
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
package io.micronaut.starter.feature.buildtools;

import com.fizzed.rocker.RockerModel;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.rocker.RockerWritable;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.gradle.GradleFile;
import io.micronaut.projectgen.core.buildtools.gradle.GradlePlugin;
import io.micronaut.starter.feature.Category;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.micronaut.template.buildtools.gradle.gradleEnterprise;
import io.micronaut.projectgen.micronaut.template.buildtools.maven.extensions;
import io.micronaut.projectgen.core.rocker.RockerTemplate;
import jakarta.inject.Singleton;

/**
 * Feature to add Gradle Enterprise plugin support, enabling integration
 * with Gradle Enterprise and scans.gradle.com.
 */
@Requires(property = "micronaut.starter.feature.gradle.enterprise.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class GradleEnterprise implements Feature, GradleEnterpriseConfiguration {
    public static final String GRADLE_ENTERPRISE_PLUGIN_ID = "com.gradle.enterprise";
    public static final String GRADLE_ENTERPRISE_ARTIFACT_ID = "gradle-enterprise-gradle-plugin";
    private static final String SLASH = "/";
    private static final String MAVEN_FOLDER = ".mvn";
    private static final String EXTENSIONS_XML = "extensions.xml";
    private static final String GRADLE_ENTERPRISE_XML = "gradle-enterprise.xml";
    private static final String DOT = ".";
    private static final String ARTIFACT_ID_GRADLE_ENTERPRISE_MAVEN_EXTENSION = "gradle-enterprise-maven-extension";
    private static final String ARTIFACT_ID_COMMON_CUSTOM_USER_DATA_MAVEN_EXTENSION = "common-custom-user-data-maven-extension";

    @Override
    @NonNull
    public String getName() {
        return "gradle-enterprise";
    }

    @Override
    @NonNull
    public String getTitle() {
        return "Gradle Enterprise";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds Gradle Enterprise Gradle plugin which enables integration with Gradle Enterprise and scans.gradle.com";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        ModuleContext module = generatorContext.getRootModule();
        if (OptionUtils.hasGradleBuildTool(generatorContext.getOptions())) {
            module.addBuildPlugin(gradlePlugin(this));
        }
        if (OptionUtils.hasMavenBuildTool(generatorContext.getOptions())) {
            applyMaven(generatorContext, this);
        }
    }

    /**
     * Creates a {@link GradlePlugin} instance configured with the Gradle Enterprise plugin.
     *
     * @param configuration The Gradle Enterprise configuration to apply.
     * @return A {@link GradlePlugin} configured with the Gradle Enterprise plugin.
     */
    protected GradlePlugin gradlePlugin(GradleEnterpriseConfiguration configuration) {
        return GradlePlugin.builder()
            .gradleFile(GradleFile.SETTINGS)
            .id(GRADLE_ENTERPRISE_PLUGIN_ID)
            .lookupArtifactId(GRADLE_ENTERPRISE_ARTIFACT_ID)
            .settingsExtension(new RockerWritable(gradleEnterprise.template(configuration)))
            .build();
    }

    /**
     * Applies the necessary Maven templates to enable Gradle Enterprise support.
     *
     * @param generatorContext The generator context containing the project state.
     * @param server The Gradle Enterprise configuration to use for the templates.
     */
    protected void applyMaven(GeneratorContext generatorContext, GradleEnterpriseConfiguration server) {
        addMavenTemplate(generatorContext, EXTENSIONS_XML, extensionsRockerModel(generatorContext));
        addMavenTemplate(generatorContext, GRADLE_ENTERPRISE_XML, io.micronaut.projectgen.micronaut.template.buildtools.maven.gradleEnterprise.template(server));
    }

    /**
     * Adds a Maven template to the root module using the given name and {@link RockerModel}.
     *
     * @param generatorContext The generator context containing the project state.
     * @param name The name of the template file.
     * @param rockerModel The rocker model used to generate the template content.
     */
    protected void addMavenTemplate(GeneratorContext generatorContext, String name, RockerModel rockerModel) {
        String templateName = name.contains(DOT) ? name.substring(0, name.indexOf(DOT)) : name;
        String path = String.join(SLASH, MAVEN_FOLDER, name);
        ModuleContext module = generatorContext.getRootModule();
        module.addTemplate(templateName, new RockerTemplate(path, rockerModel));
    }

    private static RockerModel extensionsRockerModel(GeneratorContext generatorContext) {
        return extensions.template(
            generatorContext.resolveCoordinate(ARTIFACT_ID_GRADLE_ENTERPRISE_MAVEN_EXTENSION).getVersion(),
            generatorContext.resolveCoordinate(ARTIFACT_ID_COMMON_CUSTOM_USER_DATA_MAVEN_EXTENSION).getVersion());
    }

    @Override
    public String getCategory() {
        return Category.DEV_TOOLS;
    }

    @Override
    public String getThirdPartyDocumentation(GeneratorContext generatorContext) {
        return "https://docs.gradle.com/enterprise/gradle-plugin/";
    }
}
