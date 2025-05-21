package io.micronaut.monolith.features;

import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import jakarta.inject.Singleton;

@Singleton
class ChromeSeleniumDriver implements SeleniumDriver {
    private static final Dependency DEPENDENCY_SELENIUM_CHROME_DRIVER = Dependency.builder()
        .groupId(GROUP_ID_SELENIUM_DRIVER)
        .artifactId("selenium-chrome-driver")
        .version(SELENIUM_VERSION)
        .runtime()
        .build();

    @Override
    public String getName() {
        return "selenium-driver-chrome";
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public String getThirdPartyDocumentation(GeneratorContext generatorContext) {
        return "https://developer.chrome.com/docs/chromedriver/downloads";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        SeleniumDriver.super.apply(generatorContext);
        generatorContext.getRootModule().addDependency(DEPENDENCY_SELENIUM_CHROME_DRIVER);
    }
}
