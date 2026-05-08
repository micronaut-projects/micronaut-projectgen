package io.micronaut.projectgen.demo;

import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.io.PreviewGenerator;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.test.BuildTestVerifier;
import io.micronaut.projectgen.test.GradleBuildTestVerifier;
import io.micronaut.projectgen.test.MavenBuildTestVerifier;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
class ProjectGenTest {

    @Test
    void testProjectGeneration(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFactory.create(Collections.emptyList());
        Map<String, String> project = previewGenerator.generate(options);
        assertTrue(project.containsKey("mvnw"));
        assertTrue(project.containsKey("mvnw.bat"));
        assertTrue(project.containsKey(".mvn/wrapper/maven-wrapper.jar"));
        assertTrue(project.containsKey(".mvn/wrapper/maven-wrapper.properties"));
        assertTrue(project.containsKey("pom.xml"));
        assertTrue(project.containsKey("src/main/java/com/example/HelloWorld.java"));
        assertFalse(project.containsKey("src/test/java/com/example/HelloWorldTest.java"));
        String pom = project.get("pom.xml");
        BuildTestVerifier verifier = new MavenBuildTestVerifier(pom, options.language());
        assertEquals("25", verifier.getProperty("maven.compiler.source"));
        assertEquals("25", verifier.getProperty("maven.compiler.target"));
        assertTrue(verifier.hasBuildPlugin("org.apache.maven.plugins", "maven-jar-plugin"));
        String buildGradleKts = project.get("build.gradle.kts");
        verifier = new GradleBuildTestVerifier(buildGradleKts, BuildTool.GRADLE, options.language(), options.testFramework());
        assertFalse(verifier.hasDependency("org.junit.jupiter", "junit-jupiter", Scope.TEST));
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
        String gradleWrapperProperties = project.get("gradle/wrapper/gradle-wrapper.properties");
        assertTrue(gradleWrapperProperties.contains("9.5.0"));

        options = OptionsFactory.create(List.of("hello-world-test"));
        project = previewGenerator.generate(options);
        buildGradleKts = project.get("build.gradle.kts");
        assertTrue(project.containsKey("src/test/java/com/example/HelloWorldTest.java"));
        verifier = new GradleBuildTestVerifier(buildGradleKts, BuildTool.GRADLE, options.language(), options.testFramework());
        assertTrue(verifier.hasDependency("org.junit.jupiter", "junit-jupiter", Scope.TEST), buildGradleKts);

        assertTrue(project.containsKey("projectgen.properties"));
        String props = project.get("projectgen.properties");
        assertEquals("""
            artifact=demo-project
            java=JDK_25
            buildTools[0]=maven
            buildTools[1]=gradle
            gradleDsl=KOTLIN
            name=demo
            packageName=com.example
            version=1.0.0
            group=io.micronaut.projectgen
            """, props);
    }
}
