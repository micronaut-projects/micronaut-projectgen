package io.micronaut.projectgen.micronaut.features.langchain4j;

import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.io.MapOutputHandler;
import io.micronaut.projectgen.micronaut.MicronautOptions;
import io.micronaut.projectgen.micronaut.MicronautProjectGenerator;
import io.micronaut.projectgen.test.BuildTestVerifier;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
class Lanchain4jTest {
    @Inject
    MicronautProjectGenerator micronautProjectGenerator;

    @ParameterizedTest
    @MethodSource("laghcain4JArguments")
    void lanchain4JDependencies(String feature, String groupId, String artifactId, boolean hikari) throws Exception {
        MicronautOptions options = MicronautOptions.builder().feature(feature).feature("test-resources").build();
        MapOutputHandler outputHandler = new MapOutputHandler();
        micronautProjectGenerator.generate(options, outputHandler);
        Map<String, String> project = outputHandler.getProject();
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasAnnotationProcessor("io.micronaut.langchain4j", "micronaut-langchain4j-processor"), buildGradle);
        assertTrue(verifier.hasDependency(groupId, artifactId), buildGradle);
        if (hikari) {
            assertTrue(verifier.hasDependency("io.micronaut.sql", "micronaut-jdbc-hikari"), buildGradle);
        }
        if (feature.equals("langchain4j-ollama")) {
            assertTrue(verifier.hasDependency("io.micronaut.langchain4j", "micronaut-langchain4j-ollama-testresources", Scope.TEST_RESOURCES_SERVICE), buildGradle);
        }
        if (feature.equals("langchain4j-store-qdrant")) {
            assertTrue(verifier.hasDependency("io.micronaut.langchain4j", "micronaut-langchain4j-qdrant-testresource", Scope.TEST_RESOURCES_SERVICE), buildGradle);
        }
    }

    private static Stream<Arguments> laghcain4JArguments() {
        return Stream.of(
            Arguments.of("langchain4j-store-elasticsearch", "io.micronaut.langchain4j", "micronaut-langchain4j-store-elasticsearch", false),
            Arguments.of("langchain4j-store-mongodb-atlas","io.micronaut.langchain4j", "micronaut-langchain4j-store-mongodb-atlas", false),
            Arguments.of("langchain4j-store-neo4j", "io.micronaut.langchain4j", "micronaut-langchain4j-store-neo4j", false),
            Arguments.of("langchain4j-store-opensearch", "io.micronaut.langchain4j", "micronaut-langchain4j-store-opensearch", false),
            Arguments.of("langchain4j-store-oracle", "io.micronaut.langchain4j", "micronaut-langchain4j-store-oracle", true),
            Arguments.of("langchain4j-store-pgvector", "io.micronaut.langchain4j", "micronaut-langchain4j-store-pgvector", true),
            Arguments.of("langchain4j-store-qdrant", "io.micronaut.langchain4j", "micronaut-langchain4j-store-qdrant", false),
            Arguments.of("langchain4j-anthropic", "io.micronaut.langchain4j", "micronaut-langchain4j-anthropic", false),
            Arguments.of("langchain4j-azure", "io.micronaut.langchain4j", "micronaut-langchain4j-azure", false),
            Arguments.of("langchain4j-bedrock", "io.micronaut.langchain4j", "micronaut-langchain4j-bedrock", false),
            Arguments.of("langchain4j-googleai-gemini", "io.micronaut.langchain4j", "micronaut-langchain4j-googleai-gemini", false),
            Arguments.of("langchain4j-hugging-face", "io.micronaut.langchain4j", "micronaut-langchain4j-hugging-face", false),
            Arguments.of("langchain4j-mistralai", "io.micronaut.langchain4j", "micronaut-langchain4j-mistralai", false),
            Arguments.of("langchain4j-ollama", "io.micronaut.langchain4j", "micronaut-langchain4j-ollama", false),
            Arguments.of("langchain4j-openai", "io.micronaut.langchain4j", "micronaut-langchain4j-openai", false),
            Arguments.of("langchain4j-vertexai", "io.micronaut.langchain4j", "micronaut-langchain4j-vertexai", false),
            Arguments.of("langchain4j-vertexai-gemini", "io.micronaut.langchain4j", "micronaut-langchain4j-vertexai-gemini", false)
        );
    }
}
