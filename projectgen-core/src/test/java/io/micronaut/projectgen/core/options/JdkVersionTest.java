package io.micronaut.projectgen.core.options;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JdkVersionTest {
    @Test
    void testValueOfIntegerWithaSupportedJDKVersion() {
        assertEquals(JdkVersion.JDK_17, JdkVersion.valueOf(17));
    }

    @Test
    void testValueOfStringWithASupportedJDKVersion() {
        assertEquals(JdkVersion.JDK_17, JdkVersion.valueOf("JDK_17"));
    }

    @Test
    void testValueOfWhenThJDKVersionDoesNotExist() {
        IllegalArgumentException ex =assertThrows(IllegalArgumentException.class, () -> JdkVersion.valueOf(3));
        assertTrue(ex.getMessage().startsWith("Unsupported JDK version: 3. Supported values are "));
    }

    @Test
    void greaterThanOrEqual() {
        assertTrue(JdkVersion.JDK_11.greaterThanEqual(JdkVersion.JDK_11));
        assertTrue(JdkVersion.JDK_17.greaterThanEqual(JdkVersion.JDK_11));
        assertFalse(JdkVersion.JDK_8.greaterThanEqual(JdkVersion.JDK_11));
    }
}
