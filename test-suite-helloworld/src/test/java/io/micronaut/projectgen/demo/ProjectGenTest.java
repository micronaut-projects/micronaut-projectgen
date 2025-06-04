package io.micronaut.projectgen.demo;

import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.io.PreviewGenerator;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.test.BuildTestVerifier;
import io.micronaut.projectgen.test.GradleBuildTestVerifier;
import io.micronaut.projectgen.test.MavenBuildTestVerifier;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
class ProjectGenTest {

    @Test
    void testProjectGeneration(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsUtils.createOptions();
        Map<String, String> project = previewGenerator.generate(options);
        assertTrue(project.containsKey("mvnw"));
        assertTrue(project.containsKey("mvnw.bat"));
        assertTrue(project.containsKey(".mvn/wrapper/maven-wrapper.jar"));
        assertTrue(project.containsKey(".mvn/wrapper/maven-wrapper.properties"));
        assertTrue(project.containsKey("pom.xml"));
        assertTrue(project.containsKey("src/main/java/com/example/HelloWorld.java"));
        String pom = project.get("pom.xml");
        BuildTestVerifier verifier = new MavenBuildTestVerifier(pom, options.language());
        assertEquals("21", verifier.getProperty("maven.compiler.source"));
        assertEquals("21", verifier.getProperty("maven.compiler.target"));
        assertTrue(verifier.hasBuildPlugin("org.apache.maven.plugins", "maven-jar-plugin"));
        String buildGradleKts = project.get("build.gradle.kts");
        System.out.println(buildGradleKts);
        verifier = new GradleBuildTestVerifier(buildGradleKts, BuildTool.GRADLE, options.language(), options.testFramework());
        assertTrue(verifier.hasBuildPlugin("java"));
        assertTrue(verifier.hasBuildPlugin("application"));
        assertTrue(project.containsKey("settings.gradle.kts"));
        String settings = project.get("settings.gradle.kts");
        assertEquals("""
        rootProject.name="demo"
        """, settings);
        assertTrue(project.containsKey("gradlew"));
        assertTrue(project.containsKey("gradlew.bat"));
        assertTrue(project.containsKey("gradle/wrapper/gradle-wrapper.jar"));
        assertTrue(project.containsKey("gradle/wrapper/gradle-wrapper.properties"));
    }
}
