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
class LiquibaseSlf4jTest {
    @Test
    void liquibaseSlf4jFeaturesAddsTheDependency(ProjectGenerator projectGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle("liquibase-slf4j");
        Map<String, String> project = generateProject(projectGenerator, options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("com.mattbertolini", "liquibase-slf4j", Scope.RUNTIME), buildGradle);
        assertTrue(buildGradle.contains("5.0.0"));
    }

    @Test
    void liquibaseSlf4jFeaturesAddsTheLinkInReadmeFile(ProjectGenerator projectGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle("liquibase-slf4j");
        Map<String, String> project = generateProject(projectGenerator, options);
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://github.com/mattbertolini/liquibase-slf4j"));

    }

    private static Map<String, String> generateProject(ProjectGenerator projectGenerator,
                                                       Options options) throws Exception {
        MapOutputHandler outputHandler = new MapOutputHandler();
        projectGenerator.generate(options, outputHandler);
        return outputHandler.getProject();
    }
}
