package io.micronaut.projectgen.demo;

import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.gradle.GradleDsl;
import io.micronaut.projectgen.core.options.GenericOptionsBuilder;
import io.micronaut.projectgen.core.options.JdkVersion;
import io.micronaut.projectgen.core.options.Options;

import java.util.List;

public final class OptionsFactory {
    private OptionsFactory() {
    }

    public static Options create(List<String> features) {
        return GenericOptionsBuilder.builder()
                .name("demo")
                .packageName("com.example")
                .group("io.micronaut.projectgen")
                .artifact("demo-project")
                .version("1.0.0")
                .features(features)
                .buildTools(List.of(BuildTool.MAVEN, BuildTool.GRADLE))
                .gradleDsl(GradleDsl.KOTLIN)
                .java(JdkVersion.JDK_25)
                .build();
    }
}
