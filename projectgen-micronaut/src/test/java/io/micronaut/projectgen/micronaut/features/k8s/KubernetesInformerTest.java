package io.micronaut.projectgen.micronaut.features.k8s;

import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.generator.ProjectGenerator;
import io.micronaut.projectgen.core.io.MapOutputHandler;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.OptionsFixture;
import io.micronaut.projectgen.test.BuildTestVerifier;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
class KubernetesInformerTest {
    @Test
    void kubernetesInformerFeaturesAddsTheDependency(ProjectGenerator micronautProjectGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("kubernetes-informer")).build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, BuildTool.GRADLE, options.language(), options.testFramework());
        assertTrue(verifier.hasDependency("io.micronaut.kubernetes", "micronaut-kubernetes-informer", Scope.COMPILE), buildGradle);
    }

    @Disabled
    //TODO enable this test
    @Test
    void kubernetesInformerFeaturesAddsTheDiscoveryCoreDependencyForGroovyAndMaven(ProjectGenerator micronautProjectGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().buildTools(List.of(BuildTool.MAVEN))
            .features(List.of("kubernetes-informer"))
            .language(Language.GROOVY).build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String pom = project.get("pom.xml");
        assertNotNull(pom);
        BuildTestVerifier verifier = BuildTestVerifier.of(pom, BuildTool.MAVEN, options.language(), options.testFramework());
        assertTrue(verifier.hasDependency("io.micronaut", "micronaut-discovery-core", Scope.COMPILE), pom);
    }

    @Test
    void kubernetesInformerFeaturesAddsTheLinkInReadmeFile(ProjectGenerator micronautProjectGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("kubernetes-informer")).build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://micronaut-projects.github.io/micronaut-kubernetes/latest/guide/#kubernetes-informer"));
        assertTrue(readme.contains("https://github.com/kubernetes-client/java/wiki"));

    }

    private static Map<String, String> generateProject(ProjectGenerator micronautProjectGenerator,
        Options options) throws Exception {
        MapOutputHandler outputHandler = new MapOutputHandler();
        micronautProjectGenerator.generate(options, outputHandler);
        return outputHandler.getProject();
    }
}
