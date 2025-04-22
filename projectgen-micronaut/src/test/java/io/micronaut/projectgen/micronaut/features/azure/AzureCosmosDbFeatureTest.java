package io.micronaut.projectgen.micronaut.features.azure;

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
class AzureCosmosDbFeatureTest {

    @Test
    void azureCosmosDbConfiguration(MicronautProjectGenerator micronautProjectGenerator) throws Exception {
        MicronautOptions options = MicronautOptions.builder().feature("azure-cosmos-db").build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        assertEquals(StringUtils.FALSE, applicationProperties.getProperty("azure.cosmos.endpoint-discovery-enabled"));
        assertEquals(StringUtils.TRUE, applicationProperties.getProperty("azure.cosmos.default-gateway-mode"));
        assertEquals("azure-cosmos-endpoint", applicationProperties.getProperty("azure.cosmos.endpoint"));
        assertEquals("SESSION", applicationProperties.getProperty("azure.cosmos.consistency-level"));
        assertEquals("pleasechangeme", applicationProperties.getProperty("azure.cosmos.key"));
    }

    @Test
    void azureCosmosDbFeaturesAddsTheDependency(MicronautProjectGenerator micronautProjectGenerator) throws Exception {
        MicronautOptions options = MicronautOptions.builder().feature("azure-cosmos-db").build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("io.micronaut.azure", "micronaut-azure-cosmos", Scope.COMPILE), buildGradle);
    }

    @Test
    void azureCosmosDbFeaturesAddsTheLinkInReadmeFile(MicronautProjectGenerator micronautProjectGenerator) throws Exception {
        MicronautOptions options = MicronautOptions.builder().feature("azure-cosmos-db").build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://micronaut-projects.github.io/micronaut-azure/latest/guide/#azureCosmosClient"));
        assertTrue(readme.contains("https://learn.microsoft.com/en-us/azure/cosmos-db/"));
    }

    private static Map<String, String> generateProject(MicronautProjectGenerator micronautProjectGenerator,
                                                       MicronautOptions options) throws Exception {
        MapOutputHandler outputHandler = new MapOutputHandler();
        micronautProjectGenerator.generate(options, outputHandler);
        return outputHandler.getProject();
    }
}

