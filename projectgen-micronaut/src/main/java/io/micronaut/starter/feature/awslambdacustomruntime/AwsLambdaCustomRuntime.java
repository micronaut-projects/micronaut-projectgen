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
package io.micronaut.starter.feature.awslambdacustomruntime;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.exceptions.ConfigurationException;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.core.options.GenericOptionsBuilder;
import io.micronaut.projectgen.core.rocker.RockerWritable;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.Project;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.starter.buildtools.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.ApplicationFeature;
import io.micronaut.starter.feature.Category;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.core.feature.Features;
import io.micronaut.starter.feature.aws.AwsCloudFeature;
import io.micronaut.projectgen.micronaut.template.awslambdacustomruntime.awsCustomRuntimeReadme;
import io.micronaut.projectgen.micronaut.template.awslambdacustomruntime.functionLambdaRuntimeGroovy;
import io.micronaut.projectgen.micronaut.template.awslambdacustomruntime.functionLambdaRuntimeJava;
import io.micronaut.projectgen.micronaut.template.awslambdacustomruntime.functionLambdaRuntimeKotlin;
import io.micronaut.starter.feature.function.FunctionFeature;
import io.micronaut.starter.feature.function.awslambda.AwsLambda;
import io.micronaut.starter.feature.graalvm.GraalVM;
import io.micronaut.projectgen.micronaut.features.httpclient.HttpClientFeature;
import io.micronaut.projectgen.micronaut.features.httpclient.HttpClientJdk;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

@Requires(property = "micronaut.starter.feature.aws.lambda.custom.runtime.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class AwsLambdaCustomRuntime implements FunctionFeature, ApplicationFeature, AwsCloudFeature, OpenRewriteFeature {
    public static final Dependency DEPENDENCY_AWS_FUNCTION_AWS_CUSTOM_RUNTIME = MicronautDependencyUtils.awsDependency()
        .artifactId("micronaut-function-aws-custom-runtime")
        .compile()
        .build();
    public static final String MAIN_CLASS_NAME = "io.micronaut.function.aws.runtime.MicronautLambdaRuntime";

    public static final String FEATURE_NAME_AWS_LAMBDA_CUSTOM_RUNTIME = "aws-lambda-custom-runtime";

    private final Provider<AwsLambda> awsLambda;
    private final HttpClientJdk httpClientJdk;

    public AwsLambdaCustomRuntime(Provider<AwsLambda> awsLambda,
        HttpClientJdk httpClientJdk) {
        this.awsLambda = awsLambda;
        this.httpClientJdk = httpClientJdk;
    }

    /**
     * Processes selected features and ensures required AWS Lambda and HTTP client features are present.
     *
     * @param featureContext The feature context to modify.
     */
    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        AwsLambda awsLambda = this.awsLambda.get();
        ApplicationType applicationType = ApplicationType.of(featureContext.getOptions().template());
        if (awsLambda.supports(GenericOptionsBuilder.builder().template(applicationType.toString()).build()) && !featureContext.isPresent(AwsLambda.class)) {
            featureContext.addFeature(awsLambda);
        }
        if (!featureContext.isPresent(HttpClientFeature.class)) {
            featureContext.addFeature(httpClientJdk);
        }
    }

    @Override
    @NonNull
    public String getName() {
        return FEATURE_NAME_AWS_LAMBDA_CUSTOM_RUNTIME;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public String getTitle() {
        return "Custom AWS Lambda runtime";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds support for deploying a Micronaut Function to a Custom AWS Lambda Runtime";
    }

    /**
     * Applies the AWS Lambda Custom Runtime feature to the generator context by adding required dependencies,
     * templates, and configuration settings.
     *
     * @param generatorContext The context of the generator to apply this feature to.
     */
    @SuppressWarnings("EmptyBlock")
    @Override
    public void apply(GeneratorContext generatorContext) {
        ApplicationFeature.super.apply(generatorContext);
        OpenRewriteFeature.super.apply(generatorContext);
        ModuleContext module = generatorContext.getRootModule();
        Project project = generatorContext.getProject();
        if (shouldGenerateMainClassForRuntime(generatorContext)) {
            addFunctionLambdaRuntime(generatorContext, module, project);
        }
        if (generatorContext.getFeatures().isFeaturePresent(GraalVM.class)) {
            module.addHelpTemplate(new RockerWritable(awsCustomRuntimeReadme.template(generatorContext.getOptions().getBuildTool())));
        }
    }

    /**
     * Determines if the FunctionLambdaRuntime main class should be generated.
     *
     * @param generatorContext The generator context.
     * @return {@code true} if the main class should be generated, {@code false} otherwise.
     */
    public boolean shouldGenerateMainClassForRuntime(GeneratorContext generatorContext) {
        ApplicationType applicationType = ApplicationType.of(generatorContext.getOptions().template());
        return applicationType == ApplicationType.FUNCTION
            && generatorContext.getFeatures().isFeaturePresent(AwsLambda.class);
    }

    /**
     * Returns the fully qualified main class name to use for AWS Lambda custom runtime,
     * based on the application type and features.
     *
     * @param generatorContext The generator context.
     * @return The main class name to use.
     * @throws ConfigurationException if AWS Lambda feature is missing.
     */
    @Override
    @Nullable
    public String mainClassName(GeneratorContext generatorContext) {
        Features features = generatorContext.getFeatures();
        if (features.isFeaturePresent(AwsLambda.class)) {
            ApplicationType applicationType = ApplicationType.of(generatorContext.getOptions().template());
            if (applicationType == ApplicationType.DEFAULT) {
                return AwsLambdaCustomRuntime.MAIN_CLASS_NAME;
            } else if (applicationType == ApplicationType.FUNCTION) {
                return generatorContext.getProject().getPackageName() + ".FunctionLambdaRuntime";
            }
        }
        throw new ConfigurationException("aws-lambda-custom-runtime should be used together with aws-lambda or aws-gateway-lambda-proxy");
    }

    /**
     * Adds the main class template for AWS Lambda custom runtime function.
     *
     * @param generatorContext The generator context.
     * @param module The module to which the template should be added.
     * @param project The current project metadata.
     */
    private void addFunctionLambdaRuntime(GeneratorContext generatorContext, ModuleContext module, Project project) {
        String functionLambdaRuntime = generatorContext.getSourcePath("/{packagePath}/FunctionLambdaRuntime");
        module.addTemplate(generatorContext.getOptions().language(), "functionLambdaRuntime", functionLambdaRuntime,
            functionLambdaRuntimeJava.template(generatorContext.getFeatures(), project),
            functionLambdaRuntimeKotlin.template(generatorContext.getFeatures(), project),
            functionLambdaRuntimeGroovy.template(generatorContext.getFeatures(), project));
    }

    @Override
    public String getCategory() {
        return Category.SERVERLESS;
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        List<String> recipes = new ArrayList<>();
        if (generatorContext.getFeatures().testFramework().isSpock()
            && OptionUtils.hasGradleBuildTool(generatorContext.getOptions())) {
            // maven has this in parent pom
            recipes.add("io.micronaut.starter.feature.micronaut-function-test");
        }
        recipes.add("io.micronaut.starter.feature.aws-lambda-custom-runtime");
        return recipes;
    }

}
