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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    void providedScope() {
        Optional<MavenScope> mavenScope = MavenScope.of(Scope.COMPILE_ONLY, Language.JAVA);
        assertTrue(mavenScope.isPresent());
        assertEquals(MavenScope.PROVIDED, mavenScope.get());
    }

    @ParameterizedTest
    @MethodSource("mavenScopeOfString")
    void testProjectNaturalName(String scope, MavenScope mavenScope) {
        Optional<MavenScope> mavenScopeOptional = MavenScope.of(scope);
        assertTrue(mavenScopeOptional.isPresent());
        assertEquals(mavenScope, mavenScopeOptional.get());
        assertTrue(MavenScope.of("foo").isEmpty());
    }

    @ParameterizedTest
    @MethodSource("toScope")
    void testToScope(MavenScope mavenScope, Optional<Scope> scopeOptional) {
        Optional<Scope> toScopeResult = mavenScope.toScope();
        if (toScopeResult.isPresent()) {
            assertTrue(scopeOptional.isPresent());
            Scope expectedScope = scopeOptional.get();
            Scope result = toScopeResult.get();
            assertEquals(expectedScope, result);
        } else {
            assertTrue(scopeOptional.isEmpty());
        }
    }

    private static Stream<Arguments> mavenScopeOfString() {
        return Stream.of(
            Arguments.of("compile", MavenScope.COMPILE),
            Arguments.of("provided", MavenScope.PROVIDED),
            Arguments.of("runtime", MavenScope.RUNTIME),
            Arguments.of("test", MavenScope.TEST),
            Arguments.of("system", MavenScope.SYSTEM),
            Arguments.of("import", MavenScope.IMPORT)
        );
    }

    private static Stream<Arguments> toScope() {
        return Stream.of(
            Arguments.of(MavenScope.COMPILE, Optional.of(Scope.COMPILE)),
            Arguments.of(MavenScope.PROVIDED, Optional.of(Scope.COMPILE_ONLY)),
            Arguments.of(MavenScope.RUNTIME, Optional.of(Scope.RUNTIME)),
            Arguments.of(MavenScope.TEST, Optional.of(Scope.TEST)),
            Arguments.of(MavenScope.SYSTEM, Optional.empty()),
            Arguments.of(MavenScope.IMPORT, Optional.empty())
        );
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
