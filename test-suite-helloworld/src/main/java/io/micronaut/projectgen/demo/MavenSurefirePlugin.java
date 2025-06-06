package io.micronaut.projectgen.demo;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.projectgen.core.buildtools.maven.MavenPlugin;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.utils.OptionUtils;
import jakarta.inject.Singleton;

@Singleton
class MavenSurefirePlugin implements Feature {
    private static final @NonNull MavenPlugin MAVEN_PLUGIN_SUREFIRE = MavenPlugin.builder() // <1>
        .groupId("org.apache.maven.plugins")
        .artifactId("maven-surefire-plugin")
        .version("3.1.2")
        .build();

    @Override
    public String getName() {
        return "maven-surefire-plugin";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (OptionUtils.hasMavenBuildTool(generatorContext.getOptions())) {
            generatorContext.getRootModule().addBuildPlugin(MAVEN_PLUGIN_SUREFIRE);
        }
    }
}
