package io.micronaut.projectgen.micronaut;

import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.options.GenericOptionsBuilder;
import io.micronaut.projectgen.core.options.JdkVersion;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.options.TestFramework;

import java.util.List;

public class OptionsFixture {

    public static Options defaultGradle(String feature) {
        return OptionsFixture.defaultGradle().features(List.of(feature)).build();
    }

    public static GenericOptionsBuilder defaultGradle() {
        return GenericOptionsBuilder.builder()
            .name("demo")
            .packageName("com.example")
            .language(Language.JAVA)
            .testFramework(TestFramework.JUNIT)
            .java(JdkVersion.JDK_21)
            .template(ApplicationType.DEFAULT.toString())
            .buildTools(List.of(BuildTool.GRADLE_KOTLIN));
    }
}
