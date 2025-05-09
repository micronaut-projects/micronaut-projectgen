package io.micronaut.projectgen.micronaut.features.sourcegen;

import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.generator.ProjectGenerator;
import io.micronaut.projectgen.core.io.MapOutputHandler;
import io.micronaut.projectgen.core.options.GenericOptionsBuilder;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.micronaut.OptionsFixture;
import io.micronaut.projectgen.test.BuildTestVerifier;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class SourceGenTest {

    @Inject
    ProjectGenerator micronautProjectGenerator;

    @ParameterizedTest
    @MethodSource("sourceGenDependencies")
    void sourceGenDependencies(String groupId, String artifactId, Language language) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("sourcegen-generator")).language(language).build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency(groupId, artifactId, Scope.ANNOTATION_PROCESSOR));
    }

    private static Stream<Arguments> sourceGenDependencies() {
        return Stream.of(
            Arguments.of("io.micronaut.sourcegen", "micronaut-sourcegen-generator-java", Language.JAVA),
            Arguments.of("io.micronaut.sourcegen","micronaut-sourcegen-generator-kotlin", Language.KOTLIN)
        );
    }

    @Test
    void sourceGenFeaturesAddsTheDependency(ProjectGenerator micronautProjectGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("sourcegen-generator")).build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("io.micronaut.sourcegen", "micronaut-sourcegen-annotations", Scope.COMPILE), buildGradle);
    }

    @Test
    void sourceGenFeaturesAddsTheLinkInReadmeFile(ProjectGenerator micronautProjectGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("sourcegen-generator")).build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://micronaut-projects.github.io/micronaut-sourcegen/latest/guide/"));

    }

    private static Map<String, String> generateProject(ProjectGenerator micronautProjectGenerator,
                                                       Options options) throws Exception {
        MapOutputHandler outputHandler = new MapOutputHandler();
        micronautProjectGenerator.generate(options, outputHandler);
        return outputHandler.getProject();
    }
}
