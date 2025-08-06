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
package io.micronaut.starter.feature.lang.groovy;

import com.fizzed.rocker.RockerModel;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.feature.GroovyApplicationFeature;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.Project;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.micronaut.features.logging.Slf4jJulBridge;
import io.micronaut.projectgen.micronaut.template.lang.groovy.application;
import io.micronaut.starter.feature.RequireEagerSingletonInitializationFeature;
import io.micronaut.starter.feature.database.TransactionalNotSupported;
import io.micronaut.starter.feature.function.FunctionFeature;
import io.micronaut.projectgen.micronaut.template.test.groovyJunit;
import io.micronaut.projectgen.micronaut.template.test.koTest;
import io.micronaut.projectgen.micronaut.template.test.spock;
import io.micronaut.starter.options.DefaultTestRockerModelProvider;
import io.micronaut.projectgen.core.options.TestFramework;
import io.micronaut.projectgen.core.rocker.TestRockerModelProvider;
import io.micronaut.projectgen.core.rocker.RockerTemplate;
import jakarta.inject.Singleton;

/**
 * Feature that generates a Groovy-based application entry point for supported Micronaut application types.
 * Provides Groovy-specific application and test templates, and configures the main class accordingly.
 */
@Requires(property = "micronaut.starter.feature.groovy.application.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class GroovyApplication implements GroovyApplicationFeature {

    @Override
    @Nullable
    public String mainClassName(GeneratorContext generatorContext) {
        return generatorContext.getProject().getPackageName() + ".Application";
    }

    @Override
    public String getName() {
        return "groovy-application";
    }

    @Override
    public boolean supports(Options options) {
        ApplicationType applicationType = ApplicationType.of(options.template());
        return applicationType != ApplicationType.CLI && applicationType != ApplicationType.FUNCTION;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        GroovyApplicationFeature.super.apply(generatorContext);
        ModuleContext module = generatorContext.getRootModule();
        if (shouldGenerateApplicationFile(generatorContext)) {
            addApplication(generatorContext, module);
            addApplicationTest(generatorContext, module);
        }
    }

    /**
     * Determines whether an application file should be generated based on the provided GeneratorContext.
     * The decision is made based on the application type and the presence of the FunctionFeature.
     *
     * @param generatorContext the context used to generate the project
     * @return true if an application file should be generated, false otherwise
     */
    protected boolean shouldGenerateApplicationFile(GeneratorContext generatorContext) {
        ApplicationType applicationType = ApplicationType.of(generatorContext.getOptions().template());
        return applicationType == ApplicationType.DEFAULT
            || !generatorContext.getFeatures().hasFeature(FunctionFeature.class);
    }

    /**
     * Generates a RockerModel for the application based on the provided GeneratorContext and ModuleContext.
     * The generated RockerModel is used to render the application template.
     *
     * @param generatorContext the context used to generate the project
     * @param module the module context containing information about the module being generated
     * @return a RockerModel representing the application template
     */
    protected RockerModel application(GeneratorContext generatorContext, ModuleContext module) {
        String defaultEnvironment = getDefaultEnvironment(module);
        boolean eagerInitSingleton = generatorContext.getFeatures().isFeaturePresent(RequireEagerSingletonInitializationFeature.class);
        return application.template(
            generatorContext.getProject(),
            generatorContext.getFeatures(),
            new GroovyApplicationRenderingContext(defaultEnvironment, eagerInitSingleton),
            generatorContext.hasFeature(Slf4jJulBridge.class)
        );
    }

    private static String getDefaultEnvironment(ModuleContext module) {
        return module.hasConfigurationByEnvironment(Environment.DEVELOPMENT) ? Environment.DEVELOPMENT : null;
    }

    /**
     * Adds the application template to the module context.
     *
     * This method generates a RockerModel for the application based on the provided GeneratorContext and ModuleContext,
     * and adds it to the module context as a template named "application".
     *
     * @param generatorContext the context used to generate the project
     * @param module the module context containing information about the module being generated
     */
    protected void addApplication(GeneratorContext generatorContext, ModuleContext module) {
        module.addTemplate("application", new RockerTemplate(getPath(),
            application(generatorContext, module)));
    }

    /**
     * Adds an application test template to the module context.
     *
     * This method generates a test source path based on the provided GeneratorContext,
     * creates a RockerModel for the application test using the applicationTest method,
     * and adds it to the module context as a template named "applicationTest".
     *
     * @param generatorContext the context used to generate the project
     * @param module the module context containing information about the module being generated
     */
    protected void addApplicationTest(GeneratorContext generatorContext, ModuleContext module) {
        String testSourcePath = generatorContext.getTestSourcePath("/{packagePath}/{className}");
        module.addTemplate("applicationTest",
            new RockerTemplate(testSourcePath, applicationTest(generatorContext))
        );
    }

    /**
     * Generates a RockerModel for the application test based on the provided GeneratorContext.
     * The generated RockerModel is used to render the application test template.
     * The test framework and project information are retrieved from the GeneratorContext,
     * and used to determine the appropriate test template to use.
     *
     * @param generatorContext the context used to generate the project
     * @return a RockerModel representing the application test template
     */
    protected RockerModel applicationTest(GeneratorContext generatorContext) {
        TestFramework testFramework = generatorContext.getTestFramework();
        Project project = generatorContext.getProject();
        boolean transactional = !generatorContext.getFeatures().hasFeature(TransactionalNotSupported.class);
        TestRockerModelProvider provider = new DefaultTestRockerModelProvider(spock.template(project, transactional),
            groovyJunit.template(project, transactional),
            groovyJunit.template(project, transactional),
            groovyJunit.template(project, transactional),
            koTest.template(project, transactional));
        return provider.findModel(generatorContext.getLanguage(), testFramework);
    }

    /**
     * Returns the file path for the Groovy application class.
     *
     * The path is in the format "src/main/groovy/{packagePath}/Application.groovy",
     * where "{packagePath}" is a placeholder for the actual package path.
     *
     * @return the file path for the Groovy application class
     */
    protected String getPath() {
        return "src/main/groovy/{packagePath}/Application.groovy";
    }
}
