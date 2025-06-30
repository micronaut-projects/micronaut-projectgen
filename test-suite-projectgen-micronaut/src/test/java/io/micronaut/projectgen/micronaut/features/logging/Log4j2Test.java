package io.micronaut.projectgen.micronaut.features.logging;

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
class Log4j2Test {
    @Test
    void log4jFeaturesAddsTheDependency(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultMaven().features(List.of("log4j2")).build();
        Map<String, String> project = previewGenerator.generate(options);
        String pom = project.get("pom.xml");
        assertNotNull(pom);
        BuildTestVerifier verifier = BuildTestVerifier.of(pom, options);
        assertTrue(verifier.hasDependency("org.apache.logging.log4j", "log4j-api", Scope.COMPILE), pom);
        assertTrue(verifier.hasDependency("org.apache.logging.log4j", "log4j-core", Scope.RUNTIME), pom);
        assertTrue(verifier.hasDependency("org.apache.logging.log4j", "log4j-slf4j-impl", Scope.RUNTIME), pom);
        //toDo add support for bom  assertTrue(verifier.hasBom("org.apache.logging.log4j", "log4j-bom", "import"), pom);
    }

    @Test
    void log4jPlatformFeaturesAddsTheDependency(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("log4j2")).build();
        Map<String, String> project = previewGenerator.generate(options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        //toDo add support for Platform dependency
    }

}
