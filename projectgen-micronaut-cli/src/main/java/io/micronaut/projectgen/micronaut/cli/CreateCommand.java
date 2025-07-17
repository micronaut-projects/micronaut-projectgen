package io.micronaut.projectgen.micronaut.cli;

import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.gradle.GradleDsl;
import io.micronaut.projectgen.core.generator.ProjectGenerator;
import io.micronaut.projectgen.core.options.ConfigurationFormat;
import io.micronaut.projectgen.core.options.GenericOptionsBuilder;
import io.micronaut.projectgen.core.options.JdkVersion;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.options.TestFramework;
import io.micronaut.projectgen.micronaut.ApplicationType;
import jakarta.inject.Inject;
import picocli.CommandLine;
import picocli.CommandLine.Command;

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
            //TODO make this Picocli options
            Options options = GenericOptionsBuilder.builder()
                .name("demo")
                .packageName("com.example")
                .group("example.micronaut")
                .artifact("demo")
                .version("1.0.0")
                .features(features)
                .configurationFormat(ConfigurationFormat.PROPERTIES)
                .language(Language.JAVA)
                .testFramework(TestFramework.JUNIT)
                .java(JdkVersion.JDK_21)
                .template(ApplicationType.DEFAULT.toString())
                .buildTools(List.of(BuildTool.GRADLE))
                .gradleDsl(GradleDsl.KOTLIN)
                .java(JdkVersion.JDK_21)
                .build();
            projectGenerator.writeTo(options, outputDir);
        }
    }
}
