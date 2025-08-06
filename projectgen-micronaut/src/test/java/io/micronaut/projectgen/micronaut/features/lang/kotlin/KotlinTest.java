package io.micronaut.projectgen.micronaut.features.lang.kotlin;

import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.io.PreviewGenerator;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.OptionsFixture;
import io.micronaut.projectgen.test.BuildTestVerifier;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
class KotlinTest {
    @Test
    void kotlinFeaturesAddsTheDependency(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().language(Language.KOTLIN).build();
        Map<String, String> project = previewGenerator.generate(options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("io.micronaut.kotlin", "micronaut-kotlin-runtime", Scope.COMPILE), buildGradle);
        assertTrue(verifier.hasDependency("org.jetbrains.kotlin", "kotlin-reflect", Scope.COMPILE, "${kotlinVersion}", true), buildGradle);
        assertTrue(verifier.hasDependency("org.jetbrains.kotlin", "kotlin-stdlib-jdk8", Scope.COMPILE, "${kotlinVersion}", true), buildGradle);
        assertTrue(verifier.hasDependency("io.micronaut.kotlin", "micronaut-kotlin-runtime", Scope.COMPILE), buildGradle);
        assertTrue(verifier.hasDependency("com.fasterxml.jackson.module", "jackson-module-kotlin", Scope.RUNTIME), buildGradle);
    }
}
