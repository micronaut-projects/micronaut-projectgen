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

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class SpringDataJpaTest {
    @Test
    void springFeaturesAddsTheDependency(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("spring-data-jpa")).build();
        Map<String, String> project = previewGenerator.generate(options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("io.micronaut.data", "micronaut-data-spring", Scope.COMPILE), buildGradle);
        assertTrue(verifier.hasDependency("io.micronaut.data", "micronaut-data-spring-jpa", Scope.COMPILE), buildGradle);
        assertTrue(verifier.hasDependency("org.springframework", "spring-orm", Scope.COMPILE), buildGradle);

    }

}
