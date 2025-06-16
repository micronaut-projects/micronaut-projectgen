package io.micronaut.projectgen.demo;

import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.gradle.GradleDsl;
import io.micronaut.projectgen.core.generator.ProjectGenerator;
import io.micronaut.projectgen.core.options.GenericOptionsBuilder;
import io.micronaut.projectgen.core.options.JdkVersion;
import io.micronaut.projectgen.core.options.Options;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import jakarta.inject.Inject;

import java.io.File;
import java.util.List;

@Command(
    name = "create",
    description = "Create a new project"
)
public class CreateCommand implements Runnable {
    @CommandLine.Option(
        names = { "--output", "-o" },
        required = true,
        description = "The output folder where the project file will be generated")
    private File outputDir;

    @CommandLine.Option(
        names = { "--features", "-f" },
        description = "Comma-separated list of features to include",
        split = ",")
    private List<String> features;

    @Inject
    ProjectGenerator projectGenerator;

    @Override
    public void run() {
        if (!outputDir.exists() || !outputDir.isDirectory()) {
            System.err.println("Provided path is not an existing directory: " + outputDir);
        } else {
            Options options = GenericOptionsBuilder.builder()
                .name("demo")
                .packageName("com.example")
                .group("io.micronaut.projectgen")
                .artifact("demo-project")
                .version("1.0.0")
                .features(features)
                .buildTools(List.of(BuildTool.MAVEN))
                .gradleDsl(GradleDsl.KOTLIN)
                .java(JdkVersion.JDK_21)
                .build();
            projectGenerator.writeTo(options, outputDir);
        }
    }
}
