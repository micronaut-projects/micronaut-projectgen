package io.micronaut.projectgen.http.server.conf;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class FeaturesControllerConfigurationTest {
    @Test
    void diffControllerConfiguration(FeaturesControllerConfiguration config) {
        assertEquals("/api/v1/features", config.getPath());
        assertTrue(config.isEnabled());
    }
}
