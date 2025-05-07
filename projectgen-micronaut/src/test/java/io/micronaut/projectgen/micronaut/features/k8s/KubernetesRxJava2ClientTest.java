package io.micronaut.projectgen.micronaut.features.k8s;

import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.io.MapOutputHandler;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.micronaut.MicronautOptions;
import io.micronaut.projectgen.micronaut.MicronautProjectGenerator;
import io.micronaut.projectgen.test.BuildTestVerifier;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class KubernetesRxJava2ClientTest {
    @Test
    void kubernetesRxjavaClientFeaturesAddsTheDependency(MicronautProjectGenerator micronautProjectGenerator) throws Exception {
        MicronautOptions options = MicronautOptions.builder().feature("kubernetes-rxjava2-client").build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, BuildTool.GRADLE_KOTLIN, options.language(), options.testFramework());
        assertTrue(verifier.hasDependency("io.micronaut.kubernetes", "micronaut-kubernetes-client-rxjava2", Scope.COMPILE), buildGradle);
        assertTrue(verifier.hasDependency("io.micronaut.rxjava2", "micronaut-rxjava2", Scope.COMPILE), buildGradle);
    }

    @Disabled //TODO enable this test
    @Test
    void kubernetesRxjavaClientAddsTheDiscoveryCoreDependencyForGroovyAndMaven(MicronautProjectGenerator micronautProjectGenerator) throws Exception {
        MicronautOptions options = MicronautOptions.builder().buildTool(BuildTool.MAVEN)
            .feature("kubernetes-rxjava2-client")
            .language(Language.GROOVY).build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String pom = project.get("pom.xml");
        assertNotNull(pom);
        BuildTestVerifier verifier = BuildTestVerifier.of(pom, BuildTool.MAVEN, options.language(), options.testFramework());
        assertTrue(verifier.hasDependency("io.micronaut", "micronaut-discovery-core", Scope.COMPILE), pom);
    }

    @Test
    void kubernetesRxjavaClientFeaturesAddsTheLinkInReadmeFile(MicronautProjectGenerator micronautProjectGenerator) throws Exception {
        MicronautOptions options = MicronautOptions.builder().feature("kubernetes-rxjava2-client").build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://micronaut-projects.github.io/micronaut-kubernetes/latest/guide/#kubernetes-client"));
        assertTrue(readme.contains("https://github.com/kubernetes-client/java/wiki"));

    }

    private static Map<String, String> generateProject(MicronautProjectGenerator micronautProjectGenerator,
                                                       MicronautOptions options) throws Exception {
        MapOutputHandler outputHandler = new MapOutputHandler();
        micronautProjectGenerator.generate(options, outputHandler);
        return outputHandler.getProject();
    }
}
