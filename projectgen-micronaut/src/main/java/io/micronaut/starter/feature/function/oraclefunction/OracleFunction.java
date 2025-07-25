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
package io.micronaut.projectgen.micronaut.template.function.oraclefunction;

import com.fizzed.rocker.RockerModel;
import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.Project;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.micronaut.features.logging.Logback;
import io.micronaut.starter.buildtools.dependencies.MicronautDependencyUtils;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.starter.feature.function.AbstractFunctionFeature;
import io.micronaut.starter.feature.logging.SimpleLogging;
import io.micronaut.starter.feature.server.ServerFeature;
import io.micronaut.projectgen.micronaut.template.server.groovyJunit;
import io.micronaut.projectgen.micronaut.template.server.javaJunit;
import io.micronaut.projectgen.micronaut.template.server.koTest;
import io.micronaut.projectgen.micronaut.template.server.kotlinJunit;
import io.micronaut.projectgen.micronaut.template.server.spock;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.rocker.RockerTemplate;
import jakarta.inject.Singleton;

@Requires(property = "micronaut.starter.feature.oracle.function.http.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
@Primary
public class OracleFunction extends AbstractFunctionFeature implements OracleCloudFeature {
    public static final String GROUP_ID_COM_FNPROJECT_FN = "com.fnproject.fn";
    public static final Dependency COM_FNPROJECT_RUNTIME = Dependency.builder()
        .groupId(GROUP_ID_COM_FNPROJECT_FN)
        .artifactId("runtime")
        .runtime()
        .build();
    private static final Dependency MICRONAUT_OCI_FUNCTION_HTTP = MicronautDependencyUtils
        .ociDependency()
        .artifactId("micronaut-oraclecloud-function-http")
        .compile()
        .build();

    private static final Dependency MICRONAUT_OCI_FUNCTION_HTTP_TEST = MicronautDependencyUtils
        .ociDependency()
        .artifactId("micronaut-oraclecloud-function-http-test")
        .test()
        .build();
    private final SimpleLogging simpleLogging;

    public OracleFunction(SimpleLogging simpleLogging) {
        this.simpleLogging = simpleLogging;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(SimpleLogging.class)) {
            featureContext.addFeature(simpleLogging);
            featureContext.exclude(Logback.class::isInstance);
        }

        if (featureContext.isPresent(ServerFeature.class)) {
            featureContext.exclude(ServerFeature.class::isInstance);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);
        ModuleContext module = generatorContext.getRootModule();
        addDependencies(generatorContext.getOptions(), module);
        addFuncYamlTemplate(module, generatorContext.getProject());
    }

    @Override
    @NonNull
    public String getName() {
        return "oracle-function-http";
    }

    @Override
    public String getTitle() {
        return "Oracle Function";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds support for writing functions to deploy to Oracle Cloud Function";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    protected String getRunCommand(BuildTool buildTool) {
        if (buildTool == BuildTool.MAVEN) {
            return "mvnw mn:run";
        } else {
            return "gradlew run";
        }
    }

    @Override
    protected String getBuildCommand(BuildTool buildTool) {
        if (buildTool == BuildTool.MAVEN) {
            return "mvnw clean package";
        } else if (buildTool == BuildTool.GRADLE) {
            return "gradlew clean assemble";
        } else {
            throw new IllegalStateException("Unsupported build tool");
        }
    }

    @Override
    protected String getTestSuffix(ApplicationType type) {
        if (type == ApplicationType.FUNCTION) {
            return "Function";
        }
        return "Controller";
    }

    @Override
    protected RockerModel javaJUnitTemplate(Project project) {
        return javaJunit.template(project, true);
    }

    @Override
    protected RockerModel kotlinJUnitTemplate(Project project) {
        return kotlinJunit.template(project, true);
    }

    @Override
    protected RockerModel groovyJUnitTemplate(Project project) {
        return groovyJunit.template(project, true);
    }

    @Override
    protected RockerModel koTestTemplate(Project project) {
        return koTest.template(project, true);
    }

    @Override
    public RockerModel spockTemplate(Project project) {
        return spock.template(project, true);
    }

    @Nullable
    @Override
    public String getFrameworkDocumentation(GeneratorContext generatorContext) {
        return "https://micronaut-projects.github.io/micronaut-oracle-cloud/latest/guide/#httpFunctions";
    }

    @Override
    @NonNull
    public String resolveMicronautRuntime(@NonNull GeneratorContext generatorContext) {
        return "oracle_function";
    }

    protected void addDependencies(Options options, ModuleContext module) {
        if (OptionUtils.hasMavenBuildTool(options)) {
            module.addDependency(COM_FNPROJECT_RUNTIME);
            module.addDependency(MICRONAUT_OCI_FUNCTION_HTTP);
            module.addDependency(MICRONAUT_OCI_FUNCTION_HTTP_TEST);
        }
    }

    protected final void addFuncYamlTemplate(ModuleContext module, Project project) {
        module.addTemplate(
            "func.yml", new RockerTemplate(
            "func.yml",
            projectFnFunc.template(project
            ))
        );
    }
}
