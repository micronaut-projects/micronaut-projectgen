package io.micronaut.projectgen.micronaut.features.consul;

import io.micronaut.projectgen.core.io.PreviewGenerator;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.OptionsFixture;
import io.micronaut.projectgen.test.ConfigurationUtils;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class ConsulTest {
    @Test
    void consulFeaturesConfiguration(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("consul", "config-consul")).build();
        Map<String, String> project = previewGenerator.generate(options);
        Properties bootstrapProperties  = ConfigurationUtils.loadBootstrapProperties(project);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        assertEquals("localhost:8500", bootstrapProperties.getProperty("consul.client.defaultZone"));
    }

    @Test
    void consulConfFeaturesConfiguration(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("consul")).build();
        Map<String, String> project = previewGenerator.generate(options);
        Properties applicationProperties  = ConfigurationUtils.loadApplicationProperties(project);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        assertEquals("localhost:8500", applicationProperties.getProperty("consul.client.defaultZone"));
    }
}
