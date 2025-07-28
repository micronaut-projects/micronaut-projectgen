/*
 * Copyright 2017-2023 original authors
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
package io.micronaut.projectgen.micronaut.features.httpclient;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.micronaut.features.validation.MicronautHttpValidation;
import io.micronaut.starter.buildtools.dependencies.MicronautDependencyUtils;
import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.feature.FeaturePhase;
import io.micronaut.starter.feature.awslambdacustomruntime.AwsLambdaCustomRuntime;
import io.micronaut.starter.feature.function.awslambda.AwsLambda;
import io.micronaut.starter.feature.graalvm.GraalVM;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Requires(property = "micronaut.starter.feature.http.client.test.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class HttpClientTest implements OpenRewriteFeature {
    public static final String ARTIFACT_ID_MICRONAUT_HTTP_CLIENT = "micronaut-http-client";
    private static final String ARTIFACT_ID_MICRONAUT_HTTP_CLIENT_JDK = "micronaut-http-client-jdk";

    @Override
    public String getName() {
        return "http-client-test";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        ApplicationType applicationType = ApplicationType.of(generatorContext.getOptions().template());
        if (hasHttpClientFeatureDependencyInScope(generatorContext, Scope.COMPILE)) {
            return Collections.emptyList();
        }
        List<String> recipes = new ArrayList<>();
        if (generatorContext.getFeatures().hasFeature(AwsLambdaCustomRuntime.class) || (generatorContext.getFeatures().hasFeature(AwsLambda.class) && generatorContext.getFeatures().hasFeature(GraalVM.class))) {
            recipes.add("io.micronaut.starter.feature.http-client-jdk.dependencies");

        } else if (applicationType == ApplicationType.DEFAULT) {
            recipes.add(generatorContext.getFeatures().hasFeature(AwsLambda.class)
                ? "io.micronaut.starter.feature.http-client-jdk.dependencies.test"
                : "io.micronaut.starter.feature.http-client.dependencies.test");
            if (generatorContext.hasFeature(MicronautHttpValidation.class) && OptionUtils.hasGradleBuildTool(generatorContext.getOptions())) {
                recipes.add(generatorContext.getFeatures().hasFeature(AwsLambda.class)
                    ? "io.micronaut.starter.feature.http-client-jdk.dependencies.compileonly"
                    : "io.micronaut.starter.feature.http-client.dependencies.compileonly");
            }
        }
        return recipes;
    }

    private boolean hasHttpClientFeatureDependencyInScope(@NonNull GeneratorContext generatorContext, @NonNull Scope scope) {
        ModuleContext module = generatorContext.getRootModule();
        return module.hasDependencyInScope(MicronautDependencyUtils.GROUP_ID_MICRONAUT, ARTIFACT_ID_MICRONAUT_HTTP_CLIENT, scope)
            || module.hasDependencyInScope(MicronautDependencyUtils.GROUP_ID_MICRONAUT, ARTIFACT_ID_MICRONAUT_HTTP_CLIENT_JDK, scope);
    }

    @Override
    public int getOrder() {
        return FeaturePhase.TEST.getOrder();
    }
}
