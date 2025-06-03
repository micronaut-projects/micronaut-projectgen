package io.micronaut.projectgen.http.server.controllers;


import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.buildtools.gradle.Gradle;
import io.micronaut.projectgen.core.feature.DefaultFeature;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Property(name = "spec.name", value = "DiffControllerTest")
@MicronautTest
class DiffControllerTest {
    @Test
    void featureDiff(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        Map<String, Object> form = form();
        HttpRequest<?> request = HttpRequest.POST("/api/v1/diff", form)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED_TYPE);
        String diff = assertDoesNotThrow(() -> client.retrieve(request));
        assertNotNull(diff);
        assertEquals("""
            --- build.gradle.kts
            +++ build.gradle.kts
            @@ -1,0 +1,3 @@
            +dependencies {
            +    testImplementation("org.gebish:geb-core:7.0")
            +}


            """, diff);
    }

    @Test
    void featureDownloadDiff(@Client("/") HttpClient httpClient) {
        Map<String, Object> form = form();
        BlockingHttpClient client = httpClient.toBlocking();
        HttpRequest<?> downloadDiff = HttpRequest.POST("/api/v1/download/diff", form)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED_TYPE);
        HttpResponse<byte[]> responseDiff = assertDoesNotThrow(() -> client.exchange(downloadDiff, byte[].class));
        assertTrue(responseDiff.getContentType().isPresent());
        assertEquals(MediaType.TEXT_PLAIN_TYPE, responseDiff.getContentType().get());
        assertEquals("attachment; filename=demo.diff", responseDiff.getHeaders().get(HttpHeaders.CONTENT_DISPOSITION));
        assertTrue(responseDiff.getBody().isPresent());
        byte[] zipByteArray = responseDiff.getBody().get();
        assertTrue(zipByteArray.length > 0);
    }

    @Requires(property = "spec.name", value = "DiffControllerTest")
    @Singleton
    static class GebFeature implements Feature {

        @Override
        public String getName() {
            return "geb-core";
        }

        @Override
        public void apply(GeneratorContext generatorContext) {
            generatorContext.getRootModule().addDependency(Dependency.builder()
                .groupId("org.gebish")
                .artifactId("geb-core")
                .version("7.0")
                .test()
                .build());
        }
    }

    private static Map<String, Object> form() {
        Map<String, Object> form = new HashMap<>();
        form.put("name", "demo");
        form.put("build", "GRADLE");
        form.put("gradleDsl", "KOTLIN");
        form.put("features", List.of("geb-core"));
        return form;
    }
}
