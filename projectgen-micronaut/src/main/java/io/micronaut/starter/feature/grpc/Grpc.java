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
package io.micronaut.starter.feature.grpc;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.BuildPlugin;
import io.micronaut.projectgen.core.buildtools.gradle.GradleDsl;
import io.micronaut.projectgen.core.buildtools.gradle.GradlePlugin;
import io.micronaut.starter.feature.Category;
import io.micronaut.projectgen.core.feature.DefaultFeature;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.starter.feature.discovery.DiscoveryCore;
import io.micronaut.projectgen.micronaut.template.grpc.proto;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.rocker.RockerTemplate;

import jakarta.inject.Singleton;

import java.util.List;
import java.util.Set;

/**
 * Adds gRPC support to the project.
 * <p>
 * Automatically adds the DiscoveryCore feature if not present.
 * Applies protobuf Gradle plugin and proto templates for gRPC projects.
 */
@Requires(property = "micronaut.starter.feature.grpc.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Grpc implements DefaultFeature, OpenRewriteFeature {

    private final DiscoveryCore discoveryCore;

    public Grpc(DiscoveryCore discoveryCore) {
        this.discoveryCore = discoveryCore;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeatureIfNotPresent(DiscoveryCore.class, discoveryCore);
    }

    @Override
    public boolean shouldApply(Options options, Set<Feature> selectedFeatures) {
        return ApplicationType.of(options.template()) == ApplicationType.GRPC;
    }

    private BuildPlugin gradlePlugin(GeneratorContext generatorContext) {
        GradlePlugin.Builder builder = GradlePlugin.builder()
            .id("com.google.protobuf")
            .lookupArtifactId("protobuf-gradle-plugin");
        GradleDsl gradleDsl = generatorContext.getOptions().gradleDsl();
        if (gradleDsl == GradleDsl.KOTLIN) {
            builder.buildImports("import com.google.protobuf.gradle.*");
        }
        return builder.build();
    }

    @Override
    public String getName() {
        return "grpc";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public String getCategory() {
        return Category.API;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        OpenRewriteFeature.super.apply(generatorContext);
        ModuleContext module = generatorContext.getRootModule();
        module.addTemplate("proto", new RockerTemplate("src/main/proto/{propertyName}.proto", proto.template(generatorContext.getProject())));
        if (OptionUtils.hasGradleBuildTool(generatorContext.getOptions())) {
            module.addHelpLink("Protobuf Gradle Plugin", "https://plugins.gradle.org/plugin/com.google.protobuf");
            module.addBuildPlugin(gradlePlugin(generatorContext));
        }
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return List.of("io.micronaut.starter.feature.grpc");
    }

}
