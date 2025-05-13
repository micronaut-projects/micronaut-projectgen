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

    @Test
    void buildToolOf() {
        assertTrue(BuildTool.of("foo").isEmpty());
        assertEquals(BuildTool.GRADLE, BuildTool.of("gradle").get());
        assertEquals(BuildTool.GRADLE_KOTLIN, BuildTool.of("gradle_kotlin").get());
        assertEquals(BuildTool.MAVEN, BuildTool.of("maven").get());
    }
}
