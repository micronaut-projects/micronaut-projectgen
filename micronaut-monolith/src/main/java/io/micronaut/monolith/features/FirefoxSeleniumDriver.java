package io.micronaut.monolith.features;

import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import jakarta.inject.Singleton;

@Singleton
class FirefoxSeleniumDriver implements SeleniumDriver {
    private static final Dependency DEPENDENCY_SELENIUM_FIREFOX_DRIVER = Dependency.builder()
        .groupId(GROUP_ID_SELENIUM_DRIVER)
        .artifactId("selenium-firefox-driver")
        .version(SELENIUM_VERSION)
        .runtime()
        .build();

    @Override
    public String getName() {
        return "selenium-driver-firefox";
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public String getThirdPartyDocumentation(GeneratorContext generatorContext) {
        return "https://github.com/mozilla/geckodriver";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        SeleniumDriver.super.apply(generatorContext);
        generatorContext.getRootModule().addDependency(DEPENDENCY_SELENIUM_FIREFOX_DRIVER);
    }
}
