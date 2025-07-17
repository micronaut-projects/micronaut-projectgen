package io.micronaut.projectgen.micronaut.features.dev;

import io.micronaut.core.util.StringUtils;
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
public class ControlPanelTest {
    @Test
    void controlPanelFeaturesConfiguration(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("control-panel")).build();
        Map<String, String> project = previewGenerator.generate(options);
        Properties applicationDevProperties = ConfigurationUtils.loadDevProperties(project);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        assertEquals(StringUtils.TRUE, applicationDevProperties.getProperty("endpoints.all.enabled"));
        assertEquals(StringUtils.FALSE, applicationDevProperties.getProperty("endpoints.loggers.write-sensitive"));
        assertEquals(StringUtils.FALSE, applicationDevProperties.getProperty("endpoints.all.sensitive"));
        assertEquals("ANONYMOUS", applicationDevProperties.getProperty("endpoints.health.details-visible"));
    }

    @Test
    void controlPanelFeaturesAddsTheDependency(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("control-panel")).build();
        Map<String, String> project = previewGenerator.generate(options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("io.micronaut.controlpanel", "micronaut-control-panel-management", Scope.DEVELOPMENT_ONLY), buildGradle);
        assertTrue(verifier.hasDependency("io.micronaut.controlpanel", "micronaut-control-panel-ui", Scope.DEVELOPMENT_ONLY), buildGradle);
    }

    @Test
    void controlPanelFeaturesAddsTheLinkInReadmeFile(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("control-panel")).build();
        Map<String, String> project = previewGenerator.generate(options);
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://micronaut-projects.github.io/micronaut-control-panel/latest/guide/index.html"));
    }
}
