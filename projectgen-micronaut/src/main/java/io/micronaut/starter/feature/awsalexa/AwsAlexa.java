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
package io.micronaut.starter.feature.awsalexa;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.Project;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.CodeContributingFeature;
import io.micronaut.starter.feature.aws.AwsCloudFeature;
import io.micronaut.projectgen.micronaut.template.awsalexa.cancelIntentHandlerGroovy;
import io.micronaut.projectgen.micronaut.template.awsalexa.cancelIntentHandlerGroovyJunit;
import io.micronaut.projectgen.micronaut.template.awsalexa.cancelIntentHandlerGroovySpock;
import io.micronaut.projectgen.micronaut.template.awsalexa.cancelIntentHandlerJava;
import io.micronaut.projectgen.micronaut.template.awsalexa.cancelIntentHandlerJavaJunit;
import io.micronaut.projectgen.micronaut.template.awsalexa.cancelIntentHandlerKoTest;
import io.micronaut.projectgen.micronaut.template.awsalexa.cancelIntentHandlerKotlin;
import io.micronaut.projectgen.micronaut.template.awsalexa.cancelIntentHandlerKotlinJunit;
import io.micronaut.projectgen.micronaut.template.awsalexa.fallbackIntentHandlerGroovy;
import io.micronaut.projectgen.micronaut.template.awsalexa.fallbackIntentHandlerGroovyJunit;
import io.micronaut.projectgen.micronaut.template.awsalexa.fallbackIntentHandlerGroovySpock;
import io.micronaut.projectgen.micronaut.template.awsalexa.fallbackIntentHandlerJava;
import io.micronaut.projectgen.micronaut.template.awsalexa.fallbackIntentHandlerJavaJunit;
import io.micronaut.projectgen.micronaut.template.awsalexa.fallbackIntentHandlerKoTest;
import io.micronaut.projectgen.micronaut.template.awsalexa.fallbackIntentHandlerKotlin;
import io.micronaut.projectgen.micronaut.template.awsalexa.fallbackIntentHandlerKotlinJunit;
import io.micronaut.projectgen.micronaut.template.awsalexa.helpIntentHandlerGroovy;
import io.micronaut.projectgen.micronaut.template.awsalexa.helpIntentHandlerGroovyJunit;
import io.micronaut.projectgen.micronaut.template.awsalexa.helpIntentHandlerGroovySpock;
import io.micronaut.projectgen.micronaut.template.awsalexa.helpIntentHandlerJava;
import io.micronaut.projectgen.micronaut.template.awsalexa.helpIntentHandlerJavaJunit;
import io.micronaut.projectgen.micronaut.template.awsalexa.helpIntentHandlerKoTest;
import io.micronaut.projectgen.micronaut.template.awsalexa.helpIntentHandlerKotlin;
import io.micronaut.projectgen.micronaut.template.awsalexa.helpIntentHandlerKotlinJunit;
import io.micronaut.projectgen.micronaut.template.awsalexa.launchRequestIntentHandlerGroovy;
import io.micronaut.projectgen.micronaut.template.awsalexa.launchRequestIntentHandlerGroovyJunit;
import io.micronaut.projectgen.micronaut.template.awsalexa.launchRequestIntentHandlerGroovySpock;
import io.micronaut.projectgen.micronaut.template.awsalexa.launchRequestIntentHandlerJava;
import io.micronaut.projectgen.micronaut.template.awsalexa.launchRequestIntentHandlerJavaJunit;
import io.micronaut.projectgen.micronaut.template.awsalexa.launchRequestIntentHandlerKoTest;
import io.micronaut.projectgen.micronaut.template.awsalexa.launchRequestIntentHandlerKotlin;
import io.micronaut.projectgen.micronaut.template.awsalexa.launchRequestIntentHandlerKotlinJunit;
import io.micronaut.projectgen.micronaut.template.awsalexa.sessionEndedRequestIntentHandlerGroovy;
import io.micronaut.projectgen.micronaut.template.awsalexa.sessionEndedRequestIntentHandlerGroovyJunit;
import io.micronaut.projectgen.micronaut.template.awsalexa.sessionEndedRequestIntentHandlerGroovySpock;
import io.micronaut.projectgen.micronaut.template.awsalexa.sessionEndedRequestIntentHandlerJava;
import io.micronaut.projectgen.micronaut.template.awsalexa.sessionEndedRequestIntentHandlerJavaJunit;
import io.micronaut.projectgen.micronaut.template.awsalexa.sessionEndedRequestIntentHandlerKoTest;
import io.micronaut.projectgen.micronaut.template.awsalexa.sessionEndedRequestIntentHandlerKotlin;
import io.micronaut.projectgen.micronaut.template.awsalexa.sessionEndedRequestIntentHandlerKotlinJunit;
import io.micronaut.projectgen.micronaut.template.awsalexa.stopIntentHandlerGroovy;
import io.micronaut.projectgen.micronaut.template.awsalexa.stopIntentHandlerGroovyJunit;
import io.micronaut.projectgen.micronaut.template.awsalexa.stopIntentHandlerGroovySpock;
import io.micronaut.projectgen.micronaut.template.awsalexa.stopIntentHandlerJava;
import io.micronaut.projectgen.micronaut.template.awsalexa.stopIntentHandlerJavaJunit;
import io.micronaut.projectgen.micronaut.template.awsalexa.stopIntentHandlerKoTest;
import io.micronaut.projectgen.micronaut.template.awsalexa.stopIntentHandlerKotlin;
import io.micronaut.projectgen.micronaut.template.awsalexa.stopIntentHandlerKotlinJunit;
import io.micronaut.starter.options.DefaultTestRockerModelProvider;
import io.micronaut.projectgen.core.rocker.TestRockerModelProvider;
import jakarta.inject.Singleton;

