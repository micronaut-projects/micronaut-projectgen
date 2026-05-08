package io.micronaut.projectgen.test.mavenmultimodule;

import io.micronaut.projectgen.core.buildtools.MavenCentral;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.buildtools.gradle.GradlePlugin;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.rocker.RockerTemplate;
import io.micronaut.projectgen.core.rocker.RockerWritable;
import io.micronaut.projectgen.core.utils.OptionUtils;
import jakarta.inject.Singleton;
import multimodule.springBootDependencyManagement;
import multimodule.myService;
import multimodule.myServiceTest;
import multimodule.serviceProperties;

import static io.micronaut.projectgen.test.mavenmultimodule.SpringBootDependencies.GROUP_ID_ORG_SPRINGFRAMEWORK_BOOT;
import static io.micronaut.projectgen.test.mavenmultimodule.SpringBootDependencies.JAVA_GRADLE_PLUGIN;
import static io.micronaut.projectgen.test.mavenmultimodule.SpringBootDependencies.SPRING_BOOT;
import static io.micronaut.projectgen.test.mavenmultimodule.SpringBootDependencies.SPRING_BOOT_PARENT_POM;
import static io.micronaut.projectgen.test.mavenmultimodule.SpringBootDependencies.SPRING_BOOT_STARTER_TEST;
import static io.micronaut.projectgen.test.mavenmultimodule.SpringBootDependencies.SPRING_BOOT_VERSION;

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
            module.addBuildPlugin(GradlePlugin.builder()
                .id(GROUP_ID_ORG_SPRINGFRAMEWORK_BOOT)
                .version(SPRING_BOOT_VERSION)
                .apply(false).build());
            module.addBuildPlugin(GradlePlugin.builder()
                .id("io.spring.dependency-management")
                .version("1.1.5")
                .extension(new RockerWritable(springBootDependencyManagement.template()))
                .build());
        }
        module.moduleAttributes().setCoordinate(COORDINATE);
        module.moduleAttributes().setDescription("Demo project for Spring Boot");
        module.addDependency(SPRING_BOOT);
        module.addDependency(SPRING_BOOT_STARTER_TEST);

        module.addTemplate("ServiceProperties.java", "src/main/java/com/example/multimodule/service/ServiceProperties.java",
            serviceProperties.template());
        module.addTemplate("MyServiceTest.java", "src/test/java/com/example/multimodule/service/MyServiceTest.java",
            myServiceTest.template());
        module.addTemplate("MyService.java", "src/main/java/com/example/multimodule/service/MyService.java",
            myService.template());
    }
}
