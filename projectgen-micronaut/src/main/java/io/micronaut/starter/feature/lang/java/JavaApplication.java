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
import io.micronaut.projectgen.javalibs.logging.Slf4jJulBridge;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.Project;
import io.micronaut.projectgen.core.generator.GeneratorContext;
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

    protected boolean shouldGenerateApplicationFile(GeneratorContext generatorContext) {
        ApplicationType type = ApplicationType.of(generatorContext.getOptions().template());
        return type == ApplicationType.DEFAULT
                || !generatorContext.getFeatures().hasFeature(FunctionFeature.class);
    }

    protected void addApplication(GeneratorContext generatorContext, ModuleContext module) {
        module.addTemplate("application", new RockerTemplate(getPath(),
                application(generatorContext, module)));
    }

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

    private static String getDefaultEnvironment(ModuleContext module) {
        return module.hasConfigurationByEnvironment(Environment.DEVELOPMENT) ? Environment.DEVELOPMENT : null;
    }

    protected void addApplicationTest(GeneratorContext generatorContext, ModuleContext module) {
        String testSourcePath = generatorContext.getTestSourcePath("/{packagePath}/{className}");
        module.addTemplate("applicationTest",
                new RockerTemplate(testSourcePath, applicationTest(generatorContext)));
    }

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

    protected String getPath() {
        return "src/main/java/{packagePath}/Application.java";
    }
}
