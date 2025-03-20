package io.micronaut.projectgen.micronaut.features.email;

import io.micronaut.projectgen.core.io.MapOutputHandler;
import io.micronaut.projectgen.micronaut.MicronautOptions;
import io.micronaut.projectgen.micronaut.MicronautProjectGenerator;
import io.micronaut.projectgen.test.BuildTestVerifier;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@MicronautTest(startApplication = false)
class AmazonSesEmailTest {

    private static final MicronautOptions OPTIONS = MicronautOptions.builder().feature("email-amazon-ses").build();

    @Test
    void amazonSesEmailFeaturesAddsTheDependency(MicronautProjectGenerator micronautProjectGenerator) throws Exception {
        MapOutputHandler outputHandler = new MapOutputHandler();
        micronautProjectGenerator.generate(OPTIONS, outputHandler);
        Map<String, String> project = outputHandler.getProject();
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, OPTIONS);
        assertTrue(verifier.hasDependency("io.micronaut.email", "micronaut-email-amazon-ses"));
    }

    @Test
    void amazonSesEmailFeaturesAddsTheLinkInReadmeFile(MicronautProjectGenerator micronautProjectGenerator) throws Exception {
        MapOutputHandler outputHandler = new MapOutputHandler();
        micronautProjectGenerator.generate(OPTIONS, outputHandler);
        Map<String, String> project = outputHandler.getProject();
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://micronaut-projects.github.io/micronaut-email/latest/guide/index.html#ses"));
        assertTrue(readme.contains("https://aws.amazon.com/ses/"));
    }
}
