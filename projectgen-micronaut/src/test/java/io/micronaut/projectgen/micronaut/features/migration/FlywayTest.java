package io.micronaut.projectgen.micronaut.features.migration;

import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.buildtools.Phase;
import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.buildtools.Source;
import io.micronaut.projectgen.core.buildtools.maven.MavenScope;
import io.micronaut.projectgen.core.io.MapOutputHandler;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.micronaut.MicronautOptions;
import io.micronaut.projectgen.micronaut.MicronautProjectGenerator;
import io.micronaut.projectgen.test.BuildTestVerifier;
import io.micronaut.projectgen.test.ConfigurationUtils;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class FlywayTest {

    @Inject
    MicronautProjectGenerator micronautProjectGenerator;

    @Test
    void flywayConfiguration() throws Exception {
        MicronautOptions options = MicronautOptions.builder().feature("flyway").build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        assertEquals(StringUtils.TRUE, applicationProperties.getProperty("flyway.datasources.default.enabled"));
    }

    @Test
    void flywayFeaturesAddsTheDependency() throws Exception {
        MicronautOptions options = MicronautOptions.builder().feature("flyway").build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("io.micronaut.flyway", "micronaut-flyway", Scope.COMPILE), buildGradle);
    }

    @ParameterizedTest
    @MethodSource("flywayDependencies")
    void testProjectNaturalName(String groupId, String artifactId, String feature) throws Exception {
        MicronautOptions options = MicronautOptions.builder()
            .feature("flyway")
            .feature(feature)
            .build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency(groupId, artifactId, Scope.RUNTIME), buildGradle);
    }

    private static Stream<Arguments> flywayDependencies() {
        return Stream.of(
            Arguments.of("org.flywaydb", "flyway-sqlserver", "sqlserver"),
            Arguments.of("org.flywaydb", "flyway-mysql", "mysql"),
            Arguments.of("org.flywaydb", "flyway-mysql", "mariadb"),
            Arguments.of("org.flywaydb", "flyway-database-postgresql", "postgres"),
            Arguments.of("org.flywaydb", "flyway-database-oracle", "oracle")
        );
    }

    @Test
    void flywayFeaturesAddsTheLinkInReadmeFile(MicronautProjectGenerator micronautProjectGenerator) throws Exception {
        MicronautOptions options = MicronautOptions.builder().feature("flyway").build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://micronaut-projects.github.io/micronaut-flyway/latest/guide/index.html"));
        assertTrue(readme.contains("https://flywaydb.org/"));

    }

    private static Map<String, String> generateProject(MicronautProjectGenerator micronautProjectGenerator,
                                                       MicronautOptions options) throws Exception {
        MapOutputHandler outputHandler = new MapOutputHandler();
        micronautProjectGenerator.generate(options, outputHandler);
        return outputHandler.getProject();
    }
}
