package io.micronaut.projectgen.micronaut.features.jaxrs;

import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.io.MapOutputHandler;
import io.micronaut.projectgen.core.utils.StringUtils;
import io.micronaut.projectgen.micronaut.MicronautOptions;
import io.micronaut.projectgen.micronaut.MicronautProjectGenerator;
import io.micronaut.projectgen.test.BuildTestVerifier;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class JaxRsTest {
    @Test
    void jaxRsFeaturesAddsTheDependency(MicronautProjectGenerator micronautProjectGenerator) throws Exception {
        MicronautOptions options = MicronautOptions.builder()
            .buildTools(List.of(BuildTool.MAVEN, BuildTool.GRADLE_KOTLIN))
            .feature("jax-rs")
            .build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        System.out.println(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, BuildTool.GRADLE_KOTLIN, options.language(), options.testFramework());
        assertTrue(verifier.hasDependency("io.micronaut.jaxrs", "micronaut-jaxrs-server", Scope.COMPILE), buildGradle);
        assertTrue(verifier.hasAnnotationProcessor("io.micronaut.jaxrs", "micronaut-jaxrs-processor"), buildGradle);

        String pom = project.get("pom.xml");
        System.out.println(pom);
        assertEquals(1, StringUtils.countOccurrences(pom, "micronaut-serde-processor"), pom);
        assertNotNull(pom);
        verifier = BuildTestVerifier.of(pom, BuildTool.MAVEN, options.language(), options.testFramework());
        assertTrue(verifier.hasAnnotationProcessor("io.micronaut.jaxrs", "micronaut-jaxrs-processor"), pom);
    }

    @Test
    void jaxRsFeaturesAddsTheLinkInReadmeFile(MicronautProjectGenerator micronautProjectGenerator) throws Exception {
        MicronautOptions options = MicronautOptions.builder().feature("jax-rs").build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://micronaut-projects.github.io/micronaut-jaxrs/latest/guide/index.html"));
    }

    private static Map<String, String> generateProject(MicronautProjectGenerator micronautProjectGenerator,
                                                       MicronautOptions options) throws Exception {
        MapOutputHandler outputHandler = new MapOutputHandler();
        micronautProjectGenerator.generate(options, outputHandler);
        return outputHandler.getProject();
    }
}
