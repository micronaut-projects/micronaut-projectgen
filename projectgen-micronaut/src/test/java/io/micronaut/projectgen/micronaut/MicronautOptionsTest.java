package io.micronaut.projectgen.micronaut;

import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.options.TestFramework;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MicronautOptionsTest {
    @Test
    void defaultApplicationTypeIsDefault() {
        MicronautOptions options = MicronautOptions.builder().build();
        assertEquals(ApplicationType.DEFAULT, options.applicationType());
    }

    @Test
    void defaultBuildToolIsGradleKotlinDsl() {
        Options options = MicronautOptions.builder().build();
        assertEquals(BuildTool.GRADLE_KOTLIN, options.getBuildTool());
    }

    @Test
    void defaultLanguageIsJava() {
        Options options = MicronautOptions.builder().build();
        assertEquals(Language.JAVA, options.language());
    }

    @Test
    void defaultLanguageIsJavaAndTestFrameworkJunit() {
        Options options = MicronautOptions.builder().build();
        assertEquals(Language.JAVA, options.language());
        assertEquals(TestFramework.JUNIT, options.testFramework());
    }

    @Test
    void languageIsGroovyAndTestFrameworkSpock() {
        Options options = MicronautOptions.builder().language(Language.GROOVY).build();
        assertEquals(Language.GROOVY, options.language());
        assertEquals(TestFramework.SPOCK, options.testFramework());
    }
}
