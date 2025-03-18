package io.micronaut.starter.feature.langchain4j.embeddedstore;

import io.micronaut.projectgen.core.io.MapOutputHandler;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.options.TestFramework;
import io.micronaut.projectgen.micronaut.MicronautOptions;
import io.micronaut.projectgen.micronaut.MicronautProjectGenerator;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
class Lanchain4jTest {
    @Inject
    MicronautProjectGenerator micronautProjectGenerator;

    @ParameterizedTest
    @MethodSource("laghcain4JArguments")
    void lanchain4JDependencies(String feature) throws Exception {
        MicronautOptions options = MicronautOptions.builder().feature(feature).build();
        MapOutputHandler outputHandler = new MapOutputHandler();
        micronautProjectGenerator.generate(options, outputHandler);
        Map<String, String> project = outputHandler.getProject();
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        System.out.println(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasAnnotationProcessor("io.micronaut.langchain4j", "micronaut-langchain4j-processor"));
    }

    private static Stream<Arguments> laghcain4JArguments() {
        return Stream.of(
            Arguments.of("langchain4j-store-elasticsearch")//,
//            Arguments.of("langchain4j-store-mongodb-atlas"),
//            Arguments.of("langchain4j-store-neo4j"),
//            Arguments.of("langchain4j-store-opensearch"),
//            Arguments.of("langchain4j-store-oracle"),
//            Arguments.of("langchain4j-store-pgvector"),
//            Arguments.of("langchain4j-store-qdrant"),
//            Arguments.of("langchain4j-anthropic"),
//            Arguments.of("langchain4j-azure"),
//            Arguments.of("langchain4j-bedrock"),
//            Arguments.of("langchain4j-googleai-gemini"),
//            Arguments.of("langchain4j-hugging-face"),
//            Arguments.of("langchain4j-mistralai"),
//            Arguments.of("langchain4j-ollama"),
//            Arguments.of("langchain4j-openai"),
//            Arguments.of("langchain4j-vertexai"),
//            Arguments.of("langchain4j-vertexai-gemini")
        );
    }
}
