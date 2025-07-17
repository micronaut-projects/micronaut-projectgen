package io.micronaut.projectgen.micronaut.features.cli;

import io.micronaut.projectgen.core.buildtools.gradle.GradleDsl;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.OptionsFixture;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MicronautCliTest {

    @Test
    void testLegacyBuildToolName() {
        // Test Gradle Kotlin DSL
        Options gradleKotlinDsl = OptionsFixture.defaultGradle().gradleDsl(GradleDsl.KOTLIN).build();
        Optional<String> gradleKotlin = MicronautCli.legacyBuildToolName(gradleKotlinDsl);
        assertTrue(gradleKotlin.isPresent());
        assertEquals(MicronautCli.LEGACY_BUILD_TOOL_GRADLE_KOTLIN, gradleKotlin.get());

        // Test Gradle Groovy DSL
        Options gradleGroovyDsl = OptionsFixture.defaultGradle().gradleDsl(GradleDsl.GROOVY).build();
        Optional<String> gradleGroovy = MicronautCli.legacyBuildToolName(gradleGroovyDsl);
        assertTrue(gradleGroovy.isPresent());
        assertEquals(MicronautCli.LEGACY_BUILD_TOOL_GRADLE_GROOVY, gradleGroovy.get());

        //Test Maven
        Options mavenBuild = OptionsFixture.defaultMaven().build();
        Optional<String> maven = MicronautCli.legacyBuildToolName(mavenBuild);
        assertTrue(maven.isPresent());
        assertEquals(MicronautCli.LEGACY_BUILD_TOOL_MAVEN, maven.get());
    }

    @Test
    void testOptionsLoad() {
        //TODO write test for {@link MicronautCli#load}
    }

    @Test
    void testConfig() {
        //TODO write test for {@link MicronautCli#config}
    }
}
