package io.micronaut.projectgen.micronaut.features.server;

import io.micronaut.projectgen.core.buildtools.BuildTool;
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
class HttpPojaTest {
    @Test
    void httpPojaFeaturesAddsTheDependency(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultMaven().features(List.of("http-poja")).build();
        Map<String, String> project = previewGenerator.generate(options);
        String pom = project.get("pom.xml");
        assertNotNull(pom);
        BuildTestVerifier verifier = BuildTestVerifier.of(pom, BuildTool.MAVEN, options.language(), options.testFramework());
        assertTrue(verifier.hasDependency("io.micronaut.servlet", "micronaut-http-poja-apache", Scope.COMPILE), pom);
        assertTrue(verifier.hasDependency("io.micronaut.servlet", "micronaut-http-poja-test", Scope.TEST), pom);
    }

    @Test
    void httpPojaFeaturesAddsTheLinkInReadmeFile(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("http-poja")).build();
        Map<String, String> project = previewGenerator.generate(options);
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://micronaut-projects.github.io/micronaut-servlet/latest/guide/index.html#httpPoja"));
    }
}
