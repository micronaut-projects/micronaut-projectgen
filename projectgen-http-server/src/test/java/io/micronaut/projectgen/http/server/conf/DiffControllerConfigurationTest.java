package io.micronaut.projectgen.http.server.conf;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
class DiffControllerConfigurationTest {

    @Test
    void diffControllerConfiguration(DiffControllerConfiguration diffControllerConfiguration) {
        assertEquals("/api/v1/diff", diffControllerConfiguration.getPath());
        assertTrue(diffControllerConfiguration.isEnabled());
    }

}
