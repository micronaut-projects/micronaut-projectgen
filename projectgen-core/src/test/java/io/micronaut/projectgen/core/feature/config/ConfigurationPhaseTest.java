package io.micronaut.projectgen.core.feature.config;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ConfigurationPhaseTest {
    @ParameterizedTest
    @MethodSource("configurationPhases")
    void configurationPhaseToString(ConfigurationPhase phase, String result) {
        assertEquals(result, phase.toString());
    }

    private static Stream<Arguments> configurationPhases() {
        return Stream.of(
            Arguments.of(ConfigurationPhase.BOOTSTRAP, "bootstrap"),
            Arguments.of(ConfigurationPhase.APPLICATION, "application")
        );
    }
}
