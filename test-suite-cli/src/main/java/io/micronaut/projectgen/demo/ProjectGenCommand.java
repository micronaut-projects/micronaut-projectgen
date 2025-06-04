package io.micronaut.projectgen.demo;

import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.core.io.Writable;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.gradle.GradleDsl;
import io.micronaut.projectgen.core.io.zip.ZipGenerator;
import io.micronaut.projectgen.core.options.GenericOptionsBuilder;
import io.micronaut.projectgen.core.options.JdkVersion;
import io.micronaut.projectgen.core.options.Options;
import jakarta.inject.Inject;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.ZipOutputStream;

@Command(name = "projectgen", description = "Generates a ZIP file for a project",
        mixinStandardHelpOptions = true)
public class ProjectGenCommand implements Runnable {
    @CommandLine.Option(names = { ".", "--output", "-o" }, required = true,
        description = "The output folder where the zip file will be created")
    private File outputDir;

    @Inject
    ZipGenerator zipGenerator;

    @CommandLine.Option(names = { "--name", "-n" }, required = true,
        description = "The name of the zip file (without extension)")
    private String zipName;

    public static void main(String[] args) {
        PicocliRunner.run(ProjectGenCommand.class, args);
    }

    public void run() {
        if (!outputDir.exists() || !outputDir.isDirectory()) {
            err("Provided path is not an existing directory: " + outputDir);
        } else {
            Options options = createOptions(zipName);
            Path zipPath = outputDir.toPath().resolve(zipName + ".zip");
            Writable zip = zipGenerator.zip(options);
            try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath.toFile()))) {
                zip.writeTo(zos);
            } catch (IOException e) {
                err("Error while generating the zip file: " + e.getMessage());
            }
        }
    }

    private static void err(String message) {
        System.err.println(message);
    }

    public static Options createOptions(String name) {
        return GenericOptionsBuilder.builder()
            .name(name)
            .packageName("com.example")
            .group("io.micronaut.projectgen")
            .artifact("demo-project")
            .version("1.0.0")
            .buildTools(List.of(BuildTool.MAVEN, BuildTool.GRADLE))
            .gradleDsl(GradleDsl.KOTLIN)
            .java(JdkVersion.JDK_21)
            .build();
    }
}
