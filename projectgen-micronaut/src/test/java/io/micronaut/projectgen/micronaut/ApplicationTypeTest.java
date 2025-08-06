package io.micronaut.projectgen.micronaut;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ApplicationTypeTest {

    @Test
    void applicationTypeOf() {
        assertEquals(ApplicationType.DEFAULT, ApplicationType.of("default"));
        assertEquals(ApplicationType.CLI, ApplicationType.of("cli"));
        assertEquals(ApplicationType.LIBRARY, ApplicationType.of("library"));
        assertEquals(ApplicationType.FUNCTION, ApplicationType.of("function"));
        assertEquals(ApplicationType.GRPC, ApplicationType.of("grpc"));
        assertEquals(ApplicationType.MESSAGING, ApplicationType.of("messaging"));
        assertThrows(IllegalArgumentException.class, () -> ApplicationType.of("foo"));
    }
}
