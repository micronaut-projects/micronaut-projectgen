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

import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.options.TestFramework;
import io.micronaut.projectgen.micronaut.gradle.ShadePlugin;
import io.micronaut.starter.buildtools.dependencies.MicronautDependencyUtils;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.starter.feature.function.AbstractFunctionFeature;
import io.micronaut.starter.feature.json.JacksonDatabindFeature;

/**
 * Base class for Google Cloud Function features providing common dependencies and configuration.
 */
public abstract class AbstractGoogleCloudFunction extends AbstractFunctionFeature implements GcpCloudFeature, GcpMicronautRuntimeFeature {
    public static final Dependency.Builder GCP_FUNCTIONS_FRAMEWORK_API = Dependency.builder()
        .groupId("com.google.cloud.functions")
        .artifactId("functions-framework-api");
    private static final Dependency DEPENDENCY_MICRONAUT_SERVLET_CORE = MicronautDependencyUtils.servletDependency()
        .artifactId("micronaut-servlet-core")
        .test()
        .build();

    private final ShadePlugin shadePlugin;
    private final JacksonDatabindFeature jacksonDatabindFeature;

    public AbstractGoogleCloudFunction(
        ShadePlugin shadePlugin,
        JacksonDatabindFeature jacksonDatabindFeature) {
        this.shadePlugin = shadePlugin;
        this.jacksonDatabindFeature = jacksonDatabindFeature;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);
        ModuleContext module = generatorContext.getRootModule();
        if (generatorContext.getTestFramework() == TestFramework.SPOCK) {
            module.addDependency(DEPENDENCY_MICRONAUT_SERVLET_CORE);
        }
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(ShadePlugin.class)) {
            featureContext.addFeature(shadePlugin);
        }
        if (!featureContext.isPresent(JacksonDatabindFeature.class)) {
            featureContext.addFeature(jacksonDatabindFeature);
        }
    }
}
