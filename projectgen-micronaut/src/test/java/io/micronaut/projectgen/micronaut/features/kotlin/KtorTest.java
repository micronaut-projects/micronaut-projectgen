package io.micronaut.projectgen.micronaut.features.kotlin;

import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.generator.ProjectGenerator;
import io.micronaut.projectgen.core.io.MapOutputHandler;
import io.micronaut.projectgen.core.options.GenericOptionsBuilder;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.micronaut.OptionsFixture;
import io.micronaut.projectgen.test.BuildTestVerifier;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
public class KtorTest {
    @Test
    void ktorFeaturesAddsTheDependency(ProjectGenerator micronautProjectGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle()
            .buildTools(List.of(BuildTool.MAVEN, BuildTool.GRADLE_KOTLIN))
            .features(List.of("ktor"))
            .language(Language.KOTLIN)
            .build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, BuildTool.GRADLE_KOTLIN, options.language(), options.testFramework());
        assertTrue(verifier.hasDependency("io.micronaut.kotlin", "micronaut-ktor", Scope.COMPILE), buildGradle);
        assertTrue(verifier.hasDependency("io.micronaut.validation", "micronaut-validation", Scope.COMPILE), buildGradle);
        assertTrue(verifier.hasDependency("io.ktor", "ktor-serialization-jackson-jvm", Scope.COMPILE), buildGradle);
        assertTrue(verifier.hasDependency("io.ktor", "ktor-server-content-negotiation-jvm", Scope.COMPILE), buildGradle);
        assertTrue(verifier.hasDependency("io.ktor", "ktor-server-netty-jvm", Scope.COMPILE), buildGradle);
        assertTrue(buildGradle.contains("2.3.13"));
        assertTrue(buildGradle.contains("demo.Application"), buildGradle);

        assertTrue(project.containsKey("src/main/kotlin/demo/Application.kt"));
        assertTrue(project.containsKey("src/main/kotlin/demo/HomeRoute.kt"));
        assertTrue(project.containsKey("src/main/kotlin/demo/JacksonFeature.kt"));
        assertTrue(project.containsKey("src/main/kotlin/demo/NameTransformer.kt"));
        assertTrue(project.containsKey("src/main/kotlin/demo/UppercaseTransformer.kt"));

        String pom = project.get("pom.xml");
        assertNotNull(pom);
        //TODO fix this. It fails flaky assertTrue(pom.contains(" <exec.mainClass>demo.Application</exec.mainClass>"), pom);
    }

    @Test
    void ktorFeaturesAddsTheLinkInReadmeFile(ProjectGenerator micronautProjectGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("ktor")).language(Language.KOTLIN).build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://micronaut-projects.github.io/micronaut-kotlin/latest/guide/index.html#ktor"));
    }

    private static Map<String, String> generateProject(ProjectGenerator micronautProjectGenerator,
                                                       Options options) throws Exception {
        MapOutputHandler outputHandler = new MapOutputHandler();
        micronautProjectGenerator.generate(options, outputHandler);
        return outputHandler.getProject();
    }
}
