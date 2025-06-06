package io.micronaut.projectgen.demo;

import io.micronaut.projectgen.core.buildtools.gradle.GradlePlugin;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.utils.OptionUtils;
import jakarta.inject.Singleton;

@Singleton
class GradleApplicationPluginFeature implements Feature {
    @Override
    public String getName() {
        return "gradle-plugin-application";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (OptionUtils.hasGradleBuildTool(generatorContext.getOptions())) {
            generatorContext.getRootModule()
                .addBuildPlugin(GradlePlugin.builder()
                    .id("application")
                    .extension("""
                application {
                    mainClass.set("%s.HelloWorld")
                }""".formatted(generatorContext.getOptions().packageName()))
                    .build());
        }

    }
}
