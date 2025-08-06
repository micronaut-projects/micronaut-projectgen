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
package io.micronaut.starter.feature.camunda;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.feature.config.Configuration;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.starter.feature.database.DatabaseDriverFeature;
import io.micronaut.starter.feature.server.Jetty;
import io.micronaut.starter.feature.server.Netty;
import io.micronaut.projectgen.micronaut.features.test.AssertJ;
import jakarta.inject.Singleton;

/**
 * Camunda Platform 7 feature for embedding the Camunda Workflow Engine in Micronaut applications.
 */
@Requires(property = "micronaut.starter.feature.camunda.platform7.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Platform7 implements CamundaCommunityFeature {
    public static final String NAME = "camunda-platform7";

    private static final Dependency.Builder DEPENDENCY_PLATFORM7 = Dependency.builder()
        .lookupArtifactId("micronaut-camunda-bpm-feature")
        .compile();

    private static final Dependency.Builder DEPENDENCY_BPM_ASSERT = Dependency.builder()
        .lookupArtifactId("camunda-bpm-assert")
        .test();

    private final DatabaseDriverFeature defaultDbFeature;
    private final Jetty jetty;
    private final AssertJ assertJ;

    public Platform7(DatabaseDriverFeature defaultDbFeature, Jetty jetty, AssertJ assertJ) {
        this.defaultDbFeature = defaultDbFeature;
        this.jetty = jetty;
        this.assertJ = assertJ;
    }

    @NonNull
    @Override
    public String getCommunityFeatureName() {
        return "platform7";
    }

    @Override
    @NonNull
    public String getCommunityFeatureTitle() {
        return "Camunda Platform 7 Workflow Engine";
    }

    @Override
    public boolean isCommunity() {
        return true;
    }

    @Override
    public String getDescription() {
        return "Bringing process automation to Micronaut: Embed the Camunda Platform 7 Workflow Engine";
    }

    @Override
    public boolean supports(Options options) {
        ApplicationType type = ApplicationType.of(options.template());
        return type == ApplicationType.DEFAULT;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.exclude(Netty.class::isInstance);
        featureContext.addFeatureIfNotPresent(DatabaseDriverFeature.class, defaultDbFeature);
        featureContext.addFeatureIfNotPresent(Jetty.class, jetty);
        featureContext.addFeatureIfNotPresent(AssertJ.class, assertJ);
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        ModuleContext module = generatorContext.getRootModule();
        addConfiguration(module);
        module.addDependency(DEPENDENCY_PLATFORM7);
        module.addDependency(DEPENDENCY_BPM_ASSERT);
    }

    @Override
    public String getCategory() {
        return Category.BPM;
    }

    @Override
    @Nullable
    public String getThirdPartyDocumentation(GeneratorContext generatorContext) {
        return "https://github.com/camunda-community-hub/micronaut-camunda-platform-7";
    }

    protected static void addConfiguration(ModuleContext moduleContext) {
        Configuration config = moduleContext.configuration();
        config.put("camunda.admin-user.id", "admin");
        config.put("camunda.admin-user.password", "admin");
        config.put("camunda.webapps.enabled", true);
        config.put("camunda.rest.enabled", true);
        config.put("camunda.generic-properties.properties.initialize-telemetry", true);
        config.put("camunda.filter.create", "All tasks");
    }

}
