package io.micronaut.projectgen.demo;

import io.micronaut.projectgen.core.buildtools.MavenCentral;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.utils.OptionUtils;
import jakarta.inject.Singleton;

@Singleton
class JunitJupiter implements Feature {
    private static final Dependency DEPENDENCY_JUNIT_JUPITER = Dependency.builder() // <1>
        .groupId("org.junit.jupiter")
        .artifactId("junit-jupiter")
        .version("5.10.2")
        .test()
        .build();

    @Override
    public String getName() {
        return "junit-jupiter";
    }

    @Override
    public String getDescription() {
        return "Adds the JUnit Jupiter dependency to the project";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        ModuleContext module = generatorContext.getRootModule();
        if (OptionUtils.hasGradleBuildTool(generatorContext.getOptions())) {
            module.repositories().add(new MavenCentral()); // <1>
        }
        module.addDependency(DEPENDENCY_JUNIT_JUPITER); // <2>
    }
}
