package io.micronaut.projectgen.micronaut.features.messaging.mqtt;

import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.generator.ProjectGenerator;
import io.micronaut.projectgen.core.io.MapOutputHandler;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.OptionsFixture;
import io.micronaut.projectgen.test.BuildTestVerifier;
import io.micronaut.projectgen.test.ConfigurationUtils;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class MqttTest {
    @Test
    void mqttConfiguration(ProjectGenerator projectGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle("mqtt");
        Map<String, String> project = generateProject(projectGenerator, options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        assertEquals("${random.uuid}", applicationProperties.getProperty("mqtt.client.client-id"));

    }

    @Test
    void mqttFeaturesAddsTheDependency(ProjectGenerator projectGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle("mqtt");
        Map<String, String> project = generateProject(projectGenerator, options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("io.micronaut.mqtt", "micronaut-mqttv5", Scope.COMPILE), buildGradle);
    }

    @Test
    void mqttFeaturesAddsTheLinkInReadmeFile(ProjectGenerator projectGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle("mqtt");
        Map<String, String> project = generateProject(projectGenerator, options);
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://micronaut-projects.github.io/micronaut-mqtt/latest/guide/index.html"));

    }

    private static Map<String, String> generateProject(ProjectGenerator projectGenerator,
                                                       Options options) throws Exception {
        MapOutputHandler outputHandler = new MapOutputHandler();
        projectGenerator.generate(options, outputHandler);
        return outputHandler.getProject();
    }
}
