package io.micronaut.projectgen.micronaut.features.micrometer;

import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.generator.ProjectGenerator;
import io.micronaut.projectgen.core.io.MapOutputHandler;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.OptionsFixture;
import io.micronaut.projectgen.test.BuildTestVerifier;
import io.micronaut.projectgen.test.ConfigurationUtils;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class NewRelicTest {
    @Test
    void newRelicConfiguration(ProjectGenerator micronautProjectGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("micrometer-new-relic")).build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        assertEquals(StringUtils.TRUE, applicationProperties.getProperty("micronaut.metrics.export.new-relic.enabled"));
        assertEquals(StringUtils.TRUE, applicationProperties.getProperty("micronaut.metrics.enabled"));
        assertEquals("PT1M", applicationProperties.getProperty("micronaut.metrics.export.new-relic.step"));
        assertEquals("${NEWRELIC_API_KEY}", applicationProperties.getProperty("micronaut.metrics.export.newrelic.apiKey"));
        assertEquals("${NEWRELIC_ACCOUNT_ID}", applicationProperties.getProperty("micronaut.metrics.export.newrelic.accountId"));
    }

    @Test
    void humioFeaturesAddsTheDependency(ProjectGenerator micronautProjectGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("micrometer-new-relic")).build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("io.micronaut.micrometer", "micronaut-micrometer-registry-new-relic", Scope.COMPILE), buildGradle);
    }

    private static Map<String, String> generateProject(ProjectGenerator micronautProjectGenerator,
        Options options) throws Exception {
        MapOutputHandler outputHandler = new MapOutputHandler();
        micronautProjectGenerator.generate(options, outputHandler);
        return outputHandler.getProject();
    }
}
