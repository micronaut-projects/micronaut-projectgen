package io.micronaut.projectgen.demo;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.io.PreviewGenerator;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.test.BuildTestVerifier;
import io.micronaut.projectgen.test.GradleBuildTestVerifier;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Property(name = "hello-world-test-with-openrewrite", value = StringUtils.TRUE)
@MicronautTest(startApplication = false)
class FeatureViaOpenRewriteTest {

    @Test
    void featureWithOpenRewrite(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFactory.create(List.of("hello-world-test"));
        Map<String, String> project = previewGenerator.generate(options);
        String buildGradleKts = project.get("build.gradle.kts");
        BuildTestVerifier verifier = new GradleBuildTestVerifier(buildGradleKts, BuildTool.GRADLE, options.language(), options.testFramework());
        assertTrue(verifier.hasDependency("org.junit.jupiter", "junit-jupiter", Scope.TEST), buildGradleKts);
        //assertTrue(project.containsKey("src/test/java/com/example/HelloWorldTest.java"));
    }
}
