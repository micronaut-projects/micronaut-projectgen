package io.micronaut.projectgen.micronaut.features;

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
class DbcpTest {
    @Test
    void dbcpFeaturesConfiguration(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("jdbc-dbcp")).build();
        Map<String, String> project = previewGenerator.generate(options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        assertEquals("jdbc:h2:mem:devDb;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE", applicationProperties.getProperty("datasources.default.url"));
        assertEquals("org.h2.Driver", applicationProperties.getProperty("datasources.default.driver-class-name"));
        assertEquals("sa", applicationProperties.getProperty("datasources.default.username"));
        assertEquals("", applicationProperties.getProperty("datasources.default.password"));
    }

    @Test
    void dbcpFeaturesAddsTheDependency(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("jdbc-dbcp")).build();
        Map<String, String> project = previewGenerator.generate(options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("io.micronaut.sql", "micronaut-jdbc-dbcp", Scope.COMPILE), buildGradle);
    }

    @Test
    void dbcpFeaturesAddsTheLinkInReadmeFile(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("jdbc-dbcp")).build();
        Map<String, String> project = previewGenerator.generate(options);
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://micronaut-projects.github.io/micronaut-sql/latest/guide/index.html#jdbc"));
    }
}
