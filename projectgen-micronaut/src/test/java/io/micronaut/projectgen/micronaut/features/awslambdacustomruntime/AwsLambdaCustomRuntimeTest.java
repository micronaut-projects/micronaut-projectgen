package io.micronaut.projectgen.micronaut.features.awslambdacustomruntime;

import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.io.PreviewGenerator;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.OptionsFixture;
import io.micronaut.projectgen.test.BuildTestVerifier;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.micronaut.projectgen.core.options.TestFramework.SPOCK;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
class AwsLambdaCustomRuntimeTest {

    @Test
    void awsLambdaCustomRuntimeFeaturesAddsTheDependency(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("aws-lambda-custom-runtime")).testFramework(SPOCK).build();
        Map<String, String> project = previewGenerator.generate(options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("io.micronaut.aws", "micronaut-function-aws-custom-runtime", Scope.COMPILE), buildGradle);
        assertTrue(verifier.hasDependency("io.micronaut", "micronaut-function", Scope.TEST), buildGradle);
    }

    @Test
    void awsLambdaCustomRuntimeFeaturesAddsTheLinkInReadmeFile(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("aws-lambda-custom-runtime")).build();
        Map<String, String> project = previewGenerator.generate(options);
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://micronaut-projects.github.io/micronaut-aws/latest/guide/index.html#lambdaCustomRuntimes"));
        assertTrue(readme.contains("https://docs.aws.amazon.com/lambda/latest/dg/runtimes-custom.html"));
    }
}
