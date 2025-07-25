package io.micronaut.projectgen.micronaut.features.views;

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
class HandlebarsTest {
    @Test
    void handlebarsViewsFeaturesAddsTheDependency(PreviewGenerator generator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("views-handlebars")).build();
        Map<String, String> project = generator.generate(options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("io.micronaut.views", "micronaut-views-handlebars", Scope.COMPILE), buildGradle);
    }

    @Test
    void handlebarsViewsFeaturesAddsTheLinkInReadmeFile(PreviewGenerator generator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("views-handlebars")).build();
        Map<String, String> project = generator.generate(options);
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://micronaut-projects.github.io/micronaut-views/latest/guide/index.html#handlebars"));
        assertTrue(readme.contains("https://jknack.github.io/handlebars.java/"));
    }
}
