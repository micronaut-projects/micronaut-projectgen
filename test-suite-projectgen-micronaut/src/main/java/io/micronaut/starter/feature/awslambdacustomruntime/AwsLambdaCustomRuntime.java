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
import io.micronaut.projectgen.core.options.GenericOptionsBuilder;
import io.micronaut.projectgen.core.rocker.RockerWritable;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.Project;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
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

@Requires(property = "micronaut.starter.feature.aws.lambda.custom.runtime.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class AwsLambdaCustomRuntime implements FunctionFeature, ApplicationFeature, AwsCloudFeature {
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

    @SuppressWarnings("EmptyBlock")
    @Override
    public void apply(GeneratorContext generatorContext) {
        ApplicationFeature.super.apply(generatorContext);
        ModuleContext module = generatorContext.getRootModule();
        Project project = generatorContext.getProject();
        if (shouldGenerateMainClassForRuntime(generatorContext)) {
            addFunctionLambdaRuntime(generatorContext, module, project);
        }

        if (generatorContext.getFeatures().isFeaturePresent(GraalVM.class)) {
            module.addHelpTemplate(new RockerWritable(awsCustomRuntimeReadme.template(generatorContext.getBuildTool())));
        }
        addDependencies(generatorContext, module);
    }

    private void addDependencies(@NonNull GeneratorContext generatorContext, ModuleContext module) {
        module.addDependency(DEPENDENCY_AWS_FUNCTION_AWS_CUSTOM_RUNTIME);
        if (generatorContext.getFeatures().testFramework().isSpock() &&
                OptionUtils.hasGradleBuildTool(generatorContext.getOptions())) {
            // maven has this in parent pom
            module.addDependency(AwsLambda.DEPENDENCY_MICRONAUT_FUNCTION_TEST);
        }
    }

    public boolean shouldGenerateMainClassForRuntime(GeneratorContext generatorContext) {
        ApplicationType applicationType = ApplicationType.of(generatorContext.getOptions().template());
        return applicationType == ApplicationType.FUNCTION &&
                generatorContext.getFeatures().isFeaturePresent(AwsLambda.class);
    }

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
    public String getFrameworkDocumentation(GeneratorContext generatorContext) {
        return "https://micronaut-projects.github.io/micronaut-aws/latest/guide/index.html#lambdaCustomRuntimes";
    }

    @Override
    public String getThirdPartyDocumentation(GeneratorContext generatorContext) {
        return "https://docs.aws.amazon.com/lambda/latest/dg/runtimes-custom.html";
    }
}
