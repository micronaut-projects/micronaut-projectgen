package io.micronaut.projectgen.test.mavenmultimodule;

import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.buildtools.maven.MavenPlugin;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.generator.ModuleContext;
import jakarta.inject.Singleton;

@Singleton
class LibraryModule implements Feature {
    private static final String MODULE_LIBRARY = "library";

    @Override
    public String getName() {
        return MODULE_LIBRARY;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {

        ModuleContext module = generatorContext.getModuleByName(MODULE_LIBRARY);
        module.addDependency(Dependency.builder()
                .groupId("org.springframework.boot")
                .artifactId("spring-boot-starter")
                .compile());
        module.addDependency(Dependency.builder()
            .groupId("org.springframework.boot")
            .artifactId("spring-boot-starter-test")
            .test());
        module.addBuildPlugin(MavenPlugin.builder()
            .groupId("org.springframework.boot")
            .artifactId("spring-boot-maven-plugin")
            .build());
    }
}
