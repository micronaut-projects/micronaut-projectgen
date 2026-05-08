package io.micronaut.projectgen.micronaut.features.awsparameterstore;

import io.micronaut.core.util.StringUtils;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
class AwsParameterStoreTest {
    @Test
    void awsParameterStoreFeaturesAddsTheDependency(ProjectGenerator micronautProjectGenerator) throws Exception {
        String name = "foo";
        Options options = OptionsFixture.defaultGradle().name(name).features(List.of("aws-parameter-store")).build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("io.micronaut.aws", "micronaut-aws-parameter-store"), buildGradle);

        Properties bootstrapProperties = ConfigurationUtils.loadBootstrapProperties(project);
        assertEquals(name, bootstrapProperties.getProperty("micronaut.application.name"));
        assertEquals(StringUtils.TRUE, bootstrapProperties.getProperty("micronaut.config-client.enabled"));
        assertEquals(StringUtils.FALSE, bootstrapProperties.getProperty("aws.distributed-configuration.search-active-environments"));
        assertEquals(StringUtils.TRUE, bootstrapProperties.getProperty("aws.client.system-manager.parameterstore.enabled"));
        assertEquals(StringUtils.FALSE, bootstrapProperties.getProperty("aws.distributed-configuration.search-common-application"));
    }

    @Test
    void awsParameterStoreFeaturesAddsTheLinkInReadmeFile(ProjectGenerator micronautProjectGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("aws-parameter-store")).build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://micronaut-projects.github.io/micronaut-aws/latest/guide/index.html#parametersStore"));
        assertTrue(readme.contains("https://docs.aws.amazon.com/systems-manager/latest/userguide/systems-manager-parameter-store.html"));
    }

    private static Map<String, String> generateProject(ProjectGenerator micronautProjectGenerator,
        Options options) throws Exception {
        MapOutputHandler outputHandler = new MapOutputHandler();
        micronautProjectGenerator.generate(options, outputHandler);
        return outputHandler.getProject();
    }
}
