package io.micronaut.projectgen.test.mavenmultimodule;

import io.micronaut.projectgen.core.buildtools.MavenCentral;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.buildtools.gradle.GradlePlugin;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.utils.OptionUtils;
import jakarta.inject.Singleton;
import static io.micronaut.projectgen.test.mavenmultimodule.SpringBootDependencies.*;

@Singleton
class ApplicationModule implements Feature {

    private static final String MODULE_APPLICATION = "application";
    private static final Dependency COORDINATE = Dependency.builder()
        .groupId("com.example")
        .artifactId(MODULE_APPLICATION)
        .version("0.0.1-SNAPSHOT")
        .build();
    private static final Dependency LIBRARY_DEPENDENCY = Dependency.builder()
        .groupId("com.example")
        .artifactId("library")
        .version("${project.version}")
        .compile()
        .project("library")
        .build();


    @Override
    public String getName() {
        return MODULE_APPLICATION;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        ModuleContext module = generatorContext.getModuleByName(MODULE_APPLICATION);
        module.configuration().put("spring.application.name", "demo");
        module.moduleAttributes().setCoordinate(COORDINATE);
        module.moduleAttributes().setDescription("Demo project for Spring Boot");
        module.addDependency(SPRING_BOOT_STARTER_ACTUATOR);
        module.addDependency(SPRING_BOOT_STARTER_WEB);
        module.addDependency(SPRING_BOOT_STARTER_TEST);
        module.addDependency(LIBRARY_DEPENDENCY);
        if (OptionUtils.hasMavenBuildTool(generatorContext.getOptions())) {
            module.moduleAttributes().setParentPom(SPRING_BOOT_PARENT_POM);
            module.addBuildPlugin(SPRING_BOOT_MAVEN_PLUGIN);
        }
        if (OptionUtils.hasGradleBuildTool(generatorContext.getOptions())) {
            module.repositories().add(new MavenCentral());
            module.addBuildPlugin(JAVA_GRADLE_PLUGIN);
            module.addBuildPlugin(GradlePlugin.builder()
                .id(GROUP_ID_ORG_SPRINGFRAMEWORK_BOOT)
                .version(SPRING_BOOT_VERSION)
                .build());
            module.addBuildPlugin(GradlePlugin.builder()
                .id("io.spring.dependency-management")
                .version("1.1.5")
                .build());
        }
    }
}
