package io.micronaut.projectgen.core.options;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestFrameworkTest {

    @ParameterizedTest
    @MethodSource("supportedLanguagesArguments")
    void testSupportedLanguages(List<Language> expected, TestFramework testFramework) {
        List<Language> sortedExped = new ArrayList(expected);
        sortedExped.sort(Comparator.comparing(Language::getName));
        List<Language> supportedLanguages = new ArrayList<>(testFramework.getSupportedLanguages());
        supportedLanguages.sort(Comparator.comparing(Language::getName));
        assertEquals(sortedExped, supportedLanguages);
    }

    @ParameterizedTest
    @MethodSource("getDefaultLanguageArguments")
    void testDefaultLanguage(Language lang, TestFramework testFramework) {
        assertEquals(lang, testFramework.getDefaultLanguage());
    }

    @ParameterizedTest
    @MethodSource("getSourcePathArguments")
    void testSourcePath(Language lang, TestFramework testFramework, String expected) {
        String path = "/{packagePath}/{className}";
        assertEquals(expected, testFramework.getSourcePath(path, lang));
    }

    private static Stream<Arguments> supportedLanguagesArguments() {
        return Stream.of(
            Arguments.of(List.of(Language.JAVA, Language.GROOVY, Language.KOTLIN), TestFramework.JUNIT),
            Arguments.of(List.of(Language.GROOVY), TestFramework.SPOCK),
            Arguments.of(List.of(Language.KOTLIN), TestFramework.KOTEST)
        );
    }

    private static Stream<Arguments> getDefaultLanguageArguments() {
        return Stream.of(
            Arguments.of(Language.JAVA, TestFramework.JUNIT),
            Arguments.of(Language.GROOVY, TestFramework.SPOCK),
            Arguments.of(Language.KOTLIN, TestFramework.KOTEST)
            );
    }

    private static Stream<Arguments> getSourcePathArguments() {
        return Stream.of(
            Arguments.of(Language.JAVA, TestFramework.JUNIT, "src/test/java/{packagePath}/{className}Test.java"),
            Arguments.of(Language.GROOVY, TestFramework.JUNIT, "src/test/groovy/{packagePath}/{className}Test.groovy"),
            Arguments.of(Language.KOTLIN, TestFramework.JUNIT, "src/test/kotlin/{packagePath}/{className}Test.kt"),
            Arguments.of(Language.JAVA, TestFramework.SPOCK, "src/test/groovy/{packagePath}/{className}Spec.groovy"),
            Arguments.of(Language.KOTLIN, TestFramework.SPOCK, "src/test/groovy/{packagePath}/{className}Spec.groovy"),
            Arguments.of(Language.GROOVY, TestFramework.SPOCK, "src/test/groovy/{packagePath}/{className}Spec.groovy"),
            Arguments.of(Language.JAVA, TestFramework.KOTEST, "src/test/kotlin/{packagePath}/{className}Test.kt"),
            Arguments.of(Language.KOTLIN, TestFramework.KOTEST, "src/test/kotlin/{packagePath}/{className}Test.kt"),
            Arguments.of(Language.GROOVY, TestFramework.KOTEST, "src/test/kotlin/{packagePath}/{className}Test.kt"));
    }
}
