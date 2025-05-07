package io.micronaut.projectgen.micronaut.features.kotlin;

import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.io.MapOutputHandler;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.micronaut.MicronautOptions;
import io.micronaut.projectgen.micronaut.MicronautProjectGenerator;
import io.micronaut.projectgen.test.BuildTestVerifier;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
public class KtorTest {
    @Test
    void ktorFeaturesAddsTheDependency(MicronautProjectGenerator micronautProjectGenerator) throws Exception {
        MicronautOptions options = MicronautOptions.builder().feature("ktor").language(Language.KOTLIN).build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("io.micronaut.kotlin", "micronaut-ktor", Scope.COMPILE), buildGradle);
        assertTrue(verifier.hasDependency("io.micronaut.validation", "micronaut-validation", Scope.COMPILE), buildGradle);
        assertTrue(verifier.hasDependency("io.ktor", "ktor-serialization-jackson-jvm", Scope.COMPILE), buildGradle);
        assertTrue(verifier.hasDependency("io.ktor", "ktor-server-content-negotiation-jvm", Scope.COMPILE), buildGradle);
        assertTrue(verifier.hasDependency("io.ktor", "ktor-server-netty-jvm", Scope.COMPILE), buildGradle);
        assertTrue(buildGradle.contains("2.3.13"));
    }

    @Test
    void ktorFeaturesAddsTheLinkInReadmeFile(MicronautProjectGenerator micronautProjectGenerator) throws Exception {
        MicronautOptions options = MicronautOptions.builder().feature("ktor").language(Language.KOTLIN).build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://micronaut-projects.github.io/micronaut-kotlin/latest/guide/index.html#ktor"));

    }

    private static Map<String, String> generateProject(MicronautProjectGenerator micronautProjectGenerator,
                                                       MicronautOptions options) throws Exception {
        MapOutputHandler outputHandler = new MapOutputHandler();
        micronautProjectGenerator.generate(options, outputHandler);
        return outputHandler.getProject();
    }
}
