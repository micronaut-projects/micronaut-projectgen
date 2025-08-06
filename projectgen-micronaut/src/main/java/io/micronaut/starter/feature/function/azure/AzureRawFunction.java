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
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.Project;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.CoordinateResolver;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.micronaut.gradle.ShadePlugin;
import io.micronaut.starter.buildtools.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.CodeContributingFeature;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.micronaut.template.function.azure.azureFunctionReadme;
import io.micronaut.projectgen.micronaut.template.function.azure.raw.azureRawFunctionHttpRequestJava;
import io.micronaut.projectgen.micronaut.template.function.azure.raw.azureRawFunctionResponseBuilderJava;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.starter.options.DefaultTestRockerModelProvider;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.rocker.TestRockerModelProvider;
import io.micronaut.projectgen.core.rocker.RockerTemplate;

import jakarta.inject.Singleton;
import java.util.Optional;

/**
 * Feature implementation for a raw Azure Function in Micronaut.
 * <p>
 * Provides configuration and setup for using Azure Functions without the HTTP abstraction,
 * including dependency management, test generation, and integration with the Azure HTTP function feature.
 * </p>
 */
@Requires(property = "micronaut.starter.feature.azure.function.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class AzureRawFunction extends AbstractAzureFunction {
    private static final Dependency MICRONAUT_AZURE_FUNCTION = MicronautDependencyUtils
        .azureDependency()
        .artifactId("micronaut-azure-function")
        .compile()
        .build();
    private final AzureHttpFunction httpFunction;

    public AzureRawFunction(CoordinateResolver coordinateResolver, AzureHttpFunction httpFunction) {
        super(coordinateResolver);
        this.httpFunction = httpFunction;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.exclude(ShadePlugin.class::isInstance);
        ApplicationType applicationType = ApplicationType.of(featureContext.getOptions().template());
        if (applicationType == ApplicationType.DEFAULT) {
            featureContext.addFeature(httpFunction);
        }
    }

    @Override
    protected void applyTestTemplate(GeneratorContext generatorContext, Project project, String name) {
        ApplicationType applicationType = ApplicationType.of(generatorContext.getOptions().template());
        if (applicationType == ApplicationType.FUNCTION) {
            super.applyTestTemplate(generatorContext, project, name);
        }
    }

    @Override
    protected void applyFunction(GeneratorContext generatorContext, ApplicationType type) {
        super.applyFunction(generatorContext, type);

        if (type == ApplicationType.FUNCTION
            && generatorContext.isFeatureMissing(CodeContributingFeature.class)
            && !(OptionUtils.hasMavenBuildTool(generatorContext.getOptions()) && generatorContext.getLanguage() == Language.KOTLIN)) {
            Project project = generatorContext.getProject();

            generateJavaTestClass(generatorContext,
                "HttpRequestTemplate",
                "HttpRequest",
                azureRawFunctionHttpRequestJava.template(project));

            generateJavaTestClass(generatorContext,
                "ResponseBuilderTemplate",
                "ResponseBuilder",
                azureRawFunctionResponseBuilderJava.template(project));

            String testSource = generatorContext.getTestSourcePath("/{packagePath}/Function");
            TestRockerModelProvider provider = new DefaultTestRockerModelProvider(spockTemplate(project),
                javaJUnitTemplate(project),
                groovyJUnitTemplate(project),
                kotlinJUnitTemplate(project),
                koTestTemplate(project));
            ModuleContext module = generatorContext.getRootModule();
            module.addTemplate(generatorContext.getOptions(), "testFunction", testSource, provider);
        }
    }

    private void generateJavaTestClass(GeneratorContext generatorContext,
        String templateName,
        String name,
        RockerModel javaModel) {
        String  testSource = Language.JAVA.getTestSrcDir() + "/{packagePath}/" + name + "." + Language.JAVA.getExtension();
        ModuleContext module = generatorContext.getRootModule();
        module.addTemplate(templateName, new RockerTemplate(testSource, javaModel));
    }

    @Override
    protected Optional<RockerModel> readmeTemplate(GeneratorContext generatorContext, Project project, BuildTool buildTool) {
        return Optional.of(
            azureFunctionReadme.template(
                project,
                generatorContext.getFeatures(),
                getRunCommand(buildTool),
                getBuildCommand(buildTool),
                buildTool)
        );
    }

    @Override
    public String getFrameworkDocumentation(GeneratorContext generatorContext) {
        return "https://micronaut-projects.github.io/micronaut-azure/latest/guide/index.html#simpleAzureFunctions";
    }

    @Override
    public String getThirdPartyDocumentation(GeneratorContext generatorContext) {
        return "https://docs.microsoft.com/azure";
    }

    @Override
    protected void addDependencies(ModuleContext module, Options options) {
        super.addDependencies(module, options);
        module.addDependency(MICRONAUT_AZURE_FUNCTION);
    }
}
