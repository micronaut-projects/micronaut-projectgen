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
package io.micronaut.starter.feature.function.azure;

import com.fizzed.rocker.RockerModel;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.rocker.RockerWritable;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.Project;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.BuildProperties;
import io.micronaut.projectgen.core.buildtools.dependencies.CoordinateResolver;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.buildtools.gradle.GradleDsl;
import io.micronaut.projectgen.core.buildtools.gradle.GradlePlugin;
import io.micronaut.projectgen.core.buildtools.maven.MavenPlugin;
import io.micronaut.projectgen.micronaut.MicronautOptions;
import io.micronaut.starter.feature.CodeContributingFeature;
import io.micronaut.starter.feature.function.AbstractFunctionFeature;
import io.micronaut.projectgen.micronaut.template.function.azure.azureFunctionMavenPlugin;
import io.micronaut.projectgen.micronaut.template.function.azure.azurefunctions;
import io.micronaut.projectgen.micronaut.template.function.azure.raw.azureRawFunctionGroovyJunit;
import io.micronaut.projectgen.micronaut.template.function.azure.raw.azureRawFunctionJavaJunit;
import io.micronaut.projectgen.micronaut.template.function.azure.raw.azureRawFunctionKoTest;
import io.micronaut.projectgen.micronaut.template.function.azure.raw.azureRawFunctionKotlinJunit;
import io.micronaut.projectgen.micronaut.template.function.azure.raw.azureRawFunctionSpock;
import io.micronaut.projectgen.micronaut.template.function.azure.raw.azureRawFunctionTriggerGroovy;
import io.micronaut.projectgen.micronaut.template.function.azure.raw.azureRawFunctionTriggerJava;
import io.micronaut.projectgen.micronaut.template.function.azure.raw.azureRawFunctionTriggerKotlin;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.options.JdkVersion;
import io.micronaut.projectgen.core.template.URLTemplate;

import java.util.Optional;

/**
 * Function impl for Azure.
 *
 * @author graemerocher
 * @since 1.0.0
 */
public abstract class AbstractAzureFunction extends AbstractFunctionFeature implements AzureCloudFeature, AzureMicronautRuntimeFeature {

    public static final String GROUP_ID_COM_MICROSOFT_AZURE_FUNCTIONS = "com.microsoft.azure.functions";
    public static final String ARTIFACT_ID_AZURE_FUNCTIONS_JAVA_LIBRARY = "azure-functions-java-library";

    public static final String NAME = "azure-function";
    private final CoordinateResolver coordinateResolver;

