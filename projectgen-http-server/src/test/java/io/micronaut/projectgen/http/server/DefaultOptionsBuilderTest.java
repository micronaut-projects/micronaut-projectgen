package io.micronaut.projectgen.http.server;

import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.gradle.GradleDsl;
import io.micronaut.projectgen.core.options.JdkVersion;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.options.TestFramework;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultOptionsBuilderTest {

    @Test
    void optionsBuilder() {
        DefaultOptionsBuilder builder = new DefaultOptionsBuilder();
        Map<String, Object> form = Map.of("name", "geb-demo",
            "packageName", "com.example",
            "group", "com.example",
            "artifact", "geb-demo",
            "version", "1.0.0",
            "build", "GRADLE",
            "gradleDsl", "GROOVY",
            "testFramework", "SPOCK",
            "lang", Language.GROOVY,
            "java", "25");
        Options options = builder.createOptions(form);

        assertEquals("geb-demo", options.name());
        assertEquals("com.example", options.packageName());
        assertEquals("com.example", options.group());
        assertEquals("geb-demo", options.artifact());
        assertEquals("1.0.0", options.version());
        assertEquals(options.buildTools(), List.of(BuildTool.GRADLE));
        assertEquals(JdkVersion.JDK_25, options.java());
        assertEquals(GradleDsl.GROOVY, options.gradleDsl());
        assertEquals(Language.GROOVY, options.language());
        assertEquals(TestFramework.SPOCK, options.testFramework());

        form = Map.of(
            "name", "enum-list-demo",
            "build", List.of(BuildTool.GRADLE, BuildTool.MAVEN),
            "features", List.of("geb-core"));
        options = builder.createOptions(form);

        assertEquals(List.of(BuildTool.GRADLE, BuildTool.MAVEN), options.buildTools());
        assertEquals(List.of("geb-core"), options.features());
    }
}
