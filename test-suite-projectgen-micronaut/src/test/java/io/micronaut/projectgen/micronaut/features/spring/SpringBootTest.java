package io.micronaut.projectgen.micronaut.features.spring;

import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.io.PreviewGenerator;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.OptionsFixture;
import io.micronaut.projectgen.test.BuildTestVerifier;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class SpringBootTest {
    @Test
    void springBootFeaturesAddsTheDependency(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("spring-boot")).build();
        Map<String, String> project = previewGenerator.generate(options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("org.springframework.boot", "spring-boot-starter-web", Scope.COMPILE), buildGradle);
        assertTrue(verifier.hasDependency("io.micronaut.spring", "micronaut-spring-boot-annotation", Scope.TEST_ANNOTATION_PROCESSOR), buildGradle);
        assertTrue(verifier.hasDependency("io.micronaut.spring", "micronaut-spring-boot-annotation", Scope.ANNOTATION_PROCESSOR), buildGradle);
        assertTrue(verifier.hasDependency("io.micronaut.spring", "micronaut-spring-boot", Scope.RUNTIME), buildGradle);
    }

//Could not get version for ID groovy
//    @Test
//    void springBootMavenFeaturesAddsTheDependency(PreviewGenerator previewGenerator) throws Exception {
//        Options options = OptionsFixture.defaultMaven().language(Language.GROOVY).features(List.of("spring-boot")).build();
//        Map<String, String> project = previewGenerator.generate(options);
//        String pom = project.get("pom.xml");
//        assertNotNull(pom);
//        BuildTestVerifier verifier = BuildTestVerifier.of(pom, options);
//        assertTrue(verifier.hasDependency("io.micronaut.spring", "micronaut-spring-boot", Scope.COMPILE), pom);
//    }
}
