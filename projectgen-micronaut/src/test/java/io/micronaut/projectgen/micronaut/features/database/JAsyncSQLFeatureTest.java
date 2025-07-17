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
class JAsyncSQLFeatureTest {
    @Test
    void jasyncSqlFeaturesConfiguration(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("jasync-sql", "mysql")).build();
        Map<String, String> project = previewGenerator.generate(options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        assertEquals("5432", applicationProperties.getProperty("jasync.client.port"));
        assertEquals("the-host", applicationProperties.getProperty("jasync.client.host"));
        assertEquals("the-db", applicationProperties.getProperty("jasync.client.database"));
        assertEquals("test", applicationProperties.getProperty("jasync.client.username"));
        assertEquals("test", applicationProperties.getProperty("jasync.client.password"));
        assertEquals("5", applicationProperties.getProperty("jasync.client.maxActiveConnections"));
    }

    @Test
    void jasyncSqlFeaturesAddsTheDependency(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("jasync-sql", "mysql")).build();
        Map<String, String> project = previewGenerator.generate(options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("io.micronaut.sql", "micronaut-jasync-sql", Scope.COMPILE), buildGradle);
    }

    @Test
    void jasyncSQlFeaturesAddsTheLinkInReadmeFile(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("jasync-sql", "mysql")).build();
        Map<String, String> project = previewGenerator.generate(options);
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://micronaut-projects.github.io/micronaut-sql/latest/guide/index.html#jasync"));
        assertTrue(readme.contains("https://github.com/jasync-sql/jasync-sql/wiki"));
    }
}
