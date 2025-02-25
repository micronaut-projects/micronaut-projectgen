package io.micronaut.projectgen.core.buildtools.maven;

import io.micronaut.projectgen.core.buildtools.Phase;
import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.buildtools.Source;
import io.micronaut.projectgen.core.options.Language;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MavenScopeTest {

    @Test
    void mavenScopeToString() {
        assertEquals("runtime", MavenScope.RUNTIME.toString());
    }

    @ParameterizedTest
    @MethodSource("mavenScopeOf")
    void testProjectNaturalName(Source source, List<Phase> phases, Language language, MavenScope scope) {
        Optional<MavenScope> mavenScopeOptional = MavenScope.of(new Scope(source, phases, 1), language);
        assertTrue(mavenScopeOptional.isPresent());
        assertEquals(scope, mavenScopeOptional.get());
    }

    private static Stream<Arguments> mavenScopeOf() {
        return Stream.of(
        Arguments.of(Source.MAIN, List.of(Phase.DEVELOPMENT), Language.JAVA, MavenScope.PROVIDED),
        Arguments.of(Source.MAIN, List.of(Phase.RUNTIME, Phase.COMPILATION), Language.JAVA,  MavenScope.COMPILE),
        Arguments.of(Source.MAIN, List.of(Phase.RUNTIME, Phase.COMPILATION, Phase.PUBLIC_API), Language.JAVA, MavenScope.COMPILE),
        Arguments.of(Source.MAIN, List.of(Phase.RUNTIME), Language.JAVA, MavenScope.RUNTIME),
        Arguments.of(Source.MAIN, List.of(Phase.COMPILATION) , Language.JAVA, MavenScope.PROVIDED),
        Arguments.of(Source.TEST, List.of(Phase.RUNTIME) , Language.JAVA, MavenScope.TEST),
        Arguments.of(Source.TEST, List.of(Phase.COMPILATION) , Language.JAVA, MavenScope.TEST),
        Arguments.of(Source.TEST, List.of(Phase.RUNTIME, Phase.COMPILATION), Language.JAVA, MavenScope.TEST),
        Arguments.of(Source.MAIN, List.of(Phase.ANNOTATION_PROCESSING), Language.GROOVY, MavenScope.PROVIDED));
    }
}
