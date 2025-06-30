package io.micronaut.projectgen.core.buildtools;

import io.micronaut.projectgen.core.buildtools.gradle.GradleDsl;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BuildToolUtilsTest {
    @ParameterizedTest
    @MethodSource("settingsFileNameTestCases")
    void testSettingsFileName(String expected, BuildTool buildTool, GradleDsl gradleDsl) {
        assertEquals(expected, BuildToolUtils.settingsFileName(buildTool, gradleDsl));
    }

    private static Stream<Arguments> settingsFileNameTestCases() {
        return Stream.of(
            Arguments.of("settings.gradle", BuildTool.GRADLE, null),
            Arguments.of("settings.gradle.kts", BuildTool.GRADLE, GradleDsl.KOTLIN)
        );
    }


    @ParameterizedTest
    @MethodSource("buildFileNameTestCases")
    void testBuildFileName(String expected, BuildTool buildTool, GradleDsl gradleDsl) {
        assertEquals(expected, BuildToolUtils.buildFileName(buildTool, gradleDsl));
    }

    private static Stream<Arguments> buildFileNameTestCases() {
        return Stream.of(
            Arguments.of("pom.xml", BuildTool.MAVEN, GradleDsl.KOTLIN),
            Arguments.of("pom.xml", BuildTool.MAVEN, GradleDsl.GROOVY),
            Arguments.of("pom.xml", BuildTool.MAVEN, null),
            Arguments.of("build.gradle", BuildTool.GRADLE, null),
            Arguments.of("build.gradle", BuildTool.GRADLE, GradleDsl.GROOVY),
            Arguments.of("build.gradle.kts", BuildTool.GRADLE, GradleDsl.KOTLIN)
        );
    }

}
