package io.micronaut.projectgen.micronaut.features.database.r2dbc;

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
class R2dbcConfigurationTest {

    private static final String R2DBC_DATASOURCES_DEFAULT_DB_TYPE = "r2dbc.datasources.default.db-type";
    private static final String R2DBC_DATASOURCES_DEFAULT_DIALECT = "r2dbc.datasources.default.dialect";
    private static final String R2DBC_DATASOURCES_DEFAULT_URL = "r2dbc.datasources.default.url";
    private static final String R2DBC_DATASOURCES_DEFAULT_USERNAME = "r2dbc.datasources.default.username";
    private static final String R2DBC_DATASOURCES_DEFAULT_PASSWORD = "r2dbc.datasources.default.password";

    @ParameterizedTest
    @MethodSource("r2dbcArguments")
    void h2DriverUsedByDefaultWhenAddingR2dbc(String r2dbcFeature, PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of(r2dbcFeature)).build();
        Map<String, String> project = previewGenerator.generate(options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        assertEquals("r2dbc:h2:mem:///testdb;DB_CLOSE_ON_EXIT=FALSE", applicationProperties.getProperty(R2DBC_DATASOURCES_DEFAULT_URL));
        assertEquals("sa", applicationProperties.getProperty(R2DBC_DATASOURCES_DEFAULT_USERNAME));
        assertEquals("", applicationProperties.getProperty(R2DBC_DATASOURCES_DEFAULT_PASSWORD));
    }

    @ParameterizedTest
    @MethodSource("r2dbcArguments")
    void testR2dbcWithPostgres(String r2dbcFeature, PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of(r2dbcFeature, "postgres")).build();
        Map<String, String> project = previewGenerator.generate(options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        assertEquals("POSTGRES", applicationProperties.getProperty(R2DBC_DATASOURCES_DEFAULT_DIALECT));
        assertEquals("postgres", applicationProperties.getProperty(R2DBC_DATASOURCES_DEFAULT_DB_TYPE));
        assertNull(applicationProperties.getProperty(R2DBC_DATASOURCES_DEFAULT_USERNAME));
        assertNull(applicationProperties.getProperty(R2DBC_DATASOURCES_DEFAULT_PASSWORD));
    }

    @ParameterizedTest
    @MethodSource("r2dbcArguments")
    void testR2dbcWithMysql(String r2dbcFeature, PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of(r2dbcFeature, "mysql")).build();
        Map<String, String> project = previewGenerator.generate(options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        assertEquals("MYSQL", applicationProperties.getProperty(R2DBC_DATASOURCES_DEFAULT_DIALECT));
        assertEquals("mysql", applicationProperties.getProperty(R2DBC_DATASOURCES_DEFAULT_DB_TYPE));
        assertNull(applicationProperties.getProperty(R2DBC_DATASOURCES_DEFAULT_USERNAME));
        assertNull(applicationProperties.getProperty(R2DBC_DATASOURCES_DEFAULT_PASSWORD));
    }

    @ParameterizedTest
    @MethodSource("r2dbcArguments")
    void testR2dbcWithOracle(String r2dbcFeature, PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of(r2dbcFeature, "oracle")).build();
        Map<String, String> project = previewGenerator.generate(options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        assertEquals("ORACLE", applicationProperties.getProperty(R2DBC_DATASOURCES_DEFAULT_DIALECT));
        assertEquals("oracle", applicationProperties.getProperty(R2DBC_DATASOURCES_DEFAULT_DB_TYPE));
        assertNull(applicationProperties.getProperty(R2DBC_DATASOURCES_DEFAULT_USERNAME));
        assertNull(applicationProperties.getProperty(R2DBC_DATASOURCES_DEFAULT_PASSWORD));
    }

    @ParameterizedTest
    @MethodSource("r2dbcArguments")
    void testR2dbcWithSqlserver(String r2dbcFeature, PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of(r2dbcFeature, "sqlserver")).build();
        Map<String, String> project = previewGenerator.generate(options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        assertEquals("SQL_SERVER", applicationProperties.getProperty(R2DBC_DATASOURCES_DEFAULT_DIALECT));
        assertEquals("mssql", applicationProperties.getProperty(R2DBC_DATASOURCES_DEFAULT_DB_TYPE));
        assertEquals("false", applicationProperties.getProperty("test-resources.containers.mssql.accept-license"));
        assertNull(applicationProperties.getProperty(R2DBC_DATASOURCES_DEFAULT_USERNAME));
        assertNull(applicationProperties.getProperty(R2DBC_DATASOURCES_DEFAULT_PASSWORD));
    }

    @ParameterizedTest
    @MethodSource("r2dbcArguments")
    void testR2dbcWithMariadb(String r2dbcFeature, PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of(r2dbcFeature, "mariadb")).build();
        Map<String, String> project = previewGenerator.generate(options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        assertEquals("MYSQL", applicationProperties.getProperty(R2DBC_DATASOURCES_DEFAULT_DIALECT));
        assertEquals("mariadb", applicationProperties.getProperty(R2DBC_DATASOURCES_DEFAULT_DB_TYPE));
        assertNull(applicationProperties.getProperty(R2DBC_DATASOURCES_DEFAULT_USERNAME));
        assertNull(applicationProperties.getProperty(R2DBC_DATASOURCES_DEFAULT_PASSWORD));
    }

    static Stream<Arguments> r2dbcArguments() {
        return Stream.of(
            Arguments.of("r2dbc"),
            Arguments.of("data-r2dbc"),
            Arguments.of("r2dbc-pool")
        );
    }
}
