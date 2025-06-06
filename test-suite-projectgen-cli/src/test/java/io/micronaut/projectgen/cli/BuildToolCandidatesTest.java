package io.micronaut.projectgen.cli;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BuildToolCandidatesTest {
    @Test
    void buildToolCandidates() {
        assertEquals(List.of("gradle", "maven"),
            new BuildToolCandidates());
    }
}
