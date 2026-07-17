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
package io.micronaut.starter.feature.function.awslambda;

import com.fizzed.rocker.RockerModel;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.feature.config.Configuration;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.options.GenericOptionsBuilder;
import io.micronaut.projectgen.core.rocker.RockerWritable;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.core.options.TestFramework;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.Project;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.micronaut.gradle.ShadePlugin;
import io.micronaut.starter.buildtools.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.CodeContributingFeature;
import io.micronaut.projectgen.core.feature.DefaultFeature;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.starter.feature.architecture.CpuArchitecture;
import io.micronaut.starter.feature.architecture.X86;
import io.micronaut.starter.feature.aws.AwsApiFeature;
import io.micronaut.starter.feature.aws.AwsCloudFeature;
import io.micronaut.starter.feature.aws.AwsLambdaEventFeature;
import io.micronaut.starter.feature.aws.AwsLambdaEventsSerde;
import io.micronaut.starter.feature.aws.AwsLambdaSnapstart;
import io.micronaut.starter.feature.aws.AwsMicronautRuntimeFeature;
import io.micronaut.starter.feature.awslambdacustomruntime.AwsLambdaCustomRuntime;
import io.micronaut.starter.feature.function.CloudFeature;
import io.micronaut.starter.feature.function.DocumentationLink;
import io.micronaut.starter.feature.function.FunctionFeature;
import io.micronaut.starter.feature.function.HandlerClassFeature;
import io.micronaut.projectgen.micronaut.template.function.awslambda.awsLambdaFunctionRequestHandlerGroovy;
import io.micronaut.projectgen.micronaut.template.function.awslambda.awsLambdaFunctionRequestHandlerGroovyJunit;
import io.micronaut.projectgen.micronaut.template.function.awslambda.awsLambdaFunctionRequestHandlerJava;
import io.micronaut.projectgen.micronaut.template.function.awslambda.awsLambdaFunctionRequestHandlerJavaJunit;
import io.micronaut.projectgen.micronaut.template.function.awslambda.awsLambdaFunctionRequestHandlerKoTest;
import io.micronaut.projectgen.micronaut.template.function.awslambda.awsLambdaFunctionRequestHandlerKotlin;
import io.micronaut.projectgen.micronaut.template.function.awslambda.awsLambdaFunctionRequestHandlerKotlinJunit;
import io.micronaut.projectgen.micronaut.template.function.awslambda.awsLambdaFunctionRequestHandlerSpock;
import io.micronaut.projectgen.micronaut.template.function.awslambda.homeControllerGroovy;
import io.micronaut.projectgen.micronaut.template.function.awslambda.homeControllerGroovyJunit;
import io.micronaut.projectgen.micronaut.template.function.awslambda.homeControllerJava;
import io.micronaut.projectgen.micronaut.template.function.awslambda.homeControllerJavaJunit;
import io.micronaut.projectgen.micronaut.template.function.awslambda.homeControllerKoTest;
import io.micronaut.projectgen.micronaut.template.function.awslambda.homeControllerKotlin;
import io.micronaut.projectgen.micronaut.template.function.awslambda.homeControllerKotlinJunit;
import io.micronaut.projectgen.micronaut.template.function.awslambda.homeControllerSpock;
import io.micronaut.starter.feature.graalvm.GraalVM;
import io.micronaut.projectgen.micronaut.features.httpclient.HttpClientFeature;
import io.micronaut.projectgen.micronaut.features.httpclient.HttpClientJdk;
import io.micronaut.starter.feature.security.SecurityFeature;
import io.micronaut.starter.options.DefaultTestRockerModelProvider;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.rocker.TestRockerModelProvider;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static io.micronaut.projectgen.micronaut.ApplicationType.DEFAULT;
import static io.micronaut.projectgen.micronaut.ApplicationType.FUNCTION;
import static io.micronaut.starter.feature.crac.Crac.DEPENDENCY_MICRONAUT_CRAC;

/**
 * Provides support for AWS Lambda functions in Micronaut applications.
 * <p>
 * This feature configures necessary dependencies, handler classes, and integration
 * with AWS Lambda runtime and tools such as GraalVM native image support,
 * custom runtimes, SnapStart optimization, and HTTP client setup.
 * <p>
 * It selectively adds features based on the application type, build tools,
 * and presence of other features like GraalVM and HTTP client.
 */
