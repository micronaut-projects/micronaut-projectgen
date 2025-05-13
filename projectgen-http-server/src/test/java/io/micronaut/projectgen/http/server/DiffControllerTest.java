package io.micronaut.projectgen.http.server;


import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.buildtools.gradle.Gradle;
import io.micronaut.projectgen.core.buildtools.gradle.GradleDsl;
import io.micronaut.projectgen.core.diff.FeatureDiffer;
import io.micronaut.projectgen.core.feature.DefaultFeature;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.options.GenericOptionsBuilder;
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

@Property(name = "spec.name", value = "DiffControllerTest")
@MicronautTest
class DiffControllerTest {

    @Test
    void featureDiff(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        Map<String, Object> form = new HashMap<>();

        form.put("packageName", "com.example");
        form.put("name", "demo");
        form.put("lang", "JAVA");
        form.put("build", "GRADLE_KOTLIN");
        form.put("test", "JUNIT");
        form.put("java", "JDK_21");
        form.put("features", List.of("geb-core"));

        HttpRequest<?> request = HttpRequest.POST("/diff", form)
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

    @Requires(property = "spec.name", value = "DiffControllerTest")
    @Singleton
    static class GradleBuildDefaultFeature implements DefaultFeature {

        private final Gradle gradle;

        GradleBuildDefaultFeature(Gradle gradle) {
            this.gradle = gradle;
        }

        @Override
        public String getName() {
            return "geb-default-feature";
        }

        @Override
        public void processSelectedFeatures(FeatureContext featureContext) {
            if (OptionUtils.hasGradleBuildTool(featureContext.getOptions())) {
                featureContext.addFeatureIfNotPresent(Gradle.class, gradle);
            }
        }

        @Override
        public boolean isVisible() {
            return false;
        }

        @Override
        public boolean shouldApply(Options options, Set<Feature> selectedFeatures) {
            return true;
        }
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

}
