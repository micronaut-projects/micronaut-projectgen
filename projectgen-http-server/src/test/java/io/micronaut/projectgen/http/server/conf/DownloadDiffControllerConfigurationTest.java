package io.micronaut.projectgen.http.server.conf;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class DownloadDiffControllerConfigurationTest {
    @Test
    void diffControllerConfiguration(DownloadDiffControllerConfiguration config) {
        assertEquals("/api/v1/download/diff", config.getPath());
        assertTrue(config.isEnabled());
    }
}
