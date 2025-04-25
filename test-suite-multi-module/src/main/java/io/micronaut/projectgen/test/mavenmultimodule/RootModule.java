package io.micronaut.projectgen.test.mavenmultimodule;

import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.buildtools.maven.Packaging;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.generator.ModuleContext;
import jakarta.inject.Singleton;
import multimodule.catalog;

@Singleton
class RootModule implements Feature {
    private static final Dependency COORDINATE = Dependency.builder()
        .groupId("org.springframework")
        .artifactId("gs-multi-module")
        .version("0.1.0")
        .build();

    @Override
    public String getName() {
        return "root-module";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        ModuleContext module = generatorContext.getRootModule();
        module.moduleAttributes().setCoordinate(COORDINATE);
        module.moduleAttributes().setPackaging(Packaging.POM);
        module.addTemplate("libs.versions.toml", "gradle/libs.versions.toml", catalog.template());
    }
}
