package io.micronaut.projectgen.core.options;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LanguageTest {

    @ParameterizedTest
    @MethodSource("sourcePathArgs")
    void testProjectNaturalName(Language lang, String expected) {
        String path = "/{packagePath}/{className}";
        assertEquals( expected, lang.getSourcePath(path));
    }

    @Test
    void languageOf() {
        assertEquals(Language.JAVA, Language.of("java").get());
        assertEquals(Language.KOTLIN, Language.of("kotlin").get());
        assertEquals(Language.GROOVY, Language.of("groovy").get());
        assertTrue(Language.of("foo").isEmpty());
    }

    private static Stream<Arguments> sourcePathArgs() {
        return Stream.of(
            Arguments.of(Language.JAVA, "src/main/java/{packagePath}/{className}.java"),
            Arguments.of(Language.GROOVY, "src/main/groovy/{packagePath}/{className}.groovy"),
            Arguments.of(Language.KOTLIN, "src/main/kotlin/{packagePath}/{className}.kt")
        );
    }
}