import java.util.Collections;
import java.util.List;

@Requires(property = "micronaut.starter.feature.aws.alexa.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class AwsAlexa implements OpenRewriteFeature, AwsCloudFeature, CodeContributingFeature {
    public static final String NAME = "aws-alexa";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Alexa Skills";
    }

    @Override
    public String getDescription() {
        return "Build Alexa Skills with Micronaut";
    }

    @Override
    public boolean supports(Options options) {
        return ApplicationType.of(options.template()) == ApplicationType.FUNCTION
            || ApplicationType.of(options.template()) == ApplicationType.DEFAULT;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        OpenRewriteFeature.super.apply(generatorContext);
        Project project = generatorContext.getProject();

        cancelIntentHandler(generatorContext, project);
        cancelIntentHandlerTest(generatorContext, project);

        fallbackIntentHandler(generatorContext, project);
        fallbackIntentHandlerTest(generatorContext, project);

        helpIntentHandler(generatorContext, project);
        helpIntentHandlerTest(generatorContext, project);

        launchRequestIntentHandler(generatorContext, project);
        launchRequestIntentHandlerTest(generatorContext, project);

        sessionEndedRequestIntentHandler(generatorContext, project);
        sessionEndedIntentHandlerTest(generatorContext, project);

        stopIntentHandler(generatorContext, project);
        stopIntentHandlerTest(generatorContext, project);
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        if (ApplicationType.of(generatorContext.getOptions().template()) == ApplicationType.FUNCTION) {
            return List.of("io.micronaut.starter.feature.aws-alexa-function");
        } else if (ApplicationType.of(generatorContext.getOptions().template()) == ApplicationType.DEFAULT) {
            return List.of("io.micronaut.starter.feature.aws-alexa-default");
        }
        return Collections.emptyList();
    }

    private void cancelIntentHandler(GeneratorContext generatorContext, Project project) {
        String cancelIntentHandler = generatorContext.getSourcePath("/{packagePath}/CancelIntentHandler");
        ModuleContext module = generatorContext.getRootModule();
        module.addTemplate(generatorContext.getLanguage(), "cancelIntentHandler", cancelIntentHandler,
            cancelIntentHandlerJava.template(project),
            cancelIntentHandlerKotlin.template(project),
            cancelIntentHandlerGroovy.template(project));
    }

    private void fallbackIntentHandler(GeneratorContext generatorContext, Project project) {
        String fallbackIntentHandler = generatorContext.getSourcePath("/{packagePath}/FallbackIntentHandler");
        ModuleContext module = generatorContext.getRootModule();
        module.addTemplate(generatorContext.getLanguage(), "fallbackIntentHandler", fallbackIntentHandler,
            fallbackIntentHandlerJava.template(project),
            fallbackIntentHandlerKotlin.template(project),
            fallbackIntentHandlerGroovy.template(project));
    }

    private void helpIntentHandler(GeneratorContext generatorContext, Project project) {
        String helpIntentHandler = generatorContext.getSourcePath("/{packagePath}/HelpIntentHandler");
        ModuleContext module = generatorContext.getRootModule();
        module.addTemplate(generatorContext.getLanguage(), "helpIntentHandler", helpIntentHandler,
            helpIntentHandlerJava.template(project),
            helpIntentHandlerKotlin.template(project),
            helpIntentHandlerGroovy.template(project));
    }

    private void launchRequestIntentHandler(GeneratorContext generatorContext, Project project) {
        String launchRequestIntentHandler = generatorContext.getSourcePath("/{packagePath}/LaunchRequestIntentHandler");
        ModuleContext module = generatorContext.getRootModule();
        module.addTemplate(generatorContext.getLanguage(), "launchRequestIntentHandler", launchRequestIntentHandler,
            launchRequestIntentHandlerJava.template(project),
            launchRequestIntentHandlerKotlin.template(project),
            launchRequestIntentHandlerGroovy.template(project));
    }

    private void sessionEndedRequestIntentHandler(GeneratorContext generatorContext, Project project) {
        String sessionEndedRequestIntentHandler = generatorContext.getSourcePath("/{packagePath}/SessionEndedRequestIntentHandler");
        ModuleContext module = generatorContext.getRootModule();
        module.addTemplate(generatorContext.getLanguage(), "sessionEndedRequestIntentHandler", sessionEndedRequestIntentHandler,
            sessionEndedRequestIntentHandlerJava.template(project),
            sessionEndedRequestIntentHandlerKotlin.template(project),
            sessionEndedRequestIntentHandlerGroovy.template(project));
    }

    private void stopIntentHandler(GeneratorContext generatorContext, Project project) {
        String stopIntentHandler = generatorContext.getSourcePath("/{packagePath}/StopIntentHandler");
        ModuleContext module = generatorContext.getRootModule();
        module.addTemplate(generatorContext.getLanguage(), "stopIntentHandler", stopIntentHandler,
            stopIntentHandlerJava.template(project),
            stopIntentHandlerKotlin.template(project),
            stopIntentHandlerGroovy.template(project));
    }

    private void launchRequestIntentHandlerTest(GeneratorContext generatorContext, Project project) {
        String launchRequestIntentHandlerTest = generatorContext.getTestSourcePath("/{packagePath}/LaunchRequestIntentHandler");
        TestRockerModelProvider provider = new DefaultTestRockerModelProvider(launchRequestIntentHandlerGroovySpock.template(project),
            launchRequestIntentHandlerJavaJunit.template(project),
            launchRequestIntentHandlerGroovyJunit.template(project),
            launchRequestIntentHandlerKotlinJunit.template(project),
            launchRequestIntentHandlerKoTest.template(project));
        ModuleContext module = generatorContext.getRootModule();
        module.addTemplate(generatorContext.getOptions(), "testLaunchRequestIntentHandler", launchRequestIntentHandlerTest, provider);
    }

    private void cancelIntentHandlerTest(GeneratorContext generatorContext, Project project) {
        String cancelIntentHandlerTest = generatorContext.getTestSourcePath("/{packagePath}/CancelIntentHandler");
        TestRockerModelProvider provider = new DefaultTestRockerModelProvider(cancelIntentHandlerGroovySpock.template(project),
            cancelIntentHandlerJavaJunit.template(project),
            cancelIntentHandlerGroovyJunit.template(project),
            cancelIntentHandlerKotlinJunit.template(project),
            cancelIntentHandlerKoTest.template(project));
        ModuleContext module = generatorContext.getRootModule();
        module.addTemplate(generatorContext.getOptions(), "testCancelIntentHandler", cancelIntentHandlerTest, provider);
    }

    private void fallbackIntentHandlerTest(GeneratorContext generatorContext, Project project) {
        String fallbackIntentHandlerTest = generatorContext.getTestSourcePath("/{packagePath}/FallbackIntentHandler");
        TestRockerModelProvider provider = new DefaultTestRockerModelProvider(fallbackIntentHandlerGroovySpock.template(project),
            fallbackIntentHandlerJavaJunit.template(project),
            fallbackIntentHandlerGroovyJunit.template(project),
            fallbackIntentHandlerKotlinJunit.template(project),
            fallbackIntentHandlerKoTest.template(project));
        ModuleContext module = generatorContext.getRootModule();
        module.addTemplate(generatorContext.getOptions(), "testFallbackIntentHandler", fallbackIntentHandlerTest, provider);
    }

    private void helpIntentHandlerTest(GeneratorContext generatorContext, Project project) {
        String helpIntentHandlerTest = generatorContext.getTestSourcePath("/{packagePath}/HelpIntentHandler");
        TestRockerModelProvider provider = new DefaultTestRockerModelProvider(helpIntentHandlerGroovySpock.template(project),
            helpIntentHandlerJavaJunit.template(project),
            helpIntentHandlerGroovyJunit.template(project),
            helpIntentHandlerKotlinJunit.template(project),
            helpIntentHandlerKoTest.template(project));
        ModuleContext module = generatorContext.getRootModule();
        module.addTemplate(generatorContext.getOptions(), "testHelpIntentHandler", helpIntentHandlerTest, provider);
    }

    private void sessionEndedIntentHandlerTest(GeneratorContext generatorContext, Project project) {
        String sessionEndedIntentHandlerTest = generatorContext.getTestSourcePath("/{packagePath}/SessionEndedRequestIntentHandler");
        TestRockerModelProvider provider = new DefaultTestRockerModelProvider(sessionEndedRequestIntentHandlerGroovySpock.template(project),
            sessionEndedRequestIntentHandlerJavaJunit.template(project),
            sessionEndedRequestIntentHandlerGroovyJunit.template(project),
            sessionEndedRequestIntentHandlerKotlinJunit.template(project),
            sessionEndedRequestIntentHandlerKoTest.template(project));
        ModuleContext module = generatorContext.getRootModule();
        module.addTemplate(generatorContext.getOptions(), "testSessionEndedRequestIntentHandler", sessionEndedIntentHandlerTest, provider);

    }

    private void stopIntentHandlerTest(GeneratorContext generatorContext, Project project) {
        String stopIntentHandlerTest = generatorContext.getTestSourcePath("/{packagePath}/StopIntentHandler");
        TestRockerModelProvider provider = new DefaultTestRockerModelProvider(stopIntentHandlerGroovySpock.template(project),
            stopIntentHandlerJavaJunit.template(project),
            stopIntentHandlerGroovyJunit.template(project),
            stopIntentHandlerKotlinJunit.template(project),
            stopIntentHandlerKoTest.template(project));
        ModuleContext module = generatorContext.getRootModule();
        module.addTemplate(generatorContext.getOptions(), "testStopIntentHandler", stopIntentHandlerTest, provider);
    }

    @Override
    public String getCategory() {
        return Category.IOT;
    }

}
