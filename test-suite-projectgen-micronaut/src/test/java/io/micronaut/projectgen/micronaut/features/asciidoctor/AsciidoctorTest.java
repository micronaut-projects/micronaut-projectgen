package io.micronaut.projectgen.micronaut.features.asciidoctor;

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
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
class AsciidoctorTest {
    @Test
    void asciidoctorGradleFeaturesAddsTheDependency(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("asciidoctor")).build();
        Map<String, String> project = previewGenerator.generate(options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasBuildPlugin("org.asciidoctor.jvm.convert"), buildGradle);
    }

    @Test
    void asciidoctorMavenFeaturesAddsTheDependency(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultMaven().features(List.of("asciidoctor")).build();
        Map<String, String> project = previewGenerator.generate(options);
        String pom = project.get("pom.xml");
        assertNotNull(pom);
        BuildTestVerifier verifier = BuildTestVerifier.of(pom, options);
        assertTrue(verifier.hasBuildPlugin("org.asciidoctor", "asciidoctor-maven-plugin"), pom);
        assertTrue(pom.contains("<asciidoctor.maven.plugin.version>2.2.6</asciidoctor.maven.plugin.version>"));
        assertTrue(pom.contains("<asciidoctorj.version>2.5.13</asciidoctorj.version>"));
        assertTrue(pom.contains("<asciidoctorj.diagram.version>2.3.2</asciidoctorj.diagram.version>"));
        assertTrue(pom.contains("<jruby.version>9.4.13.0</jruby.version>"));
    }
}
