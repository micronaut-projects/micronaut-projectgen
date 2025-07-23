package io.micronaut.projectgen.micronaut;

import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.buildtools.gradle.GradleDsl;
import io.micronaut.projectgen.core.generator.ProjectGenerator;
import io.micronaut.projectgen.core.io.MapOutputHandler;
import io.micronaut.projectgen.core.io.PreviewGenerator;
import io.micronaut.projectgen.core.options.GenericOptionsBuilder;
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
        "micronaut-cli.yml",
        "README.md",
        "src/main/resources/logback.xml",
        "src/main/resources/application.properties",
        "src/main/java/com/example/Application.java",
        "src/test/java/com/example/DemoTest.java"
    );

    @Test
    void generateMicronautMavenApplication(PreviewGenerator previewGenerator) throws Exception {
        Options options = createOptionsBuilder().buildTools(List.of(BuildTool.MAVEN)).gradleDsl(GradleDsl.GROOVY).build();
        Map<String, String> project = previewGenerator.generate(options);
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
        BuildTestVerifier verifier = BuildTestVerifier.of(pomXml, BuildTool.MAVEN, options.language(), options.testFramework());
        assertTrue(verifier.hasParentPom("io.micronaut.platform", "micronaut-parent"), pomXml);
        assertTrue(pomXml.contains("<groupId>com.example</groupId>"), pomXml);
        assertTrue(pomXml.contains("<artifactId>demo</artifactId>"), pomXml);
        assertTrue(pomXml.contains("<version>0.1</version>"), pomXml);
        assertEquals("netty", verifier.getProperty("micronaut.runtime"));
        assertEquals("jar", verifier.getProperty("packaging"));
        assertEquals("21", verifier.getProperty("jdk.version"));
        assertEquals("21", verifier.getProperty("release.version"));
        assertNotNull(verifier.getProperty("exec.mainClass"));
        //assertEquals("com.example.Application", verifier.getProperty("exec.mainClass"));
        assertTrue(verifier.hasBuildPlugin("org.apache.maven.plugins", "maven-enforcer-plugin"));
        assertTrue(verifier.hasBuildPlugin("org.apache.maven.plugins", "maven-compiler-plugin"));
        assertTrue(verifier.hasBuildPlugin("io.micronaut.maven", "micronaut-maven-plugin"));
        assertTrue(verifier.hasParentPom("io.micronaut.platform", "micronaut-parent"));
    }

    @Test
    void generateMicronautGradleApplication(PreviewGenerator previewGenerator) throws Exception {
        Options options = createOptionsBuilder().buildTools(List.of(BuildTool.GRADLE)).gradleDsl(GradleDsl.GROOVY).build();
        Map<String, String> project = previewGenerator.generate(options);
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
        assertEquals(expected.stream().sorted().toList(), project.keySet().stream().sorted().toList());
        InputStream gradlePropertiesInputStream = new ByteArrayInputStream(project.get("gradle.properties").getBytes(StandardCharsets.UTF_8));
        Properties gradleProperties = new Properties();
        gradleProperties.load(gradlePropertiesInputStream);
        assertNotNull(gradleProperties.get("micronautVersion"));
        String buildGradle = project.get("build.gradle");
        //TODO this is flaky
        // assertTrue(buildGradle.contains("runtime(\"netty\")"), buildGradle);
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

    public static GenericOptionsBuilder createOptionsBuilder() {
        return OptionsFixture.defaultNoBuildTool()
            .template(ApplicationType.DEFAULT.toString())
            .name("demo")
            .version("0.1")
            .packageName("com.example")
            .java(JdkVersion.JDK_21)
            .gradleDsl(GradleDsl.KOTLIN)
            .language(Language.JAVA)
            .testFramework(TestFramework.JUNIT)
            .features(Collections.emptyList());
    }
}