@Requires(property = "micronaut.starter.feature.aws.lambda.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class AwsLambda implements FunctionFeature, DefaultFeature, AwsCloudFeature, AwsMicronautRuntimeFeature {

    public static final String FEATURE_NAME_AWS_LAMBDA = "aws-lambda";
    public static final String REQUEST_HANDLER = "FunctionRequestHandler";
    public static final Dependency DEPENDENCY_MICRONAUT_FUNCTION_TEST = MicronautDependencyUtils.coreDependency()
        .artifactId("micronaut-function")
        .test()
        .build();

    private static final String LINK_TITLE = "AWS Lambda Handler";
    private static final String LINK_URL = "https://docs.aws.amazon.com/lambda/latest/dg/java-handler.html";
    private static final Dependency AWS_LAMBDA_JAVA_EVENTS = Dependency.builder()
        .groupId("com.amazonaws")
        .artifactId("aws-lambda-java-events")
        .compile()
        .build();
    private static final Dependency DEPENDENCY_MICRONAUT_FUNCTION_AWS = MicronautDependencyUtils.awsDependency()
        .artifactId("micronaut-function-aws")
        .compile()
        .build();

    private static final Dependency DEPENDENCY_MICRONAUT_FUNCTION_AWS_API_PROXY = MicronautDependencyUtils.awsDependency()
        .artifactId("micronaut-function-aws-api-proxy")
        .compile()
        .build();

    private static final Dependency DEPENDENCY_MICRONAUT_FUNCTION_AWS_API_PROXY_TEST = MicronautDependencyUtils.awsDependency()
        .artifactId("micronaut-function-aws-api-proxy-test")
        .compile()
        .build();

    private final ShadePlugin shadePlugin;
    private final AwsLambdaCustomRuntime customRuntime;
    private final CpuArchitecture defaultCpuArchitecture;
    private final AwsLambdaSnapstart snapstart;
    private final HttpClientJdk httpClientJdk;

    private final AwsLambdaEventsSerde awsLambdaEventsSerde;

    private final HandlerClassFeature defaultAwsLambdaHandlerProvider;
    private final HandlerClassFeature functionAwsLambdaHandlerProvider;

    @Inject
    public AwsLambda(ShadePlugin shadePlugin,
        AwsLambdaCustomRuntime customRuntime,
        X86 x86,
        AwsLambdaSnapstart snapstart,
        HttpClientJdk httpClientJdk,
        AwsLambdaEventsSerde awsLambdaEventsSerde,
        DefaultAwsLambdaHandlerProvider defaultAwsLambdaHandlerProvider,
        FunctionAwsLambdaHandlerProvider functionAwsLambdaHandlerProvider) {
        this.shadePlugin = shadePlugin;
        this.customRuntime = customRuntime;
        this.defaultCpuArchitecture = x86;
        this.snapstart = snapstart;
        this.httpClientJdk = httpClientJdk;
        this.awsLambdaEventsSerde = awsLambdaEventsSerde;
        this.defaultAwsLambdaHandlerProvider = defaultAwsLambdaHandlerProvider;
        this.functionAwsLambdaHandlerProvider = functionAwsLambdaHandlerProvider;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        ApplicationType applicationType = ApplicationType.of(featureContext.getOptions().template());
        Stream.of(defaultAwsLambdaHandlerProvider, functionAwsLambdaHandlerProvider)
            .filter(f -> f.supports(GenericOptionsBuilder.builder().template(applicationType.toString()).build()))
            .findFirst()
            .ifPresent(f -> featureContext.addFeatureIfNotPresent(HandlerClassFeature.class, f));

        featureContext.addFeatureIfNotPresent(ShadePlugin.class, shadePlugin);
        featureContext.addFeatureIfNotPresent(CpuArchitecture.class, defaultCpuArchitecture);
        if (featureContext.isPresent(GraalVM.class)
            && (
            OptionUtils.hasMavenBuildTool(featureContext.getOptions())
                || (OptionUtils.hasGradleBuildTool(featureContext.getOptions()) && applicationType == FUNCTION)
        )
        ) {
            featureContext.addFeature(customRuntime);
        }

        if (featureContext.isPresent(GraalVM.class) && !featureContext.isPresent(HttpClientFeature.class)) {
            featureContext.addFeature(httpClientJdk);
        }

        if (shouldAddSnapstartFeature(featureContext)) {
            featureContext.addFeature(snapstart);
        }
//        if (featureContext.isPresent(SerializationJacksonFeature.class)) {
//            featureContext.addFeature(awsLambdaEventsSerde);
//        }
    }

    /**
     * Determines if the SnapStart feature should be added based on the current feature context.
     * Subclasses may override to change the default behavior.
     * @param featureContext The feature context to evaluate.
     * @return {@code true} if SnapStart should be added; {@code false} otherwise.
     */
    protected boolean shouldAddSnapstartFeature(FeatureContext featureContext) {
        if (featureContext.isPresent(GraalVM.class)) {
            return false;
        }
        return featureContext.getFeature(CpuArchitecture.class)
            .filter(CpuArchitecture.class::isInstance)
            .map(CpuArchitecture.class::cast)
            .map(snapstart::supports)
            .orElse(true);
    }

    @Override
    @NonNull
    public String getName() {
        return FEATURE_NAME_AWS_LAMBDA;
    }

    @Override
    public String getTitle() {
        return "AWS Lambda Function";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds support for writing functions to deploy to AWS Lambda";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        ModuleContext module = generatorContext.getRootModule();
        if (generatorContext.isFeatureMissing(CodeContributingFeature.class)) {
            ApplicationType applicationType = ApplicationType.of(generatorContext.getOptions().template());
            if (applicationType == DEFAULT || applicationType == FUNCTION) {
                addCode(generatorContext, module);
                if (applicationType == FUNCTION) {
                    module.addDependency(AWS_LAMBDA_JAVA_EVENTS);
                }
                addHelpTemplate(generatorContext, module);
                disableSecurityFilterInTestConfiguration(generatorContext, module);
            }
        }
        addMicronautRuntimeBuildProperty(generatorContext);
        addDependencies(module, generatorContext);
    }

    private void addDependencies(@NonNull ModuleContext module, GeneratorContext generatorContext) {
        ApplicationType applicationType = ApplicationType.of(generatorContext.getOptions().template());
        if (applicationType == ApplicationType.FUNCTION) {
            module.addDependency(DEPENDENCY_MICRONAUT_FUNCTION_AWS);
        }
        if (OptionUtils.hasMavenBuildTool(generatorContext.getOptions()) && applicationType == DEFAULT) {
            module.addDependency(DEPENDENCY_MICRONAUT_FUNCTION_AWS_API_PROXY);
            module.addDependency(DEPENDENCY_MICRONAUT_FUNCTION_AWS_API_PROXY_TEST);
        }
        if (OptionUtils.hasMavenBuildTool(generatorContext.getOptions()) && generatorContext.hasFeature(GraalVM.class)) {
            module.addDependency(AwsLambdaCustomRuntime.DEPENDENCY_AWS_FUNCTION_AWS_CUSTOM_RUNTIME);
        }

        if (generatorContext.hasFeature(AwsLambdaSnapstart.class)) {
            module.addDependency(DEPENDENCY_MICRONAUT_CRAC);
        }

        if (generatorContext.getTestFramework() == TestFramework.SPOCK
            && OptionUtils.hasGradleBuildTool(generatorContext.getOptions())) {
            // maven has this in parent pom
            module.addDependency(DEPENDENCY_MICRONAUT_FUNCTION_TEST);
        }
    }

    /**
     * Adds generated source code files (e.g. controller or handler) to the module based on application type.
     * Subclasses may override to customize generated code.
     * @param generatorContext The generator context.
     * @param module The module context where code will be added.
     */
    protected void addCode(@NonNull GeneratorContext generatorContext, ModuleContext module) {
        Project project = generatorContext.getProject();
        ApplicationType applicationType = ApplicationType.of(generatorContext.getOptions().template());
        if (applicationType == DEFAULT) {
            addHomeController(generatorContext, module, project);
            addHomeControllerTest(generatorContext, module, project);
        } else if (applicationType == FUNCTION) {
            addRequestHandler(generatorContext, module, project);
            if (generatorContext.getFeatures().hasFeature(AwsApiFeature.class)
                || !generatorContext.getFeatures().hasFeature(AwsLambdaEventFeature.class)) {
                addTest(generatorContext, module, project);
            }
        }
    }

    /**
     * Adds the help template (e.g. README) to the module if applicable.
     * Subclasses may override to customize documentation generation.
     * @param generatorContext The generator context.
     * @param module The module context to which the help template is added.
     */
    protected void addHelpTemplate(@NonNull GeneratorContext generatorContext, ModuleContext module) {
        readmeTemplate(generatorContext)
            .ifPresent(rockerModel -> module.addHelpTemplate(new RockerWritable(rockerModel)));
    }

    /**
     * Provides the README template for this feature, if available.
     * Subclasses may override to provide different documentation.
     *  @param generatorContext The generator context.
     *  @return An Optional containing the RockerModel for the README template, if present.
     */
    @NonNull
    public Optional<RockerModel> readmeTemplate(@NonNull GeneratorContext generatorContext) {
        DocumentationLink link = new DocumentationLink(LINK_TITLE, LINK_URL);
        return generatorContext.getFeature(HandlerClassFeature.class)
            .map(f -> HandlerClassFeature.readmeRockerModel(f, generatorContext, link));
    }

    /**
     * Disables the security filter in test configuration if security is enabled.
     * Subclasses may override to alter test environment configuration.
     * @param generatorContext the generator context
     * @param module the module context
     */
    protected void disableSecurityFilterInTestConfiguration(@NonNull GeneratorContext generatorContext, ModuleContext module) {
        if (generatorContext.getFeatures().hasFeature(SecurityFeature.class)) {
            Configuration test = module.testConfiguration();
            test.put("micronaut.security.filter.enabled", false);
        }
    }

    private void addHomeControllerTest(GeneratorContext generatorContext, ModuleContext module, Project project) {
        String testSource = generatorContext.getTestSourcePath("/{packagePath}/HomeController");
        String handler = HandlerClassFeature.resolveHandler(generatorContext);
        TestRockerModelProvider provider = new DefaultTestRockerModelProvider(homeControllerSpock.template(project, handler),
            homeControllerJavaJunit.template(project, handler),
            homeControllerGroovyJunit.template(project, handler),
            homeControllerKotlinJunit.template(project, handler),
            homeControllerKoTest.template(project, handler));
        module.addTemplate(generatorContext.getOptions(), "testHomeController", testSource, provider);
    }

    private void addHomeController(GeneratorContext generatorContext, ModuleContext module, Project project) {
        String controllerFile = generatorContext.getSourcePath("/{packagePath}/HomeController");
        module.addTemplate(generatorContext.getOptions().language(), "homeController", controllerFile,
            homeControllerJava.template(project),
            homeControllerKotlin.template(project),
            homeControllerGroovy.template(project));
    }

    private void addTest(GeneratorContext generatorContext, ModuleContext module, Project project) {
        String testSource = generatorContext.getTestSourcePath("/{packagePath}/FunctionRequestHandler");
        TestRockerModelProvider provider = new DefaultTestRockerModelProvider(awsLambdaFunctionRequestHandlerSpock.template(project),
            awsLambdaFunctionRequestHandlerJavaJunit.template(project),
            awsLambdaFunctionRequestHandlerGroovyJunit.template(project),
            awsLambdaFunctionRequestHandlerKotlinJunit.template(project),
            awsLambdaFunctionRequestHandlerKoTest.template(project));
        module.addTemplate(generatorContext.getOptions(), "testFunctionRequestHandler", testSource, provider);
    }

    private void addRequestHandler(GeneratorContext generatorContext, ModuleContext module, Project project) {
        String awsLambdaRequestHandlerFile = generatorContext.getSourcePath("/{packagePath}/" + REQUEST_HANDLER);
        module.addTemplate(generatorContext.getOptions().language(), "functionRequestHandler", awsLambdaRequestHandlerFile,
            awsLambdaFunctionRequestHandlerJava.template(generatorContext.getFeatures(), project),
            awsLambdaFunctionRequestHandlerKotlin.template(generatorContext.getFeatures(), project),
            awsLambdaFunctionRequestHandlerGroovy.template(generatorContext.getFeatures(), project));
    }

    @Override
    public boolean shouldApply(Options options, Set<Feature> selectedFeatures) {
        ApplicationType applicationType = ApplicationType.of(options.template());
        return applicationType == FUNCTION
            && selectedFeatures.stream().filter(CloudFeature.class::isInstance)
            .noneMatch(cloudFeature -> ((CloudFeature) cloudFeature).getCloud() != getCloud());
    }

    @Override
    public String getCategory() {
        return Category.SERVERLESS;
    }

    @Override
    public String getFrameworkDocumentation(GeneratorContext generatorContext) {
        return "https://micronaut-projects.github.io/micronaut-aws/latest/guide/index.html#lambda";
    }
}
