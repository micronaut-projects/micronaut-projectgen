package io.micronaut.projectgen.micronaut;

import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.generator.ProjectGenerator;
import io.micronaut.projectgen.core.io.MapOutputHandler;
import io.micronaut.projectgen.core.options.JdkVersion;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.options.TestFramework;
import io.micronaut.projectgen.test.BuildTestVerifier;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class MicronautApplicationGenerationTest {
    private static final Set<String> EXPECT_FILES_FOR_BOTH = Set.of(
        ".gitignore",
        "src/main/resources/logback.xml",
        "src/main/resources/application.properties"
    );

    @Test
    void generateMicronautMavenApplication(ProjectGenerator projectGenerator) throws Exception {
        Options options = createOptions(List.of(BuildTool.MAVEN));
        MapOutputHandler outputHandler = new MapOutputHandler();
        projectGenerator.generate(options, outputHandler);
        Map<String, String> project = outputHandler.getProject();
        Set<String> expected = new HashSet<>(Set.of(
            "pom.xml",
            "mvnw",
            "mvnw.bat",
            ".mvn/wrapper/maven-wrapper.jar",
            ".mvn/wrapper/maven-wrapper.properties"
        ));
        expected.addAll(EXPECT_FILES_FOR_BOTH);
        assertEquals(expected, project.keySet());
        String pomXml = project.get("pom.xml");
        System.out.println(pomXml);
    }

    @Test
    void generateMicronautGradleApplication(ProjectGenerator projectGenerator) throws Exception {
        Options options = createOptions(List.of(BuildTool.GRADLE));
        MapOutputHandler outputHandler = new MapOutputHandler();
        projectGenerator.generate(options, outputHandler);
        Map<String, String> project = outputHandler.getProject();
        Set<String> expected = new HashSet<>(Set.of(
            "gradle.properties",
            "settings.gradle",
            "build.gradle",
            "gradlew",
            "gradlew.bat",
            "gradle/wrapper/gradle-wrapper.jar",
            "gradle/wrapper/gradle-wrapper.properties"
        ));
        expected.addAll(EXPECT_FILES_FOR_BOTH);
        assertEquals(expected, project.keySet());
        InputStream gradlePropertiesInputStream = new ByteArrayInputStream(project.get("gradle.properties").getBytes(StandardCharsets.UTF_8));
        Properties gradleProperties = new Properties();
        gradleProperties.load(gradlePropertiesInputStream);
        assertNotNull(gradleProperties.get("micronautVersion"));
        String buildGradle = project.get("build.gradle");
        System.out.println(buildGradle);
        String settingsGradle = project.get("settings.gradle");
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasBuildPlugin("java"));
        assertTrue(verifier.hasBuildPlugin("com.github.johnrengelman.shadow"));
        assertTrue(verifier.hasBuildPlugin("io.micronaut.aot"));
        assertTrue(verifier.hasBuildPlugin("io.micronaut.application"));
        assertTrue(verifier.hasDependency("io.micronaut", "micronaut-http-validation", Scope.ANNOTATION_PROCESSOR));
        assertTrue(verifier.hasDependency("io.micronaut.serde", "micronaut-serde-processor", Scope.ANNOTATION_PROCESSOR));
        assertTrue(verifier.hasDependency("io.micronaut.serde", "micronaut-serde-jackson", Scope.COMPILE));
        // test dependencies are not present as the micronaut gradle plugin applies them for the user
        assertFalse(verifier.hasDependency("io.micronaut.test", "micronaut-test-junit5", Scope.TEST));
        assertFalse(verifier.hasDependency("org.junit.jupiter", "junit-jupiter-api", Scope.TEST));
        assertFalse(verifier.hasDependency("org.junit.jupiter", "junit-jupiter-engine", Scope.TEST));
        assertTrue(buildGradle.contains("testRuntime(\"junit5\")"));

    }

    private static Options createOptions(List<BuildTool> buildTools) {
        return MicronautOptions.builder()
            .applicationType(ApplicationType.DEFAULT)
            .name("demo")
            .packageName("com.example")
            .javaVersion(JdkVersion.JDK_21)
            .buildTools(buildTools)
            .language(Language.JAVA)
            .testFramework(TestFramework.JUNIT)
            .features(Collections.emptyList())
            .build();
    }
}
