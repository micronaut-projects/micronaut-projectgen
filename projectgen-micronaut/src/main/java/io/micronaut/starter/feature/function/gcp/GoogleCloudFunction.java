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
package io.micronaut.starter.feature.function.gcp;

import com.fizzed.rocker.RockerModel;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.generator.Project;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.micronaut.gradle.ShadePlugin;
import io.micronaut.starter.buildtools.dependencies.MicronautDependencyUtils;
import io.micronaut.projectgen.micronaut.template.function.gcp.gcpFunctionGroovyJunit;
import io.micronaut.projectgen.micronaut.template.function.gcp.gcpFunctionJavaJunit;
import io.micronaut.projectgen.micronaut.template.function.gcp.gcpFunctionKoTest;
import io.micronaut.projectgen.micronaut.template.function.gcp.gcpFunctionKotlinJunit;
import io.micronaut.projectgen.micronaut.template.function.gcp.gcpFunctionSpock;
import io.micronaut.starter.feature.json.JacksonDatabindFeature;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import jakarta.inject.Singleton;

/**
 * A feature for supporting Google Cloud Function.
 *
 * @author graemerocher
 * @since 2.0.0
 */
@Requires(property = "micronaut.starter.feature.google.cloud.function.http.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class GoogleCloudFunction extends AbstractGoogleCloudFunction {

    public static final String NAME = "google-cloud-function-http";

    private static final Dependency MICRONAUT_GCP_FUNCTION_HTTP = MicronautDependencyUtils
        .gcpDependency()
        .artifactId("micronaut-gcp-function-http")
        .compile()
        .build();

    private static final Dependency MICRONAUT_GCP_FUNCTION_HTTP_TEST = MicronautDependencyUtils
        .gcpDependency()
        .artifactId("micronaut-gcp-function-http-test")
        .test()
        .build();

    public GoogleCloudFunction(ShadePlugin shadePlugin, JacksonDatabindFeature jacksonDatabindFeature) {
        super(shadePlugin, jacksonDatabindFeature);
    }

    @NonNull
    @Override
    public String getName() {
        return NAME;
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
    public boolean isVisible() {
        return false;
    }

    @Override
    public RockerModel javaJUnitTemplate(Project project) {
        return gcpFunctionJavaJunit.template(project);
    }

    @Override
    public RockerModel kotlinJUnitTemplate(Project project) {
        return gcpFunctionKotlinJunit.template(project);
    }

    @Override
    public RockerModel groovyJUnitTemplate(Project project) {
        return gcpFunctionGroovyJunit.template(project);
    }

    @Override
    protected RockerModel koTestTemplate(Project project) {
        return gcpFunctionKoTest.template(project);
    }

    @Override
    public RockerModel spockTemplate(Project project) {
        return gcpFunctionSpock.template(project);
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);
        ModuleContext module = generatorContext.getRootModule();
        addDependencies(module, generatorContext.getOptions());
    }

    protected final void addDependencies(ModuleContext module, Options options) {
        if (OptionUtils.hasMavenBuildTool(options)) {
            module.addDependency(GCP_FUNCTIONS_FRAMEWORK_API.compileOnly());
            module.addDependency(MICRONAUT_GCP_FUNCTION_HTTP);
            module.addDependency(MICRONAUT_GCP_FUNCTION_HTTP_TEST);
        }
    }

    @Override
    protected String getRunCommand(BuildTool buildTool) {
        return GcpCloudFunctionBuildCommandUtils.getRunCommand(buildTool);
    }

    @Override
    protected String getBuildCommand(BuildTool buildTool) {
        return GcpCloudFunctionBuildCommandUtils.getBuildCommand(buildTool);
    }

    @Override
    public String getFrameworkDocumentation(GeneratorContext generatorContext) {
        return "https://micronaut-projects.github.io/micronaut-gcp/latest/guide/index.html#httpFunctions";
    }

}
