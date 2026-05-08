package io.micronaut.projectgen.micronaut;

import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.gradle.GradleDsl;
import io.micronaut.projectgen.core.io.PreviewGenerator;
import io.micronaut.projectgen.core.options.JdkVersion;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.options.TestFramework;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
class MicronautLibraryGenerationTest {

    @Test
    void generateMicronautLibrary(PreviewGenerator previewGenerator) throws Exception {
        Options options = createOptions();
        Map<String, String> project = previewGenerator.generate(options);
        Set<String> expected = new HashSet<>(Set.of(
            "micronaut-cli.yml",
            ".gitignore",
            "config/spotless.license.java",
            "gradle.properties",
            "LICENSE",
            "README.md",
            "settings.gradle",
            "build.gradle",
            "gradlew",
            "gradlew.bat",
            "gradle/wrapper/gradle-wrapper.jar",
            "gradle/wrapper/gradle-wrapper.properties",
            "src/main/resources/logback.xml"
        ));
        assertEquals(expected.stream().sorted().toList(), project.keySet().stream().sorted().toList());
        assertTrue(project.get("LICENSE").contains("Apache License"));
    }

    private static Options createOptions() {
        return OptionsFixture.defaultGradle()
            .template(ApplicationType.LIBRARY.toString())
            .name("demo")
            .packageName("com.example")
            .java(JdkVersion.JDK_21)
            .buildTools(List.of(BuildTool.GRADLE))
            .gradleDsl(GradleDsl.GROOVY)
            .language(Language.JAVA)
            .testFramework(TestFramework.JUNIT)
            .features(Collections.emptyList())
            .build();
    }
}
