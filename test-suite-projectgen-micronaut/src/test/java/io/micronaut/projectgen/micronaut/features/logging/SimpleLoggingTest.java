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
class SimpleLoggingTest {
    @Test
    void slf4jSimpleFeaturesAddsTheDependency(ProjectGenerator projectGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle("slf4j-simple");
        Map<String, String> project = generateProject(projectGenerator, options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("org.slf4j", "slf4j-simple", Scope.RUNTIME), buildGradle);
        assertTrue(project.containsKey("src/main/resources/simplelogger.properties"));
    }

    private static Map<String, String> generateProject(ProjectGenerator projectGenerator,
                                                       Options options) throws Exception {
        MapOutputHandler outputHandler = new MapOutputHandler();
        projectGenerator.generate(options, outputHandler);
        return outputHandler.getProject();
    }
}
