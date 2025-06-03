package io.micronaut.projectgen.core.buildtools;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BuildToolTest {

    @Test
    void buildToolOf() {
        assertTrue(BuildTool.of("foo").isEmpty());
        assertEquals(BuildTool.GRADLE, BuildTool.of("gradle").get());
        assertEquals(BuildTool.MAVEN, BuildTool.of("maven").get());
    }
}
