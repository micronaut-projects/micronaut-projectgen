package io.micronaut.projectgen.micronaut.features.cli;

import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.gradle.GradleDsl;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.options.TestFramework;
import io.micronaut.projectgen.micronaut.OptionsFixture;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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
        File projectFolder = new File("src/test/resources");
        assertNotNull(projectFolder, "Project folder should not be null");
        Options options = MicronautCli.load(projectFolder);
        assertNotNull(options);
        assertEquals("default", options.template());
        assertEquals("com.example", options.packageName());
        assertEquals(TestFramework.JUNIT, options.testFramework());
        assertEquals(Language.JAVA, options.language());
        assertEquals(BuildTool.GRADLE, options.getBuildTool());
        assertEquals(GradleDsl.GROOVY, options.gradleDsl());

        List<String> expectedFeatures = List.of("app-name", "gradle", "http-client-test", "java",
            "java-application", "junit", "logback", "micronaut-aot", "micronaut-build",
            "micronaut-http-validation", "netty-server", "properties", "readme",
            "serialization-jackson", "shade", "static-resources");
        assertEquals(expectedFeatures, options.features());
    }

    @Test
    void testConfig() {
        Options options = OptionsFixture.defaultGradle()
            .packageName("com.example")
            .testFramework(TestFramework.JUNIT)
            .language(Language.JAVA)
            .features(List.of("oracle", "liquibase")).build();

        Map<String, Object> buildOptions = MicronautCli.config(options);
        assertEquals(options.template(), buildOptions.get("applicationType"));
        assertEquals(options.testFramework().toString(), buildOptions.get("testFramework"));
        assertEquals(options.packageName(), buildOptions.get("defaultPackage"));
        assertEquals(options.language().toString(), buildOptions.get("sourceLanguage"));
        assertEquals(options.features(), buildOptions.get("features"));
        assertEquals(MicronautCli.LEGACY_BUILD_TOOL_GRADLE_KOTLIN, buildOptions.get("buildTool"));
    }
}
