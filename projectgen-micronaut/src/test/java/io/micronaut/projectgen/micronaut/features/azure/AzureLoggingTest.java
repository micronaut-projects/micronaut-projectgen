package io.micronaut.projectgen.micronaut.features.azure;

import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.generator.ProjectGenerator;
import io.micronaut.projectgen.core.io.MapOutputHandler;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.OptionsFixture;
import io.micronaut.projectgen.test.BuildTestVerifier;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
class AzureLoggingTest {

    @Test
    void azureLoggingFeaturesAddsTheDependency(ProjectGenerator micronautProjectGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("azure-logging")).build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("io.micronaut.azure", "micronaut-azure-logging", Scope.COMPILE), buildGradle);
    }

    @Test
    void azureLoggingFeaturesAddsTheLinkInReadmeFile(ProjectGenerator micronautProjectGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("azure-logging")).build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://micronaut-projects.github.io/micronaut-azure/latest/guide/#azureLogging"));
        assertTrue(readme.contains("https://learn.microsoft.com/en-us/azure/azure-monitor/logs/data-platform-logs"));
    }

    private static Map<String, String> generateProject(ProjectGenerator micronautProjectGenerator,
        Options options) throws Exception {
        MapOutputHandler outputHandler = new MapOutputHandler();
        micronautProjectGenerator.generate(options, outputHandler);
        return outputHandler.getProject();
    }
}
