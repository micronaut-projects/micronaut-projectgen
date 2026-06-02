package io.micronaut.projectgen.demo;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.gradle.GradleDsl;
import io.micronaut.projectgen.core.options.JdkVersion;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
class DiffTest {

    @Test
    void testFeaturesEndpoint(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();

        Map<String, Object> form = new HashMap<>();
        form.put("name", "demo");
        form.put("packageName", "com.example");
        form.put("group", "io.micronaut.projectgen");
        form.put("artifact", "demo-project");
        form.put("version", "1.0.0");
        form.put("features", "hello-world-test");
        form.put("build", List.of(BuildTool.GRADLE));
        form.put("gradleDsl", GradleDsl.KOTLIN);
        form.put("java", JdkVersion.JDK_25);

        HttpRequest<?> request = HttpRequest.POST("/api/v1/diff", form)
            .accept(MediaType.TEXT_PLAIN)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED_TYPE);
        String diff = assertDoesNotThrow(() -> client.retrieve(request));
        assertNotNull(diff);
        assertFalse(diff.contains("--- projectgen.properties"), diff);
        String expected = """
            --- src/test/java/com/example/HelloWorldTest.java
            +++ src/test/java/com/example/HelloWorldTest.java
            @@ -1,0 +1,13 @@
            +package com.example;
            +
            +import org.junit.jupiter.api.Test;
            +
            +import static org.junit.jupiter.api.Assertions.assertEquals;
            +
            +class HelloWorldTest {
            +
            +    @Test
            +    void testHello() {
            +        assertEquals("Hello, World!", HelloWorld.hello());
            +    }
            +}""";
        assertTrue(diff.contains(expected));
        expected = """
            --- build.gradle.kts
            +++ build.gradle.kts
            @@ -4,6 +4,12 @@
             }
             group = "io.micronaut.projectgen"
             version = "1.0.0"
            +repositories {
            +    mavenCentral()
            +}
            +dependencies {
            +    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
            +}
             java {
                 sourceCompatibility = JavaVersion.VERSION_25
                 targetCompatibility = JavaVersion.VERSION_25
            """;
        assertTrue(diff.contains(expected), diff);
    }
}
