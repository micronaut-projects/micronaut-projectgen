package io.micronaut.projectgen.micronaut.features.test;

import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.io.PreviewGenerator;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.options.TestFramework;
import io.micronaut.projectgen.micronaut.OptionsFixture;
import io.micronaut.projectgen.test.BuildTestVerifier;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
class KoTestTest {
    @Test
    void kotestFeaturesAddsTheDependency(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultMaven().features(List.of("kotest")).build();
        Map<String, String> project = previewGenerator.generate(options);
        String pom = project.get("pom.xml");
        assertNotNull(pom);
        BuildTestVerifier verifier = BuildTestVerifier.of(pom, options);
        assertTrue(verifier.hasDependency("io.micronaut.test", "micronaut-test-kotest5", Scope.TEST), pom);
        assertTrue(verifier.hasDependency("io.kotest", "kotest-runner-junit5-jvm", Scope.TEST), pom);
        assertTrue(verifier.hasDependency("io.kotest", "kotest-assertions-core-jvm", Scope.TEST), pom);
    }

    @Test
    void kotestFeaturesAddsTheLinkInReadmeFile(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().testFramework(TestFramework.KOTEST).features(List.of("kotest")).build();
        Map<String, String> project = previewGenerator.generate(options);
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://micronaut-projects.github.io/micronaut-test/latest/guide/#kotest5"));
        assertTrue(readme.contains("https://kotest.io/"));
    }
}
