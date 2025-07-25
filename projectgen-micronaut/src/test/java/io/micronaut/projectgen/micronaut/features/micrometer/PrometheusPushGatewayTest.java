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
class PrometheusPushGatewayTest {
    @Test
    void prometheusPushGatewayConfiguration(ProjectGenerator micronautProjectGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("micrometer-prometheus-pushgateway")).build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        assertEquals(StringUtils.TRUE, applicationProperties.getProperty("micronaut.metrics.export.prometheus.pushgateway.enabled"));
        assertEquals(StringUtils.TRUE, applicationProperties.getProperty("micronaut.metrics.enabled"));
    }

    @Test
    void prometheusPushGatewayFeaturesAddsTheDependency(ProjectGenerator micronautProjectGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("micrometer-prometheus-pushgateway")).build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("io.micronaut.micrometer", "micronaut-micrometer-registry-prometheus-pushgateway", Scope.COMPILE), buildGradle);
    }

    @Test
    void prometheusPushGatewayFeaturesAddsTheLinkInReadmeFile(ProjectGenerator micronautProjectGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("micrometer-prometheus-pushgateway")).build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://micronaut-projects.github.io/micronaut-micrometer/latest/guide/#metricsAndReportersPrometheusPushGateway"));
    }

    private static Map<String, String> generateProject(ProjectGenerator micronautProjectGenerator,
        Options options) throws Exception {
        MapOutputHandler outputHandler = new MapOutputHandler();
        micronautProjectGenerator.generate(options, outputHandler);
        return outputHandler.getProject();
    }
}
