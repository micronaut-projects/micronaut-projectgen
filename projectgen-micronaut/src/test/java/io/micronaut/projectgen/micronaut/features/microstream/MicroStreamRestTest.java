package io.micronaut.projectgen.micronaut.features.microstream;

import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.buildtools.maven.MavenScope;
import io.micronaut.projectgen.core.generator.ProjectGenerator;
import io.micronaut.projectgen.core.io.MapOutputHandler;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.OptionsFixture;
import io.micronaut.projectgen.test.BuildTestVerifier;
import io.micronaut.projectgen.test.ConfigurationUtils;
import io.micronaut.projectgen.test.MavenBuildTestVerifier;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class MicroStreamRestTest {
    @Test
    void microstreamRestFeaturesAddsTheDependency(ProjectGenerator micronautProjectGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradleAndMaven("microstream-rest");
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, BuildTool.GRADLE_KOTLIN, options.language(), options.testFramework());
        assertTrue(verifier.hasDependency("io.micronaut.microstream", "micronaut-microstream-rest", Scope.DEVELOPMENT_ONLY), buildGradle);

        String pom = project.get("pom.xml");
        assertNotNull(pom);
        verifier = BuildTestVerifier.of(pom, BuildTool.MAVEN, options.language(), options.testFramework());
        if (verifier instanceof MavenBuildTestVerifier mavenBuildTestVerifier) {
            assertTrue(mavenBuildTestVerifier.hasDependency("io.micronaut.microstream", "micronaut-microstream-rest", MavenScope.PROVIDED), pom);
        }
    }

    @Test
    void microstreamRestFeaturesMaven(ProjectGenerator micronautProjectGenerator) throws Exception {
        Options options = OptionsFixture.defaultMaven("microstream-rest");
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String pom = project.get("pom.xml");
        assertNotNull(pom);
        BuildTestVerifier verifier = BuildTestVerifier.of(pom, BuildTool.MAVEN, options.language(), options.testFramework());
        if (verifier instanceof MavenBuildTestVerifier mavenBuildTestVerifier) {
            assertTrue(mavenBuildTestVerifier.hasDependency("io.micronaut.microstream", "micronaut-microstream-rest"), pom);
            assertTrue(mavenBuildTestVerifier.hasDependency("io.micronaut.microstream", "micronaut-microstream-rest", MavenScope.PROVIDED), pom);
        }
    }

    @Test
    void microstreamRestFeaturesAddsTheLinkInReadmeFile(ProjectGenerator micronautProjectGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("microstream-rest")).build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://micronaut-projects.github.io/micronaut-microstream/latest/guide/#rest"));
        assertTrue(readme.contains("https://docs.microstream.one/manual/storage/rest-interface/index.html"));
    }

    private static Map<String, String> generateProject(ProjectGenerator micronautProjectGenerator,
                                                       Options options) throws Exception {
        MapOutputHandler outputHandler = new MapOutputHandler();
        micronautProjectGenerator.generate(options, outputHandler);
        return outputHandler.getProject();
    }
}
