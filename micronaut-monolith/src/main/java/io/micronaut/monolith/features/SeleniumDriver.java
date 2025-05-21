package io.micronaut.monolith.features;

import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.generator.GeneratorContext;

public interface SeleniumDriver extends Feature {
    String GROUP_ID_SELENIUM_DRIVER = "org.seleniumhq.selenium";
    String SELENIUM_VERSION = "4.2.2";
    Dependency DEPENDENCY_SELENIUM_SUPPORT = Dependency.builder()
        .groupId(GROUP_ID_SELENIUM_DRIVER)
        .artifactId("selenium-support")
        .version(SELENIUM_VERSION)
        .compile()
        .build();

    @Override
    default void apply(GeneratorContext generatorContext) {
        generatorContext.getRootModule().addDependency(DEPENDENCY_SELENIUM_SUPPORT);
    }
}
