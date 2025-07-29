package io.micronaut.projectgen.micronaut.features.database;

import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.buildtools.BuildTool;
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
class DataAzureCosmosFeatureTest {
    @Test
    void dataAzureCosmosFeaturesConfiguration(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("data-azure-cosmos")).build();
        Map<String, String> project = previewGenerator.generate(options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        assertEquals(StringUtils.FALSE, applicationProperties.getProperty("azure.cosmos.endpoint-discovery-enabled"));
        assertEquals("myDb", applicationProperties.getProperty("azure.cosmos.database.database-name"));
        assertEquals("NONE", applicationProperties.getProperty("azure.cosmos.database.update-policy"));
        assertEquals("SESSION", applicationProperties.getProperty("azure.cosmos.consistency-level"));
        assertEquals(StringUtils.TRUE, applicationProperties.getProperty("azure.cosmos.default-gateway-mode"));
    }

    @Test
    void dataAzureCosmosFeaturesAddsTheDependency(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("data-azure-cosmos")).build();
        Map<String, String> project = previewGenerator.generate(options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("io.micronaut.data", "micronaut-data-azure-cosmos", Scope.COMPILE), buildGradle);
        assertTrue(verifier.hasAnnotationProcessor("io.micronaut.data", "micronaut-data-document-processor"), buildGradle);

    }

    @Test
    void dataAzureCosmosWithMavenFeaturesAddsTheDependency(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultMaven().features(List.of("data-azure-cosmos")).build();
        Map<String, String> project = previewGenerator.generate(options);
        String pom = project.get("pom.xml");
        assertNotNull(pom);
        BuildTestVerifier verifier = BuildTestVerifier.of(pom, BuildTool.MAVEN, options.language(), options.testFramework());
        assertTrue(verifier.hasDependency("io.micronaut.data", "micronaut-data-azure-cosmos", Scope.COMPILE), pom);
        assertTrue(verifier.hasAnnotationProcessor("io.micronaut.data", "micronaut-data-document-processor"), pom);
        assertTrue(verifier.hasAnnotationProcessor("io.micronaut.data", "micronaut-data-processor"), pom);

    }

    @Test
    void dataAzureCosmosFeaturesAddsTheLinkInReadmeFile(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("data-azure-cosmos")).build();
        Map<String, String> project = previewGenerator.generate(options);
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://micronaut-projects.github.io/micronaut-data/latest/guide/#azureCosmos"));
        assertTrue(readme.contains("https://learn.microsoft.com/en-us/azure/cosmos-db/"));
    }
}
