package io.micronaut.projectgen.springboot;

import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.generator.ProjectGenerator;
import io.micronaut.projectgen.core.io.MapOutputHandler;
import io.micronaut.projectgen.core.io.PreviewGenerator;
import io.micronaut.projectgen.core.options.GenericOptions;
import io.micronaut.projectgen.core.options.GenericOptionsBuilder;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
class SpringBootProjectGeneratorTest {

    @Test
    void testGenerateSpringBootMavenProject(PreviewGenerator generator) throws Exception {
        Options options = createOptions(List.of(BuildTool.MAVEN));
        Map<String, String> project = generator.generate(options);
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
    void testGenerateSpringBootGradleProject(PreviewGenerator generator) throws Exception {
        Options options = createOptions(List.of(BuildTool.GRADLE));
        Map<String, String> project = generator.generate(options);
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
        String settingsGradle = project.get("settings.gradle");
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
        return GenericOptionsBuilder.builder().name("demo")
            .group("com.example")
            .name("demo")
            .packageName("com.example.demo")
            .java(JdkVersion.JDK_21)
            .buildTools(buildTools)
            .language(Language.JAVA)
            .features(Collections.emptyList())
            .build();
    }
}
