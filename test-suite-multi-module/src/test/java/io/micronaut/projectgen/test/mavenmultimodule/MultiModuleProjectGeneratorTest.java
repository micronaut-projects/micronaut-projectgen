package io.micronaut.projectgen.test.mavenmultimodule;

import io.micronaut.core.io.ResourceLoader;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.generator.ProjectGenerator;
import io.micronaut.projectgen.core.io.MapOutputHandler;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.options.OptionsImpl;
import io.micronaut.projectgen.test.BuildTestVerifier;
import io.micronaut.projectgen.test.ConfigurationUtils;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
class MultiModuleProjectGeneratorTest {

    @Test
    void testMultiModule(ProjectGenerator projectGenerator,
                              ResourceLoader resourceLoader) throws Exception {
        MapOutputHandler outputHandler = new MapOutputHandler();
        Options options = OptionsImpl.builder()
            .buildTools(List.of(BuildTool.MAVEN, BuildTool.GRADLE))
            .name("org.springframework.gs-multi-module")
            .build();
        projectGenerator.generate(options, outputHandler);
        Map<String, String> project = outputHandler.getProject();
        assertNotNull(project.get("application/build.gradle"));
        assertNotNull(project.get("library/build.gradle"));
        assertNotNull(project.get("build.gradle"));
        assertNotNull(project.get("settings.gradle"));

        System.out.println(project.keySet());

        Properties applicationProperties = ConfigurationUtils.loadApplicationPropertiesByModule(project, "application");
        assertNotNull(applicationProperties);
        assertEquals("demo", applicationProperties.get("spring.application.name"));

        assertFile("expectedSettings.gradle", "settings.gradle", project, resourceLoader);
        assertFile("library.gradle", "library/build.gradle", project, resourceLoader);
        assertFile("rootPom.xml", "pom.xml", project, resourceLoader);
        assertFile("applicationPom.xml", "application/pom.xml", project, resourceLoader);
        assertFile("libraryPom.xml", "library/pom.xml", project, resourceLoader);

        String buildGradle = project.get("library/build.gradle");
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasBuildPlugin("org.springframework.boot"));
        assertTrue(verifier.hasBuildPlugin("io.spring.dependency-management"));
        assertTrue(verifier.hasDependency("org.springframework.boot", "spring-boot", Scope.COMPILE));
        assertTrue(verifier.hasDependency("org.springframework.boot", "spring-boot-starter-test", Scope.TEST));

        buildGradle = project.get("application/build.gradle");
        verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasBuildPlugin("org.springframework.boot"), buildGradle);
        assertTrue(verifier.hasBuildPlugin("io.spring.dependency-management"), buildGradle);
        assertTrue(verifier.hasDependency("org.springframework.boot", "spring-boot-starter-web", Scope.COMPILE), buildGradle);
        assertTrue(verifier.hasDependency("org.springframework.boot", "spring-boot-starter-actuator", Scope.COMPILE), buildGradle);
        assertTrue(verifier.hasDependency("org.springframework.boot", "spring-boot-starter-test", Scope.TEST), buildGradle);

    }

    void assertFile(String classpathName,
                    String path,
                    Map<String, String> project,
                    ResourceLoader resourceLoader) throws Exception {
        String contents = project.get(path);
        Optional<InputStream> resourceAsStream = resourceLoader.getResourceAsStream("classpath:" + classpathName);
        assertTrue(resourceAsStream.isPresent());
        String expected = new String(resourceAsStream.get().readAllBytes(), StandardCharsets.UTF_8);
        assertEquals(contents.strip(), expected.strip());
    }
}
