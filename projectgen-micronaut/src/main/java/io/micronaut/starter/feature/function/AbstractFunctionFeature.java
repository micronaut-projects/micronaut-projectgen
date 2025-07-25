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
package io.micronaut.starter.feature.function;

import com.fizzed.rocker.RockerModel;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.rocker.RockerWritable;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.Project;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.starter.feature.MicronautRuntimeFeature;
import io.micronaut.starter.feature.chatbots.ChatBotsFeature;
import io.micronaut.projectgen.micronaut.template.function.http.httpFunctionGroovyController;
import io.micronaut.projectgen.micronaut.template.function.http.httpFunctionJavaController;
import io.micronaut.projectgen.micronaut.template.function.http.httpFunctionKotlinController;
import io.micronaut.starter.feature.json.SerializationFeature;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.starter.options.DefaultTestRockerModelProvider;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.rocker.TestRockerModelProvider;
import io.micronaut.projectgen.core.rocker.RockerTemplate;

import java.util.Optional;

/**
 * Abstract function implementation.
 *
 * @author graemerocher
 * @since 1.0.0
 */
public abstract class AbstractFunctionFeature implements FunctionFeature, MicronautRuntimeFeature {

    @Override
    public void apply(GeneratorContext generatorContext) {
        ApplicationType applicationType = ApplicationType.of(generatorContext.getOptions().template());
        applyFunction(generatorContext, applicationType);
        addMicronautRuntimeBuildProperty(generatorContext);
    }

    /**
     * Returns the Java function controller template.
     * Subclasses may override to customize the template.
     *
     * @param project   the project context
     * @param useSerde  whether Serde serialization is enabled
     * @return the Rocker template for the Java controller
     */
    protected RockerModel javaControllerTemplate(Project project, boolean useSerde) {
        return httpFunctionJavaController.template(project, useSerde);
    }

    /**
     * Returns the Kotlin function controller template.
     * Subclasses may override to customize the template.
     *
     * @param project   the project context
     * @param useSerde  whether Serde serialization is enabled
     * @return the Rocker template for the Kotlin controller
     */
    protected RockerModel kotlinControllerTemplate(Project project, boolean useSerde) {
        return httpFunctionKotlinController.template(project, useSerde);
    }

    /**
     * Returns the Groovy function controller template.
     * Subclasses may override to customize the template.
     *
     * @param project   the project context
     * @param useSerde  whether Serde serialization is enabled
     * @return the Rocker template for the Groovy controller
     */
    protected RockerModel groovyControllerTemplate(Project project, boolean useSerde) {
        return httpFunctionGroovyController.template(project, useSerde);
    }

    /**
     * Applies the function setup based on the application type.
     * Subclasses may override to customize how function templates and dependencies are applied.
     *
     * @param generatorContext the generator context
     * @param type             the application type
     */
    protected void applyFunction(GeneratorContext generatorContext, ApplicationType type) {
        ModuleContext module = generatorContext.getRootModule();
        BuildTool buildTool = generatorContext.getOptions().getBuildTool();

        if (generatorContext.isFeatureMissing(ChatBotsFeature.class)) {
            readmeTemplate(generatorContext, generatorContext.getProject(), buildTool)
                .ifPresent(rockerModel -> module.addHelpTemplate(new RockerWritable(rockerModel)));
        }

        if (type == ApplicationType.DEFAULT) {

            final String className = StringUtils.capitalize(generatorContext.getProject().getClassName());
            Project project = generatorContext.getProject().withClassName(className);

            Language language = generatorContext.getLanguage();
            String sourceFile = generatorContext.getSourcePath("/{packagePath}/" + className + "Controller");

            boolean serdeFeaturePresent = generatorContext.isFeaturePresent(SerializationFeature.class);
            switch (language) {
                case GROOVY:
                    module.addTemplate("function", new RockerTemplate(
                        sourceFile,
                        groovyControllerTemplate(project, serdeFeaturePresent)));
                    break;
                case KOTLIN:
                    module.addTemplate("function", new RockerTemplate(
                        sourceFile,
                        kotlinControllerTemplate(project, serdeFeaturePresent)));
                    break;
                case JAVA:
                default:
                    module.addTemplate("function", new RockerTemplate(
                        sourceFile,
                        javaControllerTemplate(project, serdeFeaturePresent)));
                    break;
            }

            applyTestTemplate(generatorContext, project, className + getTestSuffix(type));
        }
    }

    /**
     * Returns the suffix used for function test class names.
     * Subclasses may override to provide custom suffixes.
     *
     * @param type the application type
     * @return the test class name suffix
     */
    protected String getTestSuffix(ApplicationType type) {
        return "Function";
    }

    /**
     * Applies the test templates to the module.
     * Subclasses may override to customize the test generation logic.
     *
     * @param generatorContext the generator context
     * @param project          the project
     * @param name             the name of the test class
     */
    protected void applyTestTemplate(GeneratorContext generatorContext, Project project, String name) {
        ModuleContext module = generatorContext.getRootModule();
        String testSource = generatorContext.getTestSourcePath("/{packagePath}/" + name);
        TestRockerModelProvider provider = new DefaultTestRockerModelProvider(spockTemplate(project),
            javaJUnitTemplate(project),
            groovyJUnitTemplate(project),
            kotlinJUnitTemplate(project),
            koTestTemplate(project));
        module.addTemplate(generatorContext.getOptions(), "testFunction", testSource, provider);
    }

    /**
     * Returns the README template for this function feature, if any.
     * Subclasses may override to provide custom documentation.
     *
     * @param generatorContext the generator context
     * @param project          the project
     * @param buildTool        the build tool used
     * @return an optional Rocker model for the README
     */
    protected Optional<RockerModel> readmeTemplate(GeneratorContext generatorContext, Project project, BuildTool buildTool) {
        return Optional.empty();
    }

    protected abstract String getRunCommand(BuildTool buildTool);

    protected abstract String getBuildCommand(BuildTool buildTool);

    protected abstract RockerModel javaJUnitTemplate(Project project);

    protected abstract RockerModel kotlinJUnitTemplate(Project project);

    protected abstract RockerModel groovyJUnitTemplate(Project project);

    protected abstract RockerModel koTestTemplate(Project project);

    public abstract RockerModel spockTemplate(Project project);
}
