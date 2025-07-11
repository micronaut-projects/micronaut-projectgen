package io.micronaut.projectgen.demo;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.dependencies.Coordinate;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.buildtools.gradle.GradleDsl;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.io.PreviewGenerator;
import io.micronaut.projectgen.core.options.GenericOptionsBuilder;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.options.TestFramework;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Property(name = "spec.name", value = "DependencyWithoutGroupTest")
@MicronautTest(startApplication = false)
class DependencyWithoutGroupTest {
    @Test
    void addDependencyOnlyForBuild(PreviewGenerator previewGenerator) throws Exception {
        Options options = GenericOptionsBuilder.builder()
            .name("demo")
            .packageName("example")
            .language(Language.JAVA)
            .gradleDsl(GradleDsl.KOTLIN)
            .testFramework(TestFramework.JUNIT)
            .buildTools(List.of(BuildTool.MAVEN, BuildTool.GRADLE))
            .features(List.of("http-server-netty"))
            .build();
        Map<String, String> project = previewGenerator.generate(options);

        String buildGradleKts = project.get("build.gradle.kts");
        assertNotNull(buildGradleKts);
        assertTrue(buildGradleKts.contains("implementation(mn.micronaut.http.server.netty)"), buildGradleKts);

        List<Dependency> dependencies = new ArrayList<>(List.of(
            Dependency.builder().artifactId("foo").compile().build(),
            Dependency.builder().artifactId("bar").compile().build()));
        assertDoesNotThrow(() -> dependencies.sort(Coordinate.COMPARATOR));

    }

    @Requires(property = "spec.name", value = "DependencyWithoutGroupTest")
    @Singleton
    static class HttpServerNetty implements Feature {
        @Override
        public String getName() {
            return "http-server-netty";
        }

        @Override
        public void apply(GeneratorContext generatorContext) {
            generatorContext.getRootModule().addDependency(Dependency.builder().artifactId("mn.micronaut.http.server.netty").compile().build());
        }
    }
}
