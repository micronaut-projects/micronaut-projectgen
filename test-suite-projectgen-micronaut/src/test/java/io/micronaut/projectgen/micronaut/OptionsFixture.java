package io.micronaut.projectgen.micronaut;

import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.gradle.GradleDsl;
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
            .buildTools(List.of(BuildTool.GRADLE))
            .gradleDsl(GradleDsl.KOTLIN);
    }

    public static Options defaultGradleAndMaven(String feature) {
        return defaultNoBuildTool()
            .features(List.of(feature))
            .buildTools(List.of(BuildTool.GRADLE, BuildTool.MAVEN))
            .gradleDsl(GradleDsl.KOTLIN)
            .build();
    }

    public static Options defaultMaven(String feature) {
        return OptionsFixture.defaultMaven().features(List.of(feature)).build();
    }

    public static GenericOptionsBuilder defaultMaven() {
        return defaultNoBuildTool()
            .buildTools(List.of(BuildTool.MAVEN));
    }

    public static GenericOptionsBuilder defaultNoBuildTool() {
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
