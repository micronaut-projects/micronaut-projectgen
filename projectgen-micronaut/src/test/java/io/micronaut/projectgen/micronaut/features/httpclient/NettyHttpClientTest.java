package io.micronaut.projectgen.micronaut.features.httpclient;

import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.io.MapOutputHandler;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.micronaut.MicronautOptions;
import io.micronaut.projectgen.micronaut.MicronautProjectGenerator;
import io.micronaut.projectgen.test.BuildTestVerifier;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class NettyHttpClientTest {
    @Test
    void httpClientFeaturesAddsTheDependency(MicronautProjectGenerator micronautProjectGenerator) throws Exception {
        MicronautOptions options = MicronautOptions.builder().feature("http-client").build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("io.micronaut", "micronaut-http-client", Scope.COMPILE), buildGradle);

        options = MicronautOptions.builder().build();
        project = generateProject(micronautProjectGenerator, options);
        buildGradle = project.get("build.gradle.kts");
        verifier = BuildTestVerifier.of(buildGradle, options);
        assertFalse(verifier.hasDependency("io.micronaut", "micronaut-http-client", Scope.COMPILE), buildGradle);

        options = MicronautOptions.builder().feature("aws-lambda-custom-runtime").build();
        project = generateProject(micronautProjectGenerator, options);
        buildGradle = project.get("build.gradle.kts");
        verifier = BuildTestVerifier.of(buildGradle, options);
        assertFalse(verifier.hasDependency("io.micronaut", "micronaut-http-client", Scope.COMPILE), buildGradle);
        assertTrue(verifier.hasDependency("io.micronaut", "micronaut-http-client-jdk", Scope.COMPILE), buildGradle);

        options = MicronautOptions.builder().feature("aws-lambda").build();
        project = generateProject(micronautProjectGenerator, options);
        buildGradle = project.get("build.gradle.kts");
        verifier = BuildTestVerifier.of(buildGradle, options);
        assertFalse(verifier.hasDependency("io.micronaut", "micronaut-http-client", Scope.COMPILE), buildGradle);
        assertFalse(verifier.hasDependency("io.micronaut", "micronaut-http-client-jdk", Scope.COMPILE), buildGradle);
        assertTrue(verifier.hasDependency("io.micronaut", "micronaut-http-client-jdk", Scope.COMPILE_ONLY), buildGradle);

        options = MicronautOptions.builder().build();
        project = generateProject(micronautProjectGenerator, options);
        buildGradle = project.get("build.gradle.kts");
        verifier = BuildTestVerifier.of(buildGradle, options);
        assertFalse(verifier.hasDependency("io.micronaut", "micronaut-http-client", Scope.COMPILE), buildGradle);
        assertFalse(verifier.hasDependency("io.micronaut", "micronaut-http-client-jdk", Scope.COMPILE), buildGradle);
        assertFalse(verifier.hasDependency("io.micronaut", "micronaut-http-client-jdk", Scope.COMPILE_ONLY), buildGradle);
        assertTrue(verifier.hasDependency("io.micronaut", "micronaut-http-client", Scope.COMPILE_ONLY), buildGradle);

        options = MicronautOptions.builder().applicationType(ApplicationType.FUNCTION).feature("graalvm").feature("aws-lambda").build();
        project = generateProject(micronautProjectGenerator, options);
        buildGradle = project.get("build.gradle.kts");
        verifier = BuildTestVerifier.of(buildGradle, options);
        assertFalse(verifier.hasDependency("io.micronaut", "micronaut-http-client", Scope.COMPILE), buildGradle);
        assertTrue(verifier.hasDependency("io.micronaut", "micronaut-http-client-jdk", Scope.COMPILE), buildGradle);
        assertFalse(verifier.hasDependency("io.micronaut", "micronaut-http-client-jdk", Scope.COMPILE_ONLY), buildGradle);
        assertFalse(verifier.hasDependency("io.micronaut", "micronaut-http-client", Scope.COMPILE_ONLY), buildGradle);
    }

    @Test
    void httpClientFeaturesAddsTheLinkInReadmeFile(MicronautProjectGenerator micronautProjectGenerator) throws Exception {
        MicronautOptions options = MicronautOptions.builder().feature("http-client").build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://docs.micronaut.io/latest/guide/index.html#nettyHttpClient"));

    }

    private static Map<String, String> generateProject(MicronautProjectGenerator micronautProjectGenerator,
                                                       MicronautOptions options) throws Exception {
        MapOutputHandler outputHandler = new MapOutputHandler();
        micronautProjectGenerator.generate(options, outputHandler);
        return outputHandler.getProject();
    }

}
