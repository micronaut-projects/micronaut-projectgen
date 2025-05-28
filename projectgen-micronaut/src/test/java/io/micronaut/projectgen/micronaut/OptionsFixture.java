package io.micronaut.projectgen.micronaut;

import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.options.ConfigurationFormat;
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
        return defaultNoBuildTool()
            .buildTools(List.of(BuildTool.GRADLE_KOTLIN));
    }

    public static Options defaultGradleAndMaven(String feature) {
        return defaultNoBuildTool()
            .features(List.of(feature))
            .buildTools(List.of(BuildTool.GRADLE_KOTLIN, BuildTool.MAVEN))
            .build();
    }

    public static Options defaultMaven(String feature) {
        return OptionsFixture.defaultMaven().features(List.of(feature)).build();
    }

    public static GenericOptionsBuilder defaultMaven() {
        return defaultNoBuildTool()
            .buildTools(List.of(BuildTool.MAVEN));
    }

    private static GenericOptionsBuilder defaultNoBuildTool() {
        return GenericOptionsBuilder.builder()
            .name("demo")
            .packageName("com.example")
            .configurationFormat(ConfigurationFormat.PROPERTIES)
            .language(Language.JAVA)
            .testFramework(TestFramework.JUNIT)
            .java(JdkVersion.JDK_21)
            .template(ApplicationType.DEFAULT.toString());
    }
}
