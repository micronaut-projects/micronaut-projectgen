package io.micronaut.projectgen.micronaut.features.logging;

import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.generator.ProjectGenerator;
import io.micronaut.projectgen.core.io.MapOutputHandler;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.OptionsFixture;
import io.micronaut.projectgen.test.BuildTestVerifier;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class Slf4jSimpleLoggerTest {
    @Test
    void slf4jSimpleLoggerFeaturesAddsTheDependency(ProjectGenerator projectGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle("slf4j-simple-logger");
        Map<String, String> project = generateProject(projectGenerator, options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("io.goodforgod", "slf4j-simple-logger", Scope.RUNTIME), buildGradle);
        assertTrue(buildGradle.contains("2.0.0"));
    }

    private static Map<String, String> generateProject(ProjectGenerator micronautProjectGenerator,
                                                       Options options) throws Exception {
        MapOutputHandler outputHandler = new MapOutputHandler();
        micronautProjectGenerator.generate(options, outputHandler);
        return outputHandler.getProject();
    }
}
