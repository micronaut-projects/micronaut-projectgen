package io.micronaut.projectgen.micronaut.features.database;

import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.io.PreviewGenerator;
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
class Neo4jBoltTest {
    @Test
    void neo4jBoltMysqlFeaturesConfiguration(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("neo4j-bolt")).build();
        Map<String, String> project = previewGenerator.generate(options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        assertEquals("bolt://localhost", applicationProperties.getProperty("neo4j.uri"));
    }

    @Test
    void neo4jBoltFeaturesAddsTheDependency(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("neo4j-bolt")).build();
        Map<String, String> project = previewGenerator.generate(options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("io.micronaut.neo4j", "micronaut-neo4j-bolt", Scope.COMPILE), buildGradle);
        assertTrue(verifier.hasDependency("org.neo4j.test", "neo4j-harness", Scope.TEST_RUNTIME), buildGradle);
    }

    @Test
    void neo4jBoltFeaturesAddsTheLinkInReadmeFile(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("neo4j-bolt")).build();
        Map<String, String> project = previewGenerator.generate(options);
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://micronaut-projects.github.io/micronaut-neo4j/latest/guide/index.html"));
    }
}
