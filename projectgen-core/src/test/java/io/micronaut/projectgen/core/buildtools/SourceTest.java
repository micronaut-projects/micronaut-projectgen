package io.micronaut.projectgen.core.buildtools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SourceTest {

    @Test
    void sourceSetOf() {
        assertEquals(Source.MAIN, Source.of("src/main/java"));
        assertEquals("main", Source.MAIN.getPath());
        assertEquals("test", Source.TEST.getPath());
        assertEquals("integration-test", Source.INTEGRATION_TEST.getPath());
        assertEquals("main", Source.MAIN.toString());
        assertEquals("test", Source.TEST.toString());
        assertEquals("integration-test", Source.INTEGRATION_TEST.toString());
        assertEquals(Source.INTEGRATION_TEST, Source.of("src/integration-test/java"));
        assertEquals(Source.TEST, Source.of("src/test/java"));
        assertThrows(IllegalStateException.class, () -> Source.of("src/foo/java"));
    }
}
