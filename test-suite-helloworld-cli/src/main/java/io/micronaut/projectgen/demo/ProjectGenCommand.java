package io.micronaut.projectgen.demo;

import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.projectgen.core.generator.ProjectGenerator;
import io.micronaut.projectgen.core.options.Options;
import jakarta.inject.Inject;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import java.io.File;

@Command(
    name = "projectgen",
    description = "Generates a ZIP file for a project",
    mixinStandardHelpOptions = true
)
public class ProjectGenCommand implements Runnable {
    @CommandLine.Option(
        names = { "--output", "-o" },
        required = true,
        description = "The output folder where the project file will be generated")
    private File outputDir;

    @Inject
    ProjectGenerator projectGenerator; // <1>

    public static void main(String[] args) {
        PicocliRunner.run(ProjectGenCommand.class, args);
    }

    public void run() {
        if (!outputDir.exists() || !outputDir.isDirectory()) {
            System.err.println("Provided path is not an existing directory: " + outputDir);
        } else {
            Options options = OptionsUtils.createOptions();
            projectGenerator.writeTo(options, outputDir);
        }
    }
}
