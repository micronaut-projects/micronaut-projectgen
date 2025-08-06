package io.micronaut.projectgen.micronaut.features.httpclient;

import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.generator.ProjectGenerator;
import io.micronaut.projectgen.core.io.MapOutputHandler;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.micronaut.OptionsFixture;
import io.micronaut.projectgen.test.BuildTestVerifier;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class NettyHttpClientTest {
    @Test
    void httpClientFeaturesAddsTheDependency(ProjectGenerator micronautProjectGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("http-client")).build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("io.micronaut", "micronaut-http-client", Scope.COMPILE), buildGradle);

        options = OptionsFixture.defaultGradle().build();
        project = generateProject(micronautProjectGenerator, options);
        buildGradle = project.get("build.gradle.kts");
        verifier = BuildTestVerifier.of(buildGradle, options);
        assertFalse(verifier.hasDependency("io.micronaut", "micronaut-http-client", Scope.COMPILE), buildGradle);

        options = OptionsFixture.defaultGradle().features(List.of("aws-lambda-custom-runtime")).build();
        project = generateProject(micronautProjectGenerator, options);
        buildGradle = project.get("build.gradle.kts");
        verifier = BuildTestVerifier.of(buildGradle, options);
        assertFalse(verifier.hasDependency("io.micronaut", "micronaut-http-client", Scope.COMPILE), buildGradle);
        assertTrue(verifier.hasDependency("io.micronaut", "micronaut-http-client-jdk", Scope.COMPILE), buildGradle);

        options = OptionsFixture.defaultGradle().features(List.of("aws-lambda")).build();
        project = generateProject(micronautProjectGenerator, options);
        buildGradle = project.get("build.gradle.kts");
        verifier = BuildTestVerifier.of(buildGradle, options);
        assertFalse(verifier.hasDependency("io.micronaut", "micronaut-http-client", Scope.COMPILE), buildGradle);
        assertFalse(verifier.hasDependency("io.micronaut", "micronaut-http-client-jdk", Scope.COMPILE), buildGradle);
        assertTrue(verifier.hasDependency("io.micronaut", "micronaut-http-client-jdk", Scope.COMPILE_ONLY), buildGradle);

        options = OptionsFixture.defaultGradle().build();
        project = generateProject(micronautProjectGenerator, options);
        buildGradle = project.get("build.gradle.kts");
        verifier = BuildTestVerifier.of(buildGradle, options);
        assertFalse(verifier.hasDependency("io.micronaut", "micronaut-http-client", Scope.COMPILE), buildGradle);
        assertFalse(verifier.hasDependency("io.micronaut", "micronaut-http-client-jdk", Scope.COMPILE), buildGradle);
        assertFalse(verifier.hasDependency("io.micronaut", "micronaut-http-client-jdk", Scope.COMPILE_ONLY), buildGradle);
        assertTrue(verifier.hasDependency("io.micronaut", "micronaut-http-client", Scope.COMPILE_ONLY), buildGradle);

        options = OptionsFixture.defaultGradle().template(ApplicationType.FUNCTION.toString()).features(List.of("graalvm", "aws-lambda")).build();
        project = generateProject(micronautProjectGenerator, options);
        buildGradle = project.get("build.gradle.kts");
        verifier = BuildTestVerifier.of(buildGradle, options);
        assertFalse(verifier.hasDependency("io.micronaut", "micronaut-http-client", Scope.COMPILE), buildGradle);
        assertTrue(verifier.hasDependency("io.micronaut", "micronaut-http-client-jdk", Scope.COMPILE), buildGradle);
        assertFalse(verifier.hasDependency("io.micronaut", "micronaut-http-client-jdk", Scope.COMPILE_ONLY), buildGradle);
        assertFalse(verifier.hasDependency("io.micronaut", "micronaut-http-client", Scope.COMPILE_ONLY), buildGradle);
    }

    @Test
    void httpClientFeaturesAddsTheLinkInReadmeFile(ProjectGenerator micronautProjectGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("http-client")).build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://docs.micronaut.io/latest/guide/index.html#nettyHttpClient"));

    }

    private static Map<String, String> generateProject(ProjectGenerator micronautProjectGenerator,
        Options options) throws Exception {
        MapOutputHandler outputHandler = new MapOutputHandler();
        micronautProjectGenerator.generate(options, outputHandler);
        return outputHandler.getProject();
    }

}
