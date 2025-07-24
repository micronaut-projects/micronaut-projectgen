package io.micronaut.projectgen.micronaut.features.database;

import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.io.PreviewGenerator;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.OptionsFixture;
import io.micronaut.projectgen.test.BuildTestVerifier;
import io.micronaut.projectgen.test.ConfigurationUtils;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class DataJdbcTest {
    @Test
    void dataJdbcFeaturesConfiguration(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("data-jdbc")).build();
        Map<String, String> project = previewGenerator.generate(options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        assertEquals("H2", applicationProperties.getProperty("datasources.default.dialect"));
        assertEquals("CREATE_DROP", applicationProperties.getProperty("datasources.default.schema-generate"));
        assertEquals("jdbc:h2:mem:devDb;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE", applicationProperties.getProperty("datasources.default.url"));
        assertEquals("org.h2.Driver", applicationProperties.getProperty("datasources.default.driver-class-name"));
        assertEquals("sa", applicationProperties.getProperty("datasources.default.username"));
        assertEquals("", applicationProperties.getProperty("datasources.default.password"));
    }

    @Test
    void dataJdbcFeaturesAddsTheDependency(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("data-jdbc")).build();
        Map<String, String> project = previewGenerator.generate(options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("io.micronaut.data", "micronaut-data-jdbc", Scope.COMPILE), buildGradle);
        assertTrue(verifier.hasAnnotationProcessor("io.micronaut.data", "micronaut-data-processor"), buildGradle);
    }

    @Test
    void dataJdbcFeaturesAddsTheLinkInReadmeFile(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("data-jdbc")).build();
        Map<String, String> project = previewGenerator.generate(options);
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://micronaut-projects.github.io/micronaut-data/latest/guide/index.html#jdbc"));
    }

    @ParameterizedTest
    @MethodSource("driversArguments")
    void testDataJdbcDriversConfiguration(String driver , String dialect, PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of(driver, "data-jdbc")).build();
        Map<String, String> project = previewGenerator.generate(options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        assertEquals(dialect, applicationProperties.getProperty("datasources.default.dialect"));
    }

    static Stream<Arguments> driversArguments() {
        return Stream.of(
            Arguments.of("postgres", "POSTGRES"),
            Arguments.of("mysql", "MYSQL"),
            Arguments.of("mariadb", "MYSQL"),
            Arguments.of("sqlserver", "SQL_SERVER"),
            Arguments.of("oracle", "ORACLE")
        );
    }
}
