package io.micronaut.projectgen.micronaut.features.coherence;

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
class CoherenceDistributedConfigurationTest {
    @Test
    void coherenceDistributedConfigurationConfiguration(MicronautProjectGenerator micronautProjectGenerator) throws Exception {
        MicronautOptions options = MicronautOptions.builder().feature("coherence-distributed-configuration").build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        Properties bootstrapProperties = ConfigurationUtils.loadBootstrapProperties(project);
        assertEquals(StringUtils.TRUE, bootstrapProperties.getProperty("coherence.client.enabled"));
        assertEquals("localhost", bootstrapProperties.getProperty("coherence.client.host"));
        assertEquals("1408", bootstrapProperties.getProperty("coherence.client.port"));
    }

    @Test
    void coherenceFeaturesAddsTheDependency(MicronautProjectGenerator micronautProjectGenerator) throws Exception {
        MicronautOptions options = MicronautOptions.builder().feature("coherence-distributed-configuration").build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("io.micronaut.coherence", "micronaut-coherence-distributed-configuration", Scope.COMPILE), buildGradle);
    }

    @Test
    void coherenceFeaturesAddsTheLinkReadmeFile(MicronautProjectGenerator micronautProjectGenerator) throws Exception {
        MicronautOptions options = MicronautOptions.builder().feature("coherence-distributed-configuration").build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://micronaut-projects.github.io/micronaut-coherence/latest/guide/#distributedConfiguration"));
        assertTrue(readme.contains("https://coherence.java.net/"));
    }

    private static Map<String, String> generateProject(MicronautProjectGenerator micronautProjectGenerator, MicronautOptions options) throws Exception {
        MapOutputHandler outputHandler = new MapOutputHandler();
        micronautProjectGenerator.generate(options, outputHandler);
        return outputHandler.getProject();
    }
}
