package io.micronaut.projectgen.micronaut.features.messaging.jms;

import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.generator.ProjectGenerator;
import io.micronaut.projectgen.core.io.MapOutputHandler;
import io.micronaut.projectgen.core.io.PreviewGenerator;
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
class ActiveMqArtemisTest {
    @Test
    void activeMqArtemisConfiguration(PreviewGenerator generator) throws Exception {
        Options options = OptionsFixture.defaultGradle("jms-activemq-artemis");
        Map<String, String> project = generator.generate(options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        assertEquals(StringUtils.TRUE, applicationProperties.getProperty("micronaut.jms.activemq.artemis.enabled"));
        assertEquals("tcp://localhost:61616", applicationProperties.getProperty("micronaut.jms.activemq.artemis.connection-string"));
    }

    @Test
    void activeMqArtemisFeaturesAddsTheDependency(PreviewGenerator generator) throws Exception {
        Options options = OptionsFixture.defaultGradle("jms-activemq-artemis");
        Map<String, String> project = generator.generate(options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("io.micronaut.jms", "micronaut-jms-activemq-artemis", Scope.COMPILE), buildGradle);
    }

    @Test
    void activeMqArtemisFeaturesAddsTheLinkInReadmeFile(PreviewGenerator generator) throws Exception {
        Options options = OptionsFixture.defaultGradle("jms-activemq-artemis");
        Map<String, String> project = generator.generate(options);
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://micronaut-projects.github.io/micronaut-jms/snapshot/guide/index.html"));

    }
}
