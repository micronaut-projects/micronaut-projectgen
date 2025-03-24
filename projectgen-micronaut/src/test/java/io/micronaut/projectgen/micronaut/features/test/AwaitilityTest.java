package io.micronaut.projectgen.micronaut.features.test;

import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.io.MapOutputHandler;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.micronaut.MicronautOptions;
import io.micronaut.projectgen.micronaut.MicronautProjectGenerator;
import io.micronaut.projectgen.test.BuildTestVerifier;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
class AwaitilityTest {

    @Inject
    MicronautProjectGenerator micronautProjectGenerator;

    @ParameterizedTest
    @MethodSource("awaitilityDependencies")
    void awaitilityDependencies(String groupId, String artifactId, Language language) throws Exception {
        MicronautOptions options = MicronautOptions.builder().feature("awaitility").language(language).build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency(groupId, artifactId, Scope.TEST));
    }

    private static Stream<Arguments> awaitilityDependencies() {
        return Stream.of(
            Arguments.of("org.awaitility", "awaitility", Language.JAVA),
            Arguments.of("org.awaitility","awaitility-kotlin", Language.KOTLIN),
            Arguments.of("org.awaitility", "awaitility-groovy", Language.GROOVY)
        );
    }

    private static Map<String, String> generateProject(MicronautProjectGenerator micronautProjectGenerator,
                                                       MicronautOptions options) throws Exception {
        MapOutputHandler outputHandler = new MapOutputHandler();
        micronautProjectGenerator.generate(options, outputHandler);
        return outputHandler.getProject();
    }
}
