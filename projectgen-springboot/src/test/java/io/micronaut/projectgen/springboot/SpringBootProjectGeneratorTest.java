package io.micronaut.projectgen.springboot;

import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.generator.ProjectGenerator;
import io.micronaut.projectgen.core.io.MapOutputHandler;
import io.micronaut.projectgen.core.options.JdkVersion;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.test.BuildTestVerifier;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class SpringBootProjectGeneratorTest {

    @Test
    void testGenerateSpringBootMavenProject(ProjectGenerator projectGenerator) throws Exception {
        MapOutputHandler outputHandler = new MapOutputHandler();
        Options options = createOptions(List.of(BuildTool.MAVEN));
        projectGenerator.generate(options, outputHandler);
        Map<String, String> project = outputHandler.getProject();
        Set<String> expected = Set.of(
            ".gitignore",
            "pom.xml",
            "mvnw",
            "mvnw.bat",
            ".mvn/wrapper/maven-wrapper.jar",
            ".mvn/wrapper/maven-wrapper.properties"
        );
        Set<String> keys = project.keySet();
        assertEquals(expected, keys);
    }

    @Test
    void testGenerateSpringBootGradleProject(ProjectGenerator projectGenerator) throws Exception {
        MapOutputHandler outputHandler = new MapOutputHandler();
        Options options = createOptions(List.of(BuildTool.GRADLE));
        projectGenerator.generate(options, outputHandler);
        Map<String, String> project = outputHandler.getProject();

        Set<String> expected = Set.of(
            ".gitignore",
            "settings.gradle",
            "build.gradle",
            "gradlew",
            "gradlew.bat",
            "gradle/wrapper/gradle-wrapper.jar",
            "gradle/wrapper/gradle-wrapper.properties"
        );
        Set<String> keys = project.keySet();
        assertEquals(expected, keys);
        String buildGradle = project.get("build.gradle");
        System.out.println(buildGradle);
        String settingsGradle = project.get("settings.gradle");
        System.out.println(settingsGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasBuildPlugin("java"));
        assertTrue(verifier.hasBuildPlugin("org.springframework.boot"));
        assertTrue(verifier.hasBuildPlugin("io.spring.dependency-management"));
        assertTrue(verifier.hasDependency("org.springframework.boot", "spring-boot-starter", Scope.COMPILE));
        assertTrue(verifier.hasDependency("org.springframework.boot", "spring-boot-starter-test", Scope.TEST));
        String pomXml = project.get("pom.xml");
        String gitignore = project.get(".gitignore");
    }

    private static Options createOptions(List<BuildTool> buildTools) {
        return SpringBootOptionsBuilder.builder()
            .group("com.example")
            .version("0.0.1-SNAPSHOT")
            .name("demo")
            .packageName("com.example.demo")
            .javaVersion(JdkVersion.JDK_21)
            .buildTools(buildTools)
            .language(Language.JAVA)
            .features(Collections.emptyList())
            .framework("spring-boot")
            .build();
    }
}
