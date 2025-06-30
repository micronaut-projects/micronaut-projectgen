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
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Property(name = "spec.name", value = "DownloadZipControllerTest")
@MicronautTest
class DownloadZipControllerTest {
    @Test
    void featureDownloadZip(@Client("/") HttpClient httpClient) {
        Map<String, Object> form = form();
        BlockingHttpClient client = httpClient.toBlocking();
        HttpRequest<?> request = HttpRequest.POST("/api/v1/download/zip", form)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED_TYPE);
        HttpResponse<byte[]> responseDiff = assertDoesNotThrow(() -> client.exchange(request, byte[].class));
        assertTrue(responseDiff.getContentType().isPresent());
        assertEquals(MediaType.ZIP_TYPE, responseDiff.getContentType().get());
        assertEquals("attachment; filename=demo.zip", responseDiff.getHeaders().get(HttpHeaders.CONTENT_DISPOSITION));
        assertTrue(responseDiff.getBody().isPresent());
        byte[] zipByteArray = responseDiff.getBody().get();
        assertTrue(zipByteArray.length > 0);
    }

    @Requires(property = "spec.name", value = "DownloadZipControllerTest")
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
        form.put("packageName", "com.example");
        form.put("name", "demo");
        form.put("build", "GRADLE");
        form.put("features", List.of("geb-core"));
        return form;
    }
}
