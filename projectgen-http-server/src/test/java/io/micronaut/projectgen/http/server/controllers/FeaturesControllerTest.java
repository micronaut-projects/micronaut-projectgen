package io.micronaut.projectgen.http.server.controllers;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.FeatureResponse;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

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
        FeaturesResponse response = assertDoesNotThrow(() -> client.retrieve(request, FeaturesResponse.class));
        assertNotNull(request);

        assertEquals(1, response.features().size(), String.join(", ", response.features().stream().map(FeatureResponse::name).toList()));
        assertEquals(new FeatureResponse("geb-core", "Geb Core", null, false, false), response.features().get(0));
    }

    @Requires(property = "spec.name", value = "FeaturesControllerTest")
    @Singleton
    static class GebFeature implements Feature {
        @Override
        public String getName() {
            return "geb-core";
        }

        @Override
        @NonNull
        public String getTitle() {
            return "Geb Core";
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

    @Requires(property = "spec.name", value = "FeaturesControllerTest")
    @Singleton
    static class SeleniumFirefox implements Feature {
        @Override
        public String getName() {
            return "selenium-firefox-driver";
        }

        @Override
        @NonNull
        public String getTitle() {
            return "Selenium Driver: Firefox";
        }

        @Override
        public boolean isVisible() {
            return false;
        }

        @Override
        public void apply(GeneratorContext generatorContext) {
            generatorContext.getRootModule().addDependency(Dependency.builder()
                .groupId("org.seleniumhq.selenium")
                .artifactId("selenium-firefox-driver")
                .version("4.2.2")
                .test()
                .build());
        }
    }

    private static Map<String, Object> form() {
        Map<String, Object> form = new HashMap<>();
        form.put("name", "demo");
        form.put("build", "GRADLE");
        form.put("features", List.of("geb-core"));
        return form;
    }
}
