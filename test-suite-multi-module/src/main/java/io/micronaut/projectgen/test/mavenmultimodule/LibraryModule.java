package io.micronaut.projectgen.test.mavenmultimodule;

import io.micronaut.projectgen.core.buildtools.MavenCentral;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.rocker.RockerWritable;
import io.micronaut.projectgen.core.utils.OptionUtils;
import jakarta.inject.Singleton;
import multimodule.springBootDependencyManagement;

import static io.micronaut.projectgen.test.mavenmultimodule.SpringBootDependencies.*;

@Singleton
class LibraryModule implements Feature {
    private static final String MODULE_LIBRARY = "library";
    private static final Dependency COORDINATE = Dependency.builder()
        .groupId("com.example")
        .artifactId(MODULE_LIBRARY)
        .version("0.0.1-SNAPSHOT")
        .build();

    @Override
    public String getName() {
        return MODULE_LIBRARY;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        ModuleContext module = generatorContext.getModuleByName(MODULE_LIBRARY);
        if (OptionUtils.hasMavenBuildTool(generatorContext.getOptions())) {
            module.moduleAttributes().setParentPom(SPRING_BOOT_PARENT_POM);
        }
        if (OptionUtils.hasGradleBuildTool(generatorContext.getOptions())) {
            module.repositories().add(new MavenCentral());
            module.addBuildPlugin(JAVA_GRADLE_PLUGIN);
            module.addBuildPlugin(SPRING_BOOT_PLUGIN_BUILDER.apply(false).build());
            module.addBuildPlugin(SPRING_DEPENDENCY_MANAGEMENT_GRADLE_PLUGIN_BUILDER.get()
                .extension(new RockerWritable(springBootDependencyManagement.template()))
                .build());
        }
        module.moduleAttributes().setCoordinate(COORDINATE);
        module.moduleAttributes().setDescription("Demo project for Spring Boot");
        module.addDependency(SPRING_BOOT);
        module.addDependency(SPRING_BOOT_STARTER_TEST);
    }
}
