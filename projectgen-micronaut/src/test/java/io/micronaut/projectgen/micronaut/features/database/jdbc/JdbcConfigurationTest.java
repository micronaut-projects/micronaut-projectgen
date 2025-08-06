package io.micronaut.projectgen.micronaut.features.database.jdbc;

import io.micronaut.projectgen.core.io.PreviewGenerator;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.OptionsFixture;
import io.micronaut.projectgen.test.ConfigurationUtils;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@MicronautTest(startApplication = false)
class JdbcConfigurationTest {

    private static final String DATASOURCES_DEFAULT_DB_TYPE = "datasources.default.db-type";
    private static final String DATASOURCES_DEFAULT_URL = "datasources.default.url";
    private static final String DATASOURCES_DEFAULT_DRIVER_CLASS_NAME = "datasources.default.driver-class-name";
    private static final String DATASOURCES_DEFAULT_USERNAME = "datasources.default.username";
    private static final String DATASOURCES_DEFAULT_PASSWORD = "datasources.default.password";

    @ParameterizedTest
    @MethodSource("jdbcArguments")
    void h2DriverUsedByDefaultWhenAddingJdbc(String jdbcFeature, PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of(jdbcFeature)).build();
        Map<String, String> project = previewGenerator.generate(options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        assertEquals("jdbc:h2:mem:devDb;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE", applicationProperties.getProperty(DATASOURCES_DEFAULT_URL));
        assertEquals("org.h2.Driver", applicationProperties.getProperty(DATASOURCES_DEFAULT_DRIVER_CLASS_NAME));
        assertEquals("sa", applicationProperties.getProperty(DATASOURCES_DEFAULT_USERNAME));
        assertEquals("", applicationProperties.getProperty(DATASOURCES_DEFAULT_PASSWORD));
    }

    @ParameterizedTest
    @MethodSource("jdbcArguments")
    void testJdbcWithPostgres(String jdbcFeature, PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of(jdbcFeature, "postgres")).build();
        Map<String, String> project = previewGenerator.generate(options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        assertEquals("postgres", applicationProperties.getProperty(DATASOURCES_DEFAULT_DB_TYPE));
        assertEquals("org.postgresql.Driver", applicationProperties.getProperty(DATASOURCES_DEFAULT_DRIVER_CLASS_NAME));
        assertNull(applicationProperties.getProperty(DATASOURCES_DEFAULT_USERNAME));
        assertNull(applicationProperties.getProperty(DATASOURCES_DEFAULT_PASSWORD));
    }

    @ParameterizedTest
    @MethodSource("jdbcArguments")
    void testJdbcWithMysql(String jdbcFeature, PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of(jdbcFeature, "mysql")).build();
        Map<String, String> project = previewGenerator.generate(options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        assertEquals("mysql", applicationProperties.getProperty(DATASOURCES_DEFAULT_DB_TYPE));
        assertEquals("com.mysql.cj.jdbc.Driver", applicationProperties.getProperty(DATASOURCES_DEFAULT_DRIVER_CLASS_NAME));
        assertNull(applicationProperties.getProperty(DATASOURCES_DEFAULT_USERNAME));
        assertNull(applicationProperties.getProperty(DATASOURCES_DEFAULT_PASSWORD));
    }

    @ParameterizedTest
    @MethodSource("jdbcArguments")
    void testJdbcWithOracle(String jdbcFeature, PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of(jdbcFeature, "oracle")).build();
        Map<String, String> project = previewGenerator.generate(options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        assertEquals("oracle", applicationProperties.getProperty(DATASOURCES_DEFAULT_DB_TYPE));
        assertEquals("oracle.jdbc.OracleDriver", applicationProperties.getProperty(DATASOURCES_DEFAULT_DRIVER_CLASS_NAME));
        assertNull(applicationProperties.getProperty(DATASOURCES_DEFAULT_USERNAME));
        assertNull(applicationProperties.getProperty(DATASOURCES_DEFAULT_PASSWORD));
    }

    @ParameterizedTest
    @MethodSource("jdbcArguments")
    void testJdbcWithSqlserver(String jdbcFeature, PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of(jdbcFeature, "sqlserver")).build();
        Map<String, String> project = previewGenerator.generate(options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        assertEquals("mssql", applicationProperties.getProperty(DATASOURCES_DEFAULT_DB_TYPE));
        assertEquals("com.microsoft.sqlserver.jdbc.SQLServerDriver", applicationProperties.getProperty(DATASOURCES_DEFAULT_DRIVER_CLASS_NAME));
        assertEquals("false", applicationProperties.getProperty("test-resources.containers.mssql.accept-license"));
    }

    @ParameterizedTest
    @MethodSource("jdbcArguments")
    void testJdbcWithMariadb(String jdbcFeature, PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of(jdbcFeature, "mariadb")).build();
        Map<String, String> project = previewGenerator.generate(options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        assertEquals("mariadb", applicationProperties.getProperty(DATASOURCES_DEFAULT_DB_TYPE));
        assertEquals("org.mariadb.jdbc.Driver", applicationProperties.getProperty(DATASOURCES_DEFAULT_DRIVER_CLASS_NAME));
    }

    static Stream<Arguments> jdbcArguments() {
        return Stream.of(
            Arguments.of("jdbc-hikari"),
            Arguments.of("jdbc-dbcp"),
            Arguments.of("jdbc-tomcat"),
            Arguments.of("jdbc-ucp")
        );
    }
}
