package io.micronaut.projectgen.micronaut.features.spring;

import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.io.PreviewGenerator;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.OptionsFixture;
import io.micronaut.projectgen.test.BuildTestVerifier;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
class SpringWebTest {
    @Test
    void springWebFeaturesAddsTheDependency(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("spring-web")).build();
        Map<String, String> project = previewGenerator.generate(options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("org.springframework.boot", "spring-boot-starter-web", Scope.COMPILE), buildGradle);
        assertTrue(verifier.hasDependency("io.micronaut", "micronaut-http-server", Scope.COMPILE), buildGradle);
        assertTrue(verifier.hasDependency("io.micronaut.spring", "micronaut-spring-web", Scope.RUNTIME), buildGradle);
        assertTrue(verifier.hasDependency("io.micronaut.spring", "micronaut-spring-web-annotation", Scope.TEST_ANNOTATION_PROCESSOR), buildGradle);
        assertTrue(verifier.hasDependency("io.micronaut.spring", "micronaut-spring-web-annotation", Scope.ANNOTATION_PROCESSOR), buildGradle);
    }
}
