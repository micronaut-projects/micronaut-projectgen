package io.micronaut.projectgen.demo;

import io.micronaut.projectgen.core.buildtools.MavenCentral;
import io.micronaut.projectgen.core.buildtools.MavenLocal;
import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.buildtools.gradle.GradlePlugin;
import io.micronaut.projectgen.core.feature.DefaultFeature;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.options.Options;
import jakarta.inject.Singleton;

import java.util.Set;

@Singleton
public class OpenRewriteGradlePluginFeature implements DefaultFeature {
    @Override
    public String getName() {
        return "openrewrite-rewrite-build-plugin";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        ModuleContext module = generatorContext.getRootModule();
        module.repositories().add(new MavenLocal());
        module.repositories().add(new MavenCentral());
        module.addBuildPlugin(GradlePlugin.builder()
                .id("org.openrewrite.rewrite")
                .version("7.11.0")
                .build());
        module.addDependency(Dependency.builder()
                .groupId("io.micronaut.projectgen")
                .artifactId("test-suite-helloworld-openrewrite")
                .version("0.0.1-SNAPSHOT")
            .scope(Scope.OPENREWRITE)
            .build());
    }

    @Override
    public boolean shouldApply(Options options, Set<Feature> selectedFeatures) {
        return true;
    }
}
