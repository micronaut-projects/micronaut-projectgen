package io.micronaut.projectgen.micronaut.features.tracing;

import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.buildtools.Scope;
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
class ZipkinTest {
    @Test
    void zipkinConfiguration(MicronautProjectGenerator micronautProjectGenerator) throws Exception {
        MicronautOptions options = MicronautOptions.builder().feature("tracing-zipkin").build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        assertEquals(StringUtils.TRUE, applicationProperties.getProperty("tracing.zipkin.enabled"));
        assertEquals("0.1", applicationProperties.getProperty("tracing.zipkin.sampler.probability"));
        assertEquals("http://localhost:9411", applicationProperties.getProperty("tracing.zipkin.http.url"));

    }

    @Test
    void zipkinFeaturesAddsTheDependency(MicronautProjectGenerator micronautProjectGenerator) throws Exception {
        MicronautOptions options = MicronautOptions.builder().feature("tracing-zipkin").build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("io.micronaut.tracing", "micronaut-tracing-brave-http", Scope.COMPILE), buildGradle);
    }

    @Test
    void zipkinFeaturesAddsTheLinkInReadmeFile(MicronautProjectGenerator micronautProjectGenerator) throws Exception {
        MicronautOptions options = MicronautOptions.builder().feature("tracing-zipkin").build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://micronaut-projects.github.io/micronaut-tracing/latest/guide/#zipkin"));
        assertTrue(readme.contains("https://zipkin.io/"));

    }

    private static Map<String, String> generateProject(MicronautProjectGenerator micronautProjectGenerator,
                                                       MicronautOptions options) throws Exception {
        MapOutputHandler outputHandler = new MapOutputHandler();
        micronautProjectGenerator.generate(options, outputHandler);
        return outputHandler.getProject();
    }
}
