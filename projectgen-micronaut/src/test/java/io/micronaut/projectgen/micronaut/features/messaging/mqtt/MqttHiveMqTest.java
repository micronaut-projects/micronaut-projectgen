package io.micronaut.projectgen.micronaut.features.messaging.mqtt;

import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.io.PreviewGenerator;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.OptionsFixture;
import io.micronaut.projectgen.test.BuildTestVerifier;
import io.micronaut.projectgen.test.ConfigurationUtils;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
class MqttHiveMqTest {
    @Test
    void mqttConfiguration(PreviewGenerator generator) throws Exception {
        Options options = OptionsFixture.defaultGradle("mqtt-hivemq");
        Map<String, String> project = generator.generate(options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        assertEquals("${random.uuid}", applicationProperties.getProperty("mqtt.client.client-id"));
        // Test resources is added by default, hence mqtt.client.server-uri is not set
        assertNull(applicationProperties.getProperty("mqtt.client.server-uri"));
    }

    @Test
    void mqttHivemqFeaturesAddsTheDependency(PreviewGenerator generator) throws Exception {
        Options options = OptionsFixture.defaultGradle("mqtt-hivemq");
        Map<String, String> project = generator.generate(options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("io.micronaut.mqtt", "micronaut-mqtt-hivemq", Scope.COMPILE), buildGradle);
    }

    @Test
    void mqttHivemqFeaturesAddsTheLinkInReadmeFile(PreviewGenerator generator) throws Exception {
        Options options = OptionsFixture.defaultGradle("mqtt-hivemq");
        Map<String, String> project = generator.generate(options);
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://micronaut-projects.github.io/micronaut-mqtt/latest/guide/index.html#hiveMq"));
        assertTrue(readme.contains("https://github.com/hivemq/hivemq-mqtt-client"));

    }
}
