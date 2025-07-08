package io.micronaut.projectgen.micronaut.features.database.jdbc;

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
class HikariTest {
    private static final String DATASOURCES_DEFAULT_DB_TYPE = "datasources.default.db-type";
    private static final String DATASOURCES_DEFAULT_URL = "datasources.default.url";
    private static final String DATASOURCES_DEFAULT_DRIVER_CLASS_NAME = "datasources.default.driver-class-name";
    private static final String DATASOURCES_DEFAULT_USERNAME = "datasources.default.username";
    private static final String DATASOURCES_DEFAULT_PASSWORD = "datasources.default.password";

    @Test
    void h2DriverUsedByDefaultWhenAddingJdbcHikari(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("jdbc-hikari")).build();
        Map<String, String> project = previewGenerator.generate(options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        assertEquals("jdbc:h2:mem:devDb;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE", applicationProperties.getProperty(DATASOURCES_DEFAULT_URL));
        assertEquals("org.h2.Driver", applicationProperties.getProperty(DATASOURCES_DEFAULT_DRIVER_CLASS_NAME));
        assertEquals("sa", applicationProperties.getProperty(DATASOURCES_DEFAULT_USERNAME));
        assertEquals("", applicationProperties.getProperty(DATASOURCES_DEFAULT_PASSWORD));
    }

    @Test
    void postgresqlWhenAddingJdbcHikari(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("jdbc-hikari", "postgres")).build();
        Map<String, String> project = previewGenerator.generate(options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        assertEquals("postgres", applicationProperties.getProperty(DATASOURCES_DEFAULT_DB_TYPE));
        assertEquals("org.postgresql.Driver", applicationProperties.getProperty(DATASOURCES_DEFAULT_DRIVER_CLASS_NAME));
        assertNull(applicationProperties.getProperty(DATASOURCES_DEFAULT_USERNAME));
        assertNull(applicationProperties.getProperty(DATASOURCES_DEFAULT_PASSWORD));
    }

    @Test
    void mysqlWhenAddingJdbcHikari(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("jdbc-hikari", "mysql")).build();
        Map<String, String> project = previewGenerator.generate(options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        assertEquals("mysql", applicationProperties.getProperty(DATASOURCES_DEFAULT_DB_TYPE));
        assertEquals("com.mysql.cj.jdbc.Driver", applicationProperties.getProperty(DATASOURCES_DEFAULT_DRIVER_CLASS_NAME));
        assertNull(applicationProperties.getProperty(DATASOURCES_DEFAULT_USERNAME));
        assertNull(applicationProperties.getProperty(DATASOURCES_DEFAULT_PASSWORD));
    }

    @Test
    void oracleWhenAddingJdbcHikari(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("jdbc-hikari", "oracle")).build();
        Map<String, String> project = previewGenerator.generate(options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        assertEquals("oracle", applicationProperties.getProperty(DATASOURCES_DEFAULT_DB_TYPE));
        assertEquals("oracle.jdbc.OracleDriver", applicationProperties.getProperty(DATASOURCES_DEFAULT_DRIVER_CLASS_NAME));
        assertNull(applicationProperties.getProperty(DATASOURCES_DEFAULT_USERNAME));
        assertNull(applicationProperties.getProperty(DATASOURCES_DEFAULT_PASSWORD));
    }

    @Test
    void sqlserverWhenAddingJdbcHikari(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("jdbc-hikari", "sqlserver")).build();
        Map<String, String> project = previewGenerator.generate(options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        assertEquals("mssql", applicationProperties.getProperty(DATASOURCES_DEFAULT_DB_TYPE));
        assertEquals("com.microsoft.sqlserver.jdbc.SQLServerDriver", applicationProperties.getProperty(DATASOURCES_DEFAULT_DRIVER_CLASS_NAME));
        assertEquals("false", applicationProperties.getProperty("test-resources.containers.mssql.accept-license"));
    }

    @Test
    void mariadbWhenAddingJdbcHikari(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("jdbc-hikari", "mariadb")).build();
        Map<String, String> project = previewGenerator.generate(options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        assertEquals("mariadb", applicationProperties.getProperty(DATASOURCES_DEFAULT_DB_TYPE));
        assertEquals("org.mariadb.jdbc.Driver", applicationProperties.getProperty(DATASOURCES_DEFAULT_DRIVER_CLASS_NAME));
    }

    @Test
    void hikariFeaturesAddsTheDependency(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("jdbc-hikari")).build();
        Map<String, String> project = previewGenerator.generate(options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("io.micronaut.sql", "micronaut-jdbc-hikari", Scope.COMPILE), buildGradle);
    }

    @Test
    void hikariFeaturesAddsTheLinkInReadmeFile(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("jdbc-hikari")).build();
        Map<String, String> project = previewGenerator.generate(options);
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://micronaut-projects.github.io/micronaut-sql/latest/guide/index.html#jdbc"));
    }
}
