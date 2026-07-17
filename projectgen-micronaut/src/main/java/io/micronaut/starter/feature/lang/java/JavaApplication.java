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
package io.micronaut.starter.feature.lang.java;

import com.fizzed.rocker.RockerModel;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.feature.JavaApplicationFeature;
import io.micronaut.projectgen.core.generator.ContextFactory;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.Project;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.micronaut.features.logging.Slf4jJulBridge;
import io.micronaut.projectgen.micronaut.template.lang.java.application;
import io.micronaut.starter.feature.RequireEagerSingletonInitializationFeature;
import io.micronaut.starter.feature.database.TransactionalNotSupported;
import io.micronaut.starter.feature.function.FunctionFeature;
import io.micronaut.projectgen.micronaut.template.test.javaJunit;
import io.micronaut.projectgen.micronaut.template.test.koTest;
import io.micronaut.projectgen.micronaut.template.test.spock;
import io.micronaut.starter.options.DefaultTestRockerModelProvider;
import io.micronaut.projectgen.core.options.TestFramework;
import io.micronaut.projectgen.core.rocker.TestRockerModelProvider;
import io.micronaut.projectgen.core.rocker.RockerTemplate;

import jakarta.inject.Singleton;

/**
 * Represents the Java application feature that can be applied during project generation.
 */
@Requires(property = "micronaut.starter.feature.java.application.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class JavaApplication implements JavaApplicationFeature {

    private final ContextFactory contextFactory;

    public JavaApplication(ContextFactory contextFactory) {
        this.contextFactory = contextFactory;
    }

    @Override
    @Nullable
    public String mainClassName(GeneratorContext generatorContext) {
        return generatorContext.getProject().getPackageName() + ".Application";
    }

    @Override
    public String getName() {
        return "java-application";
    }

    @Override
    public boolean supports(Options options) {
        ApplicationType type = ApplicationType.of(options.template());
        return type != ApplicationType.CLI && type != ApplicationType.FUNCTION;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        JavaApplicationFeature.super.apply(generatorContext);
        ModuleContext module = generatorContext.getRootModule();
        if (shouldGenerateApplicationFile(generatorContext)) {
            addApplication(generatorContext, module);
            addApplicationTest(generatorContext, module);
        }
    }

    /**
     * Determines whether an application file should be generated based on the provided GeneratorContext.
     *
     * The decision is made based on the application type and the presence of the FunctionFeature.
     *
     * @param generatorContext the context used to generate the project
     * @return true if an application file should be generated, false otherwise
     */
    protected boolean shouldGenerateApplicationFile(GeneratorContext generatorContext) {
        ApplicationType type = ApplicationType.of(generatorContext.getOptions().template());
        return type == ApplicationType.DEFAULT
            || !generatorContext.getFeatures().hasFeature(FunctionFeature.class);
    }

    /**
     * Adds the application template to the given ModuleContext.
     *
     * The application template is generated based on the provided GeneratorContext and ModuleContext.
     * The template is added with the name "application" and is located at the path specified by the getPath() method.
     *
     * @param generatorContext the context used to generate the project
     * @param module the module context to which the application template is added
     */
    protected void addApplication(GeneratorContext generatorContext, ModuleContext module) {
        module.addTemplate("application", new RockerTemplate(getPath(),
            application(generatorContext, module)));
    }

    /**
     * Generates the RockerModel for the application template based on the provided GeneratorContext and ModuleContext.
     *
     * The RockerModel is generated using the application template, passing in the project, features,
     * JavaApplicationRenderingContext, and whether the Slf4jJulBridge feature is present.
     *
     * The JavaApplicationRenderingContext is constructed with the default environment and whether
     * eager initialization of singletons is required.
     *
     * @param generatorContext the context used to generate the project
     * @param module the module context used to determine the default environment
     * @return the RockerModel for the application template
     */
    protected RockerModel application(GeneratorContext generatorContext, ModuleContext module) {
        String defaultEnvironment = getDefaultEnvironment(module);
        boolean eagerInitSingleton = generatorContext.getFeatures().isFeaturePresent(RequireEagerSingletonInitializationFeature.class);
        return application.template(
            generatorContext.getProject(),
            generatorContext.getFeatures(),
            new JavaApplicationRenderingContext(defaultEnvironment, eagerInitSingleton),
            generatorContext.hasFeature(Slf4jJulBridge.class)
        );
    }

    private static @Nullable String getDefaultEnvironment(ModuleContext module) {
        return module.hasConfigurationByEnvironment(Environment.DEVELOPMENT) ? Environment.DEVELOPMENT : null;
    }

    /**
     * Adds an application test template to the given ModuleContext.
     *
     * The application test template is generated based on the provided GeneratorContext.
     * The template is added with the name "applicationTest" and is located at the path specified by the
     * getTestSourcePath() method of the GeneratorContext.
     *
     * @param generatorContext the context used to generate the project
     * @param module the module context to which the application test template is added
     */
    protected void addApplicationTest(GeneratorContext generatorContext, ModuleContext module) {
        String testSourcePath = generatorContext.getTestSourcePath("/{packagePath}/{className}");
        module.addTemplate("applicationTest",
            new RockerTemplate(testSourcePath, applicationTest(generatorContext)));
    }

    /**
     * Generates the RockerModel for the application test template based on the provided GeneratorContext.
     *
     * The RockerModel is generated using a TestRockerModelProvider, which is constructed with various test templates.
     * The test framework and project are retrieved from the GeneratorContext, and the transactional flag is determined
     * based on the presence of the TransactionalNotSupported feature.
     *
     * The TestRockerModelProvider is then used to find the appropriate RockerModel based on the language and test framework.
     *
     * @param generatorContext the context used to generate the project
     * @return the RockerModel for the application test template
     */
    protected RockerModel applicationTest(GeneratorContext generatorContext) {
        TestFramework testFramework = generatorContext.getTestFramework();
        Project project = generatorContext.getProject();
        boolean transactional = !generatorContext.getFeatures().hasFeature(TransactionalNotSupported.class);
        TestRockerModelProvider provider = new DefaultTestRockerModelProvider(spock.template(project, transactional),
            javaJunit.template(project, transactional),
            javaJunit.template(project, transactional),
            javaJunit.template(project, transactional),
            koTest.template(project, transactional));
        return provider.findModel(generatorContext.getLanguage(), testFramework);
    }

    /**
     * Returns the path where the application Java file will be generated.
     *
     * The path is in the format "src/main/java/{packagePath}/Application.java", where {packagePath} is a placeholder
     * that will be replaced with the actual package path during template rendering.
     *
     * @return the path to the application Java file
     */
    protected String getPath() {
        return "src/main/java/{packagePath}/Application.java";
    }
}
