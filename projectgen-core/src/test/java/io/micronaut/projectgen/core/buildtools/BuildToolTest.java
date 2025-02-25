package io.micronaut.projectgen.core.buildtools;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BuildToolTest {

    @Test
    void valueGradleReturnsGroovyAndKotlinDsl() {
        assertTrue(BuildTool.valuesGradle().stream().allMatch(BuildTool::isGradle));
        assertEquals(List.of(BuildTool.GRADLE, BuildTool.GRADLE_KOTLIN), BuildTool.valuesGradle());
    }
}
