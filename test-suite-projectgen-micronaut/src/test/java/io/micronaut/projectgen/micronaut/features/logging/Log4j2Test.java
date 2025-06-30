package io.micronaut.projectgen.micronaut.features.logging;

import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.generator.ProjectGenerator;
import io.micronaut.projectgen.core.io.MapOutputHandler;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.OptionsFixture;
import io.micronaut.projectgen.test.BuildTestVerifier;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class Log4j2Test {

    @Test
    void log4j2FeaturesAddsTheDependency(ProjectGenerator projectGenerator) throws Exception {
//        Options options = OptionsFixture.defaultGradle("log4j2");
        Options options = OptionsFixture.defaultGradle().buildTools(List.of(BuildTool.MAVEN))
            .features(List.of("log4j2"))
            .build();
        Map<String, String> project = generateProject(projectGenerator, options);
//        for (Map.Entry<String, String> entry : project.entrySet()) {
//            System.out.println("File: " + entry.getKey());
//            System.out.println("Content: " + entry.getValue());
//            System.out.println("-------------------");
//        }
//        String buildGradle = project.get("build.gradle.kts");
        String pom = project.get("pom.xml");
        System.out.println(pom);
        BuildTestVerifier verifier = BuildTestVerifier.of(pom, BuildTool.MAVEN, options.language(), options.testFramework());

//        assertNotNull(buildGradle);
//        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
//        assertTrue(verifier.hasBom("org.apache.logging.log4j", "log4j-bom", Scope.COMPILE), pom);
//        assertTrue(verifier.hasDependency("org.apache.logging.log4j", "log4j-api", Scope.COMPILE), buildGradle);
//        assertTrue(verifier.hasDependency("org.apache.logging.log4j", "log4j-core", Scope.RUNTIME), buildGradle);
//        assertTrue(verifier.hasDependency("org.apache.logging.log4j", "log4j-slf4j-impl", Scope.RUNTIME), buildGradle);
////        assertTrue(buildGradle.contains("2.23.1"));
//        assertTrue(project.containsKey("src/main/resources/log4j2.xml"));
    }

    private static Map<String, String> generateProject(ProjectGenerator micronautProjectGenerator,
                                                       Options options) throws Exception {
        MapOutputHandler outputHandler = new MapOutputHandler();
        micronautProjectGenerator.generate(options, outputHandler);
        return outputHandler.getProject();

    }
}
