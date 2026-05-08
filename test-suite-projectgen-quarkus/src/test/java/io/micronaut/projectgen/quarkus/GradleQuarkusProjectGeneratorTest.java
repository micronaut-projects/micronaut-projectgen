package io.micronaut.projectgen.quarkus;

import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.buildtools.gradle.GradleDsl;
import io.micronaut.projectgen.core.generator.ProjectGenerator;
import io.micronaut.projectgen.core.io.MapOutputHandler;
import io.micronaut.projectgen.core.io.PreviewGenerator;
import io.micronaut.projectgen.core.options.GenericOptionsBuilder;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.options.TestFramework;
import io.micronaut.projectgen.test.BuildTestVerifier;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
class GradleQuarkusProjectGeneratorTest {

    @Test
    void testGenerateQuarkusGradleProject(PreviewGenerator generator) throws Exception {
        Options options = GenericOptionsBuilder.builder()
            .group("org.acme")
            .packageName("org.acme")
            .artifact("code-with-quarkus")
            .buildTools(List.of(BuildTool.GRADLE))
            .gradleDsl(GradleDsl.KOTLIN)
            .testFramework(TestFramework.JUNIT)
            .features(List.of("rest-assured", "quarkus-junit5-mockito"))
            .build();
        Map<String, String> project = generator.generate(options);

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
        assertTrue(verifier.hasDependency("io.rest-assured", "rest-assured", Scope.TEST));
        assertTrue(verifier.hasDependency("io.quarkus", "quarkus-junit5-mockito", Scope.TEST));

    }

}
