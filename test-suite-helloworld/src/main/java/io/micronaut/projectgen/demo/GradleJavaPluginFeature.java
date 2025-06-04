package io.micronaut.projectgen.demo;

import io.micronaut.projectgen.core.buildtools.gradle.GradlePlugin;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.utils.OptionUtils;
import jakarta.inject.Singleton;

@Singleton
class GradleJavaPluginFeature implements Feature {
    @Override
    public String getName() {
        return "gradle-plugin-java";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (OptionUtils.hasGradleBuildTool(generatorContext.getOptions())) {
            int javaVersion = generatorContext.getOptions()
                .java()
                .majorVersion();
            generatorContext.getRootModule()
                .addBuildPlugin(GradlePlugin.builder()
                    .id("java")
                    .extension(String.format("""
                        java {
                            sourceCompatibility = JavaVersion.VERSION_%1$d
                            targetCompatibility = JavaVersion.VERSION_%1$d
                        }
                        tasks.test {
                            useJUnitPlatform()
                        }""", javaVersion))
                .build());
        }
    }
}
