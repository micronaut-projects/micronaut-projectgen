package io.micronaut.projectgen.quarkus;

import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.io.MapOutputHandler;
import io.micronaut.projectgen.test.BuildTestVerifier;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class GradleQuarkusProjectGeneratorTest {

    @Test
    void testGenerateQuarkusGradleProject(QuarkusProjectGenerator projectGenerator) throws Exception {
        MapOutputHandler outputHandler = new MapOutputHandler();
        QuarkusOptions options = QuarkusOptions.builder().buildTool(BuildTool.GRADLE_KOTLIN).build();
        projectGenerator.generate(options, outputHandler);
        Map<String, String> project = outputHandler.getProject();

        Set<String> expected = Set.of(
            ".gitignore",
            "settings.gradle.kts",
            "build.gradle.kts",
            "gradlew",
            "gradlew.bat",
            "gradle/wrapper/gradle-wrapper.jar",
            "gradle/wrapper/gradle-wrapper.properties"
        );
        Set<String> keys = project.keySet();
        assertEquals(expected, keys);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        String settingsGradle = project.get("settings.gradle.kts");
        assertNotNull(settingsGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasBuildPlugin("java"));
        assertTrue(verifier.hasBuildPlugin("io.quarkus"));
        assertTrue(verifier.hasBom("io.quarkus.platform", "quarkus-bom", Scope.COMPILE));
        assertTrue(verifier.hasDependency("io.quarkus", "quarkus-rest", Scope.COMPILE));
        assertTrue(verifier.hasDependency("io.quarkus", "quarkus-arc", Scope.COMPILE));
        assertTrue(verifier.hasDependency("io.quarkus", "quarkus-junit5", Scope.TEST));
    }

}
