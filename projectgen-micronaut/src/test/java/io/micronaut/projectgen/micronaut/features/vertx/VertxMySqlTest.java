package io.micronaut.projectgen.micronaut.features.vertx;

import io.micronaut.core.util.StringUtils;
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
class VertxMySqlTest {
    @Test
    void vertxMysqlConfiguration(MicronautProjectGenerator micronautProjectGenerator) throws Exception {
        MicronautOptions options = MicronautOptions.builder().feature("vertx-mysql-client").build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        assertEquals("the-host", applicationProperties.getProperty("vertx.mysql.client.host"));
        assertEquals("3306", applicationProperties.getProperty("vertx.mysql.client.port"));
        assertEquals("the-db", applicationProperties.getProperty("vertx.mysql.client.database"));
//        assertEquals("user", applicationProperties.getProperty("vertx.mysql.client.database.user"));
//        assertEquals("password", applicationProperties.getProperty("vertx.mysql.client.database.password"));
//        assertEquals("5", applicationProperties.getProperty("vertx.mysql.client.database.maxSize"));
   }

    @Test
    void vertxMysqlFeaturesAddsTheDependency(MicronautProjectGenerator micronautProjectGenerator) throws Exception {
        MicronautOptions options = MicronautOptions.builder().feature("vertx-mysql-client").build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("io.micronaut.sql", "micronaut-vertx-mysql-client"));
    }

    private static Map<String, String> generateProject(MicronautProjectGenerator micronautProjectGenerator,
                                                       MicronautOptions options) throws Exception {
        MapOutputHandler outputHandler = new MapOutputHandler();
        micronautProjectGenerator.generate(options, outputHandler);
        return outputHandler.getProject();
    }
}
