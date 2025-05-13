package io.micronaut.projectgen.micronaut.features.aws;

import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.generator.ProjectGenerator;
import io.micronaut.projectgen.core.io.MapOutputHandler;
import io.micronaut.projectgen.core.options.GenericOptionsBuilder;
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
class AwsLambdaEventsSerdeTest {
    @Test
    void awsLambdaEventSerdeFeaturesAddsTheDependency(ProjectGenerator micronautProjectGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("aws-lambda-events-serde")).build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("io.micronaut.aws", "micronaut-aws-lambda-events-serde", Scope.COMPILE), buildGradle);
    }

    @Test
    void awsLambdaEventsSerdeFeaturesAddsTheLinkInReadmeFile(ProjectGenerator micronautProjectGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("aws-lambda-events-serde")).build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://micronaut-projects.github.io/micronaut-aws/snapshot/guide/#eventsLambdaSerde"));
        assertTrue(readme.contains("https://github.com/aws/aws-lambda-java-libs/tree/main/aws-lambda-java-events"));
    }

    private static Map<String, String> generateProject(ProjectGenerator micronautProjectGenerator,
                                                       Options options) throws Exception {
        MapOutputHandler outputHandler = new MapOutputHandler();
        micronautProjectGenerator.generate(options, outputHandler);
        return outputHandler.getProject();
    }
}