    public AbstractAzureFunction(CoordinateResolver coordinateResolver) {
        this.coordinateResolver = coordinateResolver;
    }

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Azure Function";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds support for writing functions to deploy to Microsoft Azure";
    }

    private void loadTemplates(ModuleContext module) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        module.addTemplate("host.json", new URLTemplate("host.json", classLoader.getResource("functions/azure/host.json")));
        module.addTemplate("local.settings.json", new URLTemplate("local.settings.json", classLoader.getResource("functions/azure/local.settings.json")));
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        ModuleContext module = generatorContext.getRootModule();
        super.apply(generatorContext);
        loadTemplates(module);
        Project project = generatorContext.getProject();
        BuildTool buildTool = generatorContext.getBuildTool();
        if (buildTool.isGradle()) {
            module.addHelpLink("Azure Functions Plugin for Gradle", "https://plugins.gradle.org/plugin/com.microsoft.azure.azurefunctions");
            module.addBuildPlugin(GradlePlugin.builder()
                    .id("com.microsoft.azure.azurefunctions")
                    .lookupArtifactId("azure-functions-gradle-plugin")
                    .extension(new RockerWritable(azurefunctions.template(generatorContext.getProject(), generatorContext.getBuildTool().getGradleDsl().orElse(GradleDsl.GROOVY), javaVersionValue(generatorContext).orElse("null"))))
                    .build());
        } else if (buildTool == BuildTool.MAVEN) {
            String mavenPluginArtifactId = "azure-functions-maven-plugin";
            module.addBuildPlugin(MavenPlugin.builder()
                    .artifactId(mavenPluginArtifactId)
                    .extension(new RockerWritable(azureFunctionMavenPlugin.template()))
                    .build());
            BuildProperties props = module.buildProperties();
            coordinateResolver.resolve(mavenPluginArtifactId)
                    .ifPresent(coordinate -> props.put("azure.functions.maven.plugin.version", coordinate.getVersion()));
            props.put("functionAppName", project.getName());
            props.put("functionResourceGroup", "java-functions-group");
            props.put("functionAppRegion", "westus");
            props.put("functionRuntimeOs", "windows");
            javaVersionValue(generatorContext).ifPresent(value -> props.put("functionRuntimeJavaVersion", value));
            props.put("stagingDirectory", "${project.build.directory}/azure-functions/${functionAppName}");
        }
        addFunctionTemplate(module, generatorContext, generatorContext.getOptions(), project);

        addDependencies(module, generatorContext.getOptions());
    }

    @NonNull
    private Optional<String> javaVersionValue(GeneratorContext generatorContext) {
        if (OptionUtils.hasGradleBuildTool(generatorContext.getOptions())) {
            if (JdkVersion.JDK_17.equals(generatorContext.getJdkVersion())) {
                return Optional.of("Java 17");
            } else {
                return Optional.empty();
            }
        }
        if (JdkVersion.JDK_17.equals(generatorContext.getJdkVersion())) {
            return Optional.of("17");
        } else {
            return Optional.empty();
        }
    }

    protected void addFunctionTemplate(ModuleContext module, GeneratorContext generatorContext, Options options, Project project) {
        if (options instanceof MicronautOptions micronautOptions && micronautOptions.applicationType() == ApplicationType.FUNCTION
                && generatorContext.isFeatureMissing(CodeContributingFeature.class)) {
            String triggerFile = generatorContext.getSourcePath("/{packagePath}/Function");
            module.addTemplate(options.language(), "trigger", triggerFile,
                    azureRawFunctionTriggerJava.template(project),
                    azureRawFunctionTriggerKotlin.template(project),
                    azureRawFunctionTriggerGroovy.template(project));
        }
    }

    @Override
    protected RockerModel javaJUnitTemplate(Project project) {
        return azureRawFunctionJavaJunit.template(project);
    }

    @Override
    protected RockerModel kotlinJUnitTemplate(Project project) {
        return azureRawFunctionKotlinJunit.template(project);
    }

    @Override
    protected RockerModel groovyJUnitTemplate(Project project) {
        return azureRawFunctionGroovyJunit.template(project);
    }

    @Override
    protected RockerModel koTestTemplate(Project project) {
        return azureRawFunctionKoTest.template(project);
    }

    @Override
    public RockerModel spockTemplate(Project project) {
        return azureRawFunctionSpock.template(project);
    }

    @Override
    protected String getRunCommand(BuildTool buildTool) {
        if (buildTool == BuildTool.MAVEN) {
            return "mvnw clean package azure-functions:run";
        } else {
            return "gradlew azureFunctionsRun";
        }
    }

    @Override
    protected String getBuildCommand(BuildTool buildTool) {
        return AzureBuildCommandUtils.getBuildCommand(buildTool);
    }

    protected void addDependencies(ModuleContext module, Options options) {
        addAzureFunctionsJavaLibraryDependency(module, options);
    }

    protected void addAzureFunctionsJavaLibraryDependency(ModuleContext module, Options options) {
        Dependency.Builder builder = Dependency.builder()
                .groupId(GROUP_ID_COM_MICROSOFT_AZURE_FUNCTIONS)
                .artifactId(ARTIFACT_ID_AZURE_FUNCTIONS_JAVA_LIBRARY);
        if (OptionUtils.hasMavenBuildTool(options)) {
            module.addDependency(builder.developmentOnly());
        } else if (OptionUtils.hasGradleBuildTool(options)) {
            module.addDependency(builder.compile());
        }
    }
}
