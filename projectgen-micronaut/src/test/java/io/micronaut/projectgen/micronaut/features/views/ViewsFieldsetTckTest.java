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

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class ViewsFieldsetTckTest {
    @Test
    void fieldsetTckViewsFeaturesAddsTheDependency(PreviewGenerator generator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("views-fieldset-tck")).build();
        Map<String, String> project = generator.generate(options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("io.micronaut.views", "micronaut-views-fieldset-tck", Scope.TEST), buildGradle);
    }
}
