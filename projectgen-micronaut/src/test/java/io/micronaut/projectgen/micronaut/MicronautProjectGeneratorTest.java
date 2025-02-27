package io.micronaut.projectgen.micronaut;

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
class MicronautProjectGeneratorTest {

    @Test
    void testGenerateSpringBootProject(ProjectGenerator projectGenerator) throws Exception {
        Options options = MicronautOptions.builder()
            .name("demo")
            .packageName("com.example")
            .javaVersion(JdkVersion.JDK_21)
            .buildTools(List.of(BuildTool.GRADLE, BuildTool.MAVEN))
            .language(Language.JAVA)
            .features(Collections.emptyList())
            .build();
        MapOutputHandler outputHandler = new MapOutputHandler();
        projectGenerator.generate(options, outputHandler);
        Map<String, String> project = outputHandler.getProject();

        Set<String> expected = Set.of(
            ".gitignore",
            "pom.xml",
            "mvnw",
            "mvnw.bat",
            ".mvn/wrapper/maven-wrapper.jar",
            ".mvn/wrapper/maven-wrapper.properties",
            "settings.gradle",
            "build.gradle",
            "gradlew",
            "gradlew.bat",
            "gradle/wrapper/gradle-wrapper.jar",
            "gradle/wrapper/gradle-wrapper.properties"
        );
        Set<String> keys = project.keySet();
        for (String path : expected) {
            assertTrue(keys.contains(path));
        }
        assertEquals(expected.size(), project.keySet().size());
        String buildGradle = project.get("build.gradle");
        System.out.println(buildGradle);
        String settingsGradle = project.get("settings.gradle");
        System.out.println(settingsGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasBuildPlugin("java"));
        assertTrue(verifier.hasBuildPlugin("com.github.johnrengelman.shadow"));
        //assertTrue(verifier.hasBuildPlugin("io.micronaut.aot"));
        //assertTrue(verifier.hasBuildPlugin("io.micronaut.application"));

        String pomXml = project.get("pom.xml");
        String gitignore = project.get(".gitignore");
    }
}
