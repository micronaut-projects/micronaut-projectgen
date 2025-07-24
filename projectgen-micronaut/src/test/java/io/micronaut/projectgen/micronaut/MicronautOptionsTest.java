package io.micronaut.projectgen.micronaut;

import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.options.GenericOptions;
import io.micronaut.projectgen.core.options.GenericOptionsBuilder;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.options.TestFramework;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MicronautOptionsTest {
    @Test
    void defaultApplicationTypeIsDefault() {
        Options options = OptionsFixture.defaultGradle().build();
        assertEquals(ApplicationType.DEFAULT.toString(), options.template());
    }

    @Test
    void defaultBuildToolIsGradleKotlinDsl() {
        Options options = OptionsFixture.defaultGradle().build();
        assertEquals(BuildTool.GRADLE, options.getBuildTool());
    }

    @Test
    void defaultLanguageIsJava() {
        Options options = OptionsFixture.defaultGradle().build();
        assertEquals(Language.JAVA, options.language());
    }

    @Test
    void defaultLanguageIsJavaAndTestFrameworkJunit() {
        Options options = OptionsFixture.defaultGradle().build();
        assertEquals(Language.JAVA, options.language());
        assertEquals(TestFramework.JUNIT, options.testFramework());
    }

    @Test
    void languageIsGroovyAndTestFrameworkSpock() {
        Options options = OptionsFixture.defaultGradle().language(Language.GROOVY)
            .testFramework(TestFramework.SPOCK).build();
        assertEquals(Language.GROOVY, options.language());
        assertEquals(TestFramework.SPOCK, options.testFramework());
    }
}
