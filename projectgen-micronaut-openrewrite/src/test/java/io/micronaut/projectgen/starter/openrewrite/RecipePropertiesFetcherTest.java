package io.micronaut.projectgen.starter.openrewrite;

import io.micronaut.projectgen.core.openrewrite.RecipeFetcher;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class RecipePropertiesFetcherTest {
    private static final String NAME = "io.micronaut.starter.feature.liquibase";

    private static final String BOOTSTRAP_NAME = "io.micronaut.starter.feature.discovery-kubernetes";

    @Test
    void testFetchProperties(RecipeFetcher fetcher) {
        Optional<Properties> propertiesOptional = fetcher.findPropertiesByRecipeName(NAME);
        assertTrue(propertiesOptional.isPresent());
        Properties properties = propertiesOptional.get();
        assertEquals("classpath:db/liquibase-changelog.xml", properties.get("liquibase.datasources.default.change-log"));

        propertiesOptional = fetcher.findBootstrapPropertiesByRecipeName(NAME);
        assertFalse(propertiesOptional.isPresent());
    }

    @Test
    void testFetchBootstrapProperties(RecipeFetcher fetcher) {
        Optional<Properties> propertiesOptional = fetcher.findBootstrapPropertiesByRecipeName(BOOTSTRAP_NAME);
        assertTrue(propertiesOptional.isPresent());
        Properties properties = propertiesOptional.get();
        assertEquals("endpoint", properties.get("kubernetes.client.discovery.mode"));
        assertEquals("true", properties.get("kubernetes.client.discovery.mode-configuration.endpoint.watch.enabled"));

        propertiesOptional = fetcher.findPropertiesByRecipeName(BOOTSTRAP_NAME);
        assertFalse(propertiesOptional.isPresent());
    }
}
