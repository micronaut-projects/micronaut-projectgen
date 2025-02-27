package io.micronaut.projectgen.micronaut;

import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.generator.ProjectGenerator;
import io.micronaut.projectgen.core.io.MapOutputHandler;
import io.micronaut.projectgen.core.options.JdkVersion;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.options.TestFramework;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
class MicronautLibraryGenerationTest {

    @Test
    void generateMicronautLibrary(ProjectGenerator projectGenerator) throws Exception {
        Options options = createOptions();
        MapOutputHandler outputHandler = new MapOutputHandler();
        projectGenerator.generate(options, outputHandler);
        Map<String, String> project = outputHandler.getProject();
        Set<String> expected = new HashSet<>(Set.of(
            "config/spotless.license.java",
            "gradle.properties",
            "LICENSE",
            "settings.gradle",
            "build.gradle",
            "gradlew",
            "gradlew.bat",
            "gradle/wrapper/gradle-wrapper.jar",
            "gradle/wrapper/gradle-wrapper.properties"
        ));
        assertEquals(expected.stream().sorted().toList(), project.keySet().stream().sorted().toList());

        assertTrue(project.get("LICENSE").contains("Apache License"));
        System.out.println(project.get("build.gradle"));
    }

    private static Options createOptions() {
        return MicronautOptions.builder()
            .applicationType(ApplicationType.LIBRARY)
            .name("demo")
            .packageName("com.example")
            .javaVersion(JdkVersion.JDK_21)
            .buildTools(List.of(BuildTool.GRADLE))
            .language(Language.JAVA)
            .testFramework(TestFramework.JUNIT)
            .features(Collections.emptyList())
            .build();
    }
}
