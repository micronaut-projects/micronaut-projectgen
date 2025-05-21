package io.micronaut.monolith.features;

import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import jakarta.inject.Singleton;

@Singleton
class GebCore implements Feature {
    private final Dependency DEPENDENCY_CORE = Dependency.builder()
        .groupId("org.gebish")
        .artifactId("geb-core")
        .version("7.0")
        .compile()
        .build();

    @Override
    public String getName() {
        return "geb-core";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getRootModule().addDependency(DEPENDENCY_CORE);
    }
}
