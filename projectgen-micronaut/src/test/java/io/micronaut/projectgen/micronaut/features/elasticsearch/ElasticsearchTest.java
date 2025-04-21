package io.micronaut.projectgen.micronaut.features.elasticsearch;

import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.buildtools.Scope;
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
class ElasticsearchTest {
    @Test
    void elasticSearchConfiguration(MicronautProjectGenerator micronautProjectGenerator) throws Exception {
        MicronautOptions options = MicronautOptions.builder().feature("elasticsearch").build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        assertEquals("http://localhost:9200,http://127.0.0.2:9200", applicationProperties.getProperty("elasticsearch.httpHosts"));
    }

    @Test
    void elasticSearchFeaturesAddsTheDependency(MicronautProjectGenerator micronautProjectGenerator) throws Exception {
        MicronautOptions options = MicronautOptions.builder().feature("elasticsearch").feature("graalvm").build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("io.micronaut.elasticsearch", "micronaut-elasticsearch", Scope.COMPILE), buildGradle);
        assertTrue(verifier.hasDependency("org.apache.logging.log4j", "log4j-api", Scope.COMPILE), buildGradle);
        assertTrue(verifier.hasDependency("org.slf4j", "log4j-over-slf4j", Scope.RUNTIME), buildGradle);
        assertTrue(verifier.hasDependency("org.apache.logging.log4j", "log4j-core", Scope.COMPILE), buildGradle);

    }

    @Test
    void elasticSearchFeaturesAddsTheLinkInReadmeFile(MicronautProjectGenerator micronautProjectGenerator) throws Exception {
        MicronautOptions options = MicronautOptions.builder().feature("elasticsearch").build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://micronaut-projects.github.io/micronaut-elasticsearch/latest/guide/index.html"));

    }

    private static Map<String, String> generateProject(MicronautProjectGenerator micronautProjectGenerator,
                                                       MicronautOptions options) throws Exception {
        MapOutputHandler outputHandler = new MapOutputHandler();
        micronautProjectGenerator.generate(options, outputHandler);
        return outputHandler.getProject();
    }
}
