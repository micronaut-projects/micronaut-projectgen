package io.micronaut.projectgen.quarkus.features.buildtools.gradle;

import io.micronaut.projectgen.core.buildtools.gradle.GradlePlugin;
import io.micronaut.projectgen.core.buildtools.gradle.GradleSpecificFeature;
import io.micronaut.projectgen.core.feature.BuildPluginFeature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import jakarta.inject.Singleton;

@Singleton
public class QuarkusGradlePlugin implements GradleSpecificFeature, BuildPluginFeature {
    private static final GradlePlugin QUARKUS_GRADLE_PLUGIN = GradlePlugin.builder().id("io.quarkus").build();

    @Override
    public String getName() {
        return "quarkus-gradle-plugin";
    }

    @Override
    public String getDescription() {
        return "Quarkus Gradle Plugin";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addBuildPlugin(QUARKUS_GRADLE_PLUGIN);
    }
}
