package io.micronaut.projectgen.micronaut.features.oraclecloud;

import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.generator.ProjectGenerator;
import io.micronaut.projectgen.core.io.MapOutputHandler;
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
class OracleCloudVaultTest {
    @Test
    void oracleCloudVaultConfiguration(ProjectGenerator micronautProjectGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("oracle-cloud-vault")).build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        Properties bootstrapProperties = ConfigurationUtils.loadBootstrapProperties(project);
        assertEquals("DEFAULT", applicationProperties.getProperty("oci.config.profile"));
        assertEquals(StringUtils.TRUE, bootstrapProperties.getProperty("oci.vault.config.enabled"));
        assertEquals("changeme", bootstrapProperties.getProperty("oci.vault.vaults.ocid"));
        assertEquals("changeme", bootstrapProperties.getProperty("oci.vault.vaults.compartment-ocid"));
    }

    @Test
    void oracleCloudVaultFeaturesAddsTheDependency(ProjectGenerator micronautProjectGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("oracle-cloud-vault")).build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("io.micronaut.oraclecloud", "micronaut-oraclecloud-vault", Scope.COMPILE), buildGradle);
    }

    @Test
    void oracleCloudVaultFeaturesAddsTheLinkInReadmeFile(ProjectGenerator micronautProjectGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("oracle-cloud-vault")).build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://micronaut-projects.github.io/micronaut-oracle-cloud/latest/guide/#vault"));
        assertTrue(readme.contains("https://docs.oracle.com/en-us/iaas/Content/KeyManagement/home.htm"));

    }

    private static Map<String, String> generateProject(ProjectGenerator micronautProjectGenerator,
        Options options) throws Exception {
        MapOutputHandler outputHandler = new MapOutputHandler();
        micronautProjectGenerator.generate(options, outputHandler);
        return outputHandler.getProject();
    }
}
