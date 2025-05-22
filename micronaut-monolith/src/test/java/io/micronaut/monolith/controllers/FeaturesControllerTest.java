package io.micronaut.monolith.controllers;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.FeatureResponse;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.http.server.controllers.FeaturesResponse;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Property(name = "projectgen.features.properties.enabled", value = StringUtils.FALSE)
@Property(name = "projectgen.features.toml.enabled", value = StringUtils.FALSE)
@Property(name = "projectgen.features.yaml.enabled", value = StringUtils.FALSE)
@Property(name = "projectgen.features.config4k.enabled", value = StringUtils.FALSE)
@Property(name = "projectgen.features.gitignore.enabled", value = StringUtils.FALSE)
@Property(name = "spec.name", value = "FeaturesControllerTest")
@MicronautTest
class FeaturesControllerTest {

    @Test
    void downloadFeatures(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        HttpRequest<?> request = HttpRequest.POST("/api/v1/features", form())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED_TYPE);
        HttpClientResponseException ex = assertThrows(HttpClientResponseException.class, () -> client.retrieve(request, FeaturesResponse.class));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }

    private static Map<String, Object> form() {
        Map<String, Object> form = new HashMap<>();
        form.put("name", "demo");
        form.put("build", "GRADLE");
        form.put("features", List.of("geb-core"));
        return form;
    }
}
