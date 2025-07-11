package io.micronaut.projectgen.micronaut.features.database;

import io.micronaut.core.util.StringUtils;
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
class HibernateReactiveJpaTest {
    @ParameterizedTest
    @MethodSource("driverArguments")
    void testR2dbcWithMysql(String driver , String dbType, PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("hibernate-reactive-jpa", driver)).build();
        Map<String, String> project = previewGenerator.generate(options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        assertEquals(dbType, applicationProperties.getProperty("jpa.default.properties.hibernate.connection.db-type"));
    }

    static Stream<Arguments> driverArguments() {
        return Stream.of(
            Arguments.of("mariadb", "mariadb"),
            Arguments.of("mysql", "mysql"),
            Arguments.of("postgres", "postgres"),
            Arguments.of("sqlserver", "mssql")
        );
    }

    @Test
    void hibernateReactiveJpaFeaturesConfiguration(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("hibernate-reactive-jpa", "mysql")).build();
        Map<String, String> project = previewGenerator.generate(options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        assertEquals("update", applicationProperties.getProperty("jpa.default.properties.hibernate.hbm2ddl.auto"));
        assertEquals(StringUtils.TRUE, applicationProperties.getProperty("jpa.default.reactive"));
    }

    @Test
    void hibernateReactiveJpaWithMigrationFeaturesConfiguration(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("hibernate-reactive-jpa", "mysql", "flyway")).build();
        Map<String, String> project = previewGenerator.generate(options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        assertEquals("none", applicationProperties.getProperty("jpa.default.properties.hibernate.hbm2ddl.auto"));
    }

    @Test
    void hibernateReactiveJpaFeaturesAddsTheDependency(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("hibernate-reactive-jpa", "mysql")).build();
        Map<String, String> project = previewGenerator.generate(options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("io.micronaut.sql", "micronaut-hibernate-reactive", Scope.COMPILE), buildGradle);

    }
}
