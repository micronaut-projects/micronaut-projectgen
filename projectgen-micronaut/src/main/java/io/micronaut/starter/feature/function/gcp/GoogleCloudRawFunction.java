/*
 * Copyright 2017-2023 original authors
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
package io.micronaut.starter.feature.function.gcp;

import com.fizzed.rocker.RockerModel;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.Project;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.micronaut.gradle.ShadePlugin;
import io.micronaut.starter.buildtools.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.CodeContributingFeature;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.micronaut.template.function.gcp.gcpFunctionReadme;
import io.micronaut.projectgen.micronaut.template.function.gcp.raw.gcpRawBackgroundFunctionGroovy;
import io.micronaut.projectgen.micronaut.template.function.gcp.raw.gcpRawBackgroundFunctionJava;
import io.micronaut.projectgen.micronaut.template.function.gcp.raw.gcpRawBackgroundFunctionKotlin;
import io.micronaut.projectgen.micronaut.template.function.gcp.raw.gcpRawFunctionGroovyJunit;
import io.micronaut.projectgen.micronaut.template.function.gcp.raw.gcpRawFunctionJavaJunit;
import io.micronaut.projectgen.micronaut.template.function.gcp.raw.gcpRawFunctionKoTest;
import io.micronaut.projectgen.micronaut.template.function.gcp.raw.gcpRawFunctionKotlinJunit;
import io.micronaut.projectgen.micronaut.template.function.gcp.raw.gcpRawFunctionSpock;
import io.micronaut.starter.feature.json.JacksonDatabindFeature;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import jakarta.inject.Singleton;

import java.util.Optional;

/**
 * Feature that adds support for deploying raw functions to Google Cloud Functions in a Micronaut project.
 * <p>
 * Provides integration with Google Cloud's Functions Framework and sets up the necessary templates,
 * dependencies, and configuration for function-based applications.
 */
@Requires(property = "micronaut.starter.feature.google.cloud.function.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class GoogleCloudRawFunction extends AbstractGoogleCloudFunction {
    public static final String NAME = "google-cloud-function";

    private static final Dependency MICRONAUT_GCP_FUNCTION = MicronautDependencyUtils
        .gcpDependency()
        .artifactId("micronaut-gcp-function")
        .compile()
        .build();

    private final GoogleCloudFunction googleCloudFunction;

    public GoogleCloudRawFunction(GoogleCloudFunction googleCloudFunction, ShadePlugin shadePlugin, JacksonDatabindFeature jacksonDatabindFeature) {
        super(shadePlugin, jacksonDatabindFeature);
        this.googleCloudFunction = googleCloudFunction;
    }

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);
        ModuleContext module = generatorContext.getRootModule();
        ApplicationType type = ApplicationType.of(generatorContext.getOptions().template());
        if (type == ApplicationType.FUNCTION && generatorContext.isFeatureMissing(CodeContributingFeature.class)) {
            Project project = generatorContext.getProject();
            String sourceFile = generatorContext.getSourcePath("/{packagePath}/Function");
            module.addTemplate(generatorContext.getOptions().language(),
                "function",
                sourceFile,
                gcpRawBackgroundFunctionJava.template(project),
                gcpRawBackgroundFunctionKotlin.template(project),
                gcpRawBackgroundFunctionGroovy.template(project)
            );

            applyTestTemplate(generatorContext, project, "Function");
            addDependencies(module, generatorContext.getOptions());
        }
    }

    /**
     * Adds dependencies required for Google Cloud Raw Function to the module context.
     * The dependencies added are specific to the build tool specified in the project creation options.
     *
     * @param module  the module context to add dependencies to
     * @param options the project creation options
     */
    void addDependencies(ModuleContext module, Options options) {
        module.addDependency(MICRONAUT_GCP_FUNCTION);
        module.addDependency(GCP_FUNCTIONS_FRAMEWORK_API.compileOnly());
        if (OptionUtils.hasGradleBuildTool(options)) {
            module.addDependency(GCP_FUNCTIONS_FRAMEWORK_API.test());
        }
    }

    @Override
    public String getTitle() {
        return "Google Cloud Function";
    }

    @Override
    public String getDescription() {
        return "Adds support for writing functions to deploy to Google Cloud Function";
    }

    @Override
    protected Optional<RockerModel> readmeTemplate(
        GeneratorContext generatorContext,
        Project project,
        BuildTool buildTool) {
        ApplicationType applicationType = ApplicationType.of(generatorContext.getOptions().template());
        return Optional.of(
            gcpFunctionReadme.template(
                project,
                generatorContext.getFeatures(),
                getRunCommand(buildTool),
                getBuildCommand(buildTool),
                applicationType == ApplicationType.FUNCTION
            )
        );
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        super.processSelectedFeatures(featureContext);
        ApplicationType applicationType = ApplicationType.of(featureContext.getOptions().template());
        if (applicationType == ApplicationType.DEFAULT) {
            featureContext.addFeature(
                googleCloudFunction
            );
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
    protected String getRunCommand(BuildTool buildTool) {
        return googleCloudFunction.getRunCommand(buildTool);
    }

    @Override
    protected String getBuildCommand(BuildTool buildTool) {
        return googleCloudFunction.getBuildCommand(buildTool);
    }

    @Override
    protected RockerModel javaJUnitTemplate(Project project) {
        return gcpRawFunctionJavaJunit.template(project);
    }

    @Override
    protected RockerModel kotlinJUnitTemplate(Project project) {
        return gcpRawFunctionKotlinJunit.template(project);
    }

    @Override
    protected RockerModel groovyJUnitTemplate(Project project) {
        return gcpRawFunctionGroovyJunit.template(project);
    }

    @Override
    protected RockerModel koTestTemplate(Project project) {
        return gcpRawFunctionKoTest.template(project);
    }

    @Override
    public RockerModel spockTemplate(Project project) {
        return gcpRawFunctionSpock.template(project);
    }

    @Override
    public String getFrameworkDocumentation(GeneratorContext generatorContext) {
        return "https://micronaut-projects.github.io/micronaut-gcp/latest/guide/index.html#simpleFunctions";
    }
}
