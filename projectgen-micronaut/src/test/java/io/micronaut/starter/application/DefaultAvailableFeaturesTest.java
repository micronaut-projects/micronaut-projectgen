package io.micronaut.starter.application;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
class DefaultAvailableFeaturesTest {

    @Test
    void testDefaultAvailableFeatures(DefaultAvailableFeatures features) {
        assertTrue(features.findFeature("liquibase").isPresent());
    }
}
