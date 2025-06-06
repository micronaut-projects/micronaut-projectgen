package io.micronaut.projectgen.demo;

import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.projectgen.core.generator.ProjectGenerator;
import io.micronaut.projectgen.core.options.Options;
import jakarta.inject.Inject;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import java.io.File;
import java.util.List;

@Command(
    name = "projectgen",
    description = "Generates a project in the supplied folder",
    mixinStandardHelpOptions = true
)
public class ProjectGenCommand implements Runnable {
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
    ProjectGenerator projectGenerator; // <1>

    public static void main(String[] args) {
        PicocliRunner.run(ProjectGenCommand.class, args);
    }

    public void run() {
        if (!outputDir.exists() || !outputDir.isDirectory()) {
            System.err.println("Provided path is not an existing directory: " + outputDir);
        } else {
            Options options = OptionsFactory.create(features);
            projectGenerator.writeTo(options, outputDir);
        }
    }
}
