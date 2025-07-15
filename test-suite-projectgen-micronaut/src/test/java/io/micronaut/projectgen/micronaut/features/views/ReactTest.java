package io.micronaut.projectgen.micronaut.features.views;

import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.io.PreviewGenerator;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.OptionsFixture;
import io.micronaut.projectgen.test.BuildTestVerifier;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class ReactTest {
    @Test
    void reactFeaturesAddsTheDependency(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("views-react")).build();
        Map<String, String> project = previewGenerator.generate(options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("io.micronaut.views", "micronaut-views-react", Scope.COMPILE), buildGradle);
        assertTrue(verifier.hasDependency("org.graalvm.polyglot", "js-community", Scope.RUNTIME), buildGradle);
        assertTrue(verifier.hasBuildPlugin("com.github.node-gradle.node"), buildGradle);

    }

    //todo add support for Add Gradle settings plugin recipe
    @Disabled
    @Test
    void reactFeaturesAddsTheSettingsPlugin(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("views-react")).build();
        Map<String, String> project = previewGenerator.generate(options);
        String buildGradle = project.get("settings.gradle.kts");
        assertNotNull(buildGradle);
        System.out.println(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasBuildPlugin("org.gradle.toolchains.foojay-resolver-convention"), buildGradle);
    }

    @Test
    void reactFeaturesAddsTheLinkInReadmeFile(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("views-react")).build();
        Map<String, String> project = previewGenerator.generate(options);
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://micronaut-projects.github.io/micronaut-views/latest/guide/index.html#react"));
        assertTrue(readme.contains("https://react.dev/reference/react-dom/server"));
    }
}
