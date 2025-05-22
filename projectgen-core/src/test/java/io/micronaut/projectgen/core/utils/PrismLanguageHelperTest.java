package io.micronaut.projectgen.core.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PrismLanguageHelperTest {
    @ParameterizedTest
    @MethodSource("languageProvider")
    void testFileNameIsValid(String fileName, String expectedLanguage) {
        assertNotNull(fileName);
        assertFalse(fileName.isEmpty());
        assertTrue(fileName.contains("."), "Expected a file extension");
        assertEquals(expectedLanguage, PrismLanguageHelper.getPrismLanguageClass(fileName));
    }

    static Stream<Arguments> languageProvider() {
        return Stream.of(
            Arguments.of("build.gradle", "language-groovy"),
            Arguments.of("build.gradle.kts", "language-kotlin"),
            Arguments.of("README.md", "language-markdown"),
            Arguments.of("Application.java", "language-java"),
            Arguments.of("application.properties", "language-properties")
        );
    }
}
