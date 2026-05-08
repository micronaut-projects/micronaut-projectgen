package io.micronaut.projectgen.demo;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.buildtools.gradle.GradleDsl;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.io.PreviewGenerator;
import io.micronaut.projectgen.core.options.GenericOptionsBuilder;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.options.TestFramework;
import io.micronaut.projectgen.test.BuildTestVerifier;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Property(name = "spec.name", value = "AddDependencyOnlyForBuildTest")
@MicronautTest(startApplication = false)
class AddDependencyOnlyForBuildTest {

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
        BuildTestVerifier gradleVerifier = BuildTestVerifier.of(buildGradleKts, BuildTool.GRADLE, options.language(), options.testFramework());
        assertFalse(gradleVerifier.hasDependency("io.micronaut", "micronaut-http-server-netty"), buildGradleKts);

        String pomXml = project.get("pom.xml");
        assertNotNull(pomXml);
        BuildTestVerifier verifier = BuildTestVerifier.of(pomXml, BuildTool.MAVEN, options.language(), options.testFramework());
        assertTrue(verifier.hasDependency("io.micronaut", "micronaut-http-server-netty"), pomXml);
    }

    @Requires(property = "spec.name", value = "AddDependencyOnlyForBuildTest")
    @Singleton
    static class HttpServerNetty implements Feature {
        private static final Dependency DEPENDENCY_HTTP_SERVER_NETTY = Dependency.builder()
            .groupId("io.micronaut")
            .artifactId("micronaut-http-server-netty")
            .compile()
            .build();

        @Override
        public String getName() {
            return "http-server-netty";
        }

        @Override
        public void apply(GeneratorContext generatorContext) {
            generatorContext.getRootModule().addDependencyOnlyForBuild(DEPENDENCY_HTTP_SERVER_NETTY, BuildTool.MAVEN);
        }
    }

}
