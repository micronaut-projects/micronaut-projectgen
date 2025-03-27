package io.micronaut.projectgen.micronaut.features.opentelemetry;


import io.micronaut.projectgen.core.io.MapOutputHandler;
import io.micronaut.projectgen.micronaut.MicronautOptions;
import io.micronaut.projectgen.micronaut.MicronautProjectGenerator;
import io.micronaut.projectgen.test.BuildTestVerifier;
import io.micronaut.projectgen.test.ConfigurationUtils;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class OpenTelemetryXrayTest {

    @Test
    void xrayConfiguration(MicronautProjectGenerator micronautProjectGenerator) throws Exception {
        MicronautOptions options = MicronautOptions.builder().feature("tracing-opentelemetry-xray").build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        assertEquals("otlp", applicationProperties.getProperty("otel.traces.exporter"));
//        assertEquals("tracecontext, baggage, xray", applicationProperties.getProperty("otel.traces.propagator"));
    }

    @Test
    void xrayFeaturesAddsTheDependency(MicronautProjectGenerator micronautProjectGenerator) throws Exception {
        MicronautOptions options = MicronautOptions.builder().feature("tracing-opentelemetry-xray").build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("io.opentelemetry", "opentelemetry-exporter-otlp"),buildGradle);
//        assertTrue(verifier.hasDependency("io.opentelemetry.contrib", "opentelemetry-aws-resources"),buildGradle);
//        assertTrue(verifier.hasDependency("io.opentelemetry.contrib", "opentelemetry-aws-xray"),buildGradle);
//        assertTrue(verifier.hasDependency("io.opentelemetry.contrib", "opentelemetry-aws-xray-propagator"),buildGradle);
    }

    @Test
    void xrayFeaturesAddsTheLinkInReadmeFile(MicronautProjectGenerator micronautProjectGenerator) throws Exception {
        MicronautOptions options = MicronautOptions.builder().feature("tracing-opentelemetry-xray").build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://micronaut-projects.github.io/micronaut-tracing/latest/guide/#opentelemetry"));
        assertTrue(readme.contains("https://opentelemetry.io"));
//        assertTrue(readme.contains("https://docs.aws.amazon.com/xray/latest/devguide/aws-xray.html"));
    }

    private static Map<String, String> generateProject(MicronautProjectGenerator micronautProjectGenerator,
                                                       MicronautOptions options) throws Exception {
        MapOutputHandler outputHandler = new MapOutputHandler();
        micronautProjectGenerator.generate(options, outputHandler);
        return outputHandler.getProject();
    }
}
