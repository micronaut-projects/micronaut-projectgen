/*
 * Copyright 2003-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.projectgen.nativeimage;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.PathSensitive;
import org.gradle.api.tasks.PathSensitivity;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * Writes the GraalVM reachability metadata that the Rocker templates of a module need.
 *
 * <p>Rocker keeps the plain text of a template in a nested {@code $PlainText} class and reads it back
 * when the template class is initialized. {@code PlainTextUnloadedClassLoader} does that by looking the
 * class file up as a resource and then loading the class to read its static fields, so both the class
 * file as a resource and the reflective access to the fields have to be registered for a native image.</p>
 */
@CacheableTask
public abstract class RockerReachabilityMetadataGenerator extends DefaultTask {
    private static final String PLAIN_TEXT_CLASS_SUFFIX = "$PlainText.class";

    /**
     * @return The directory holding the compiled template classes.
     */
    @InputDirectory
    @PathSensitive(PathSensitivity.RELATIVE)
    public abstract DirectoryProperty getClassesDirectory();

    /**
     * @return The group id of the module the metadata is written for.
     */
    @Input
    public abstract Property<String> getGroupId();

    /**
     * @return The artifact id of the module the metadata is written for.
     */
    @Input
    public abstract Property<String> getArtifactId();

    /**
     * @return The resources directory the metadata is written into.
     */
    @OutputDirectory
    public abstract DirectoryProperty getOutputDirectory();

    @TaskAction
    public void generate() throws IOException {
        Path classesDirectory = getClassesDirectory().get().getAsFile().toPath();
        List<String> plainTextClassFiles = plainTextClassFiles(classesDirectory);
        Path metadata = getOutputDirectory().get().getAsFile().toPath()
            .resolve("META-INF/native-image")
            .resolve(getGroupId().get())
            .resolve(getArtifactId().get())
            .resolve("reachability-metadata.json");
        Files.createDirectories(metadata.getParent());
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(metadata, StandardCharsets.UTF_8))) {
            writer.println("{");
            writer.println("  \"resources\": [");
            for (int i = 0; i < plainTextClassFiles.size(); i++) {
                writer.println("    {");
                writer.println("      \"glob\": \"" + plainTextClassFiles.get(i) + "\"");
                writer.println(i == plainTextClassFiles.size() - 1 ? "    }" : "    },");
            }
            writer.println("  ],");
            writer.println("  \"reflection\": [");
            for (int i = 0; i < plainTextClassFiles.size(); i++) {
                writer.println("    {");
                writer.println("      \"type\": \"" + className(plainTextClassFiles.get(i)) + "\",");
                writer.println("      \"allDeclaredFields\": true");
                writer.println(i == plainTextClassFiles.size() - 1 ? "    }" : "    },");
            }
            writer.println("  ]");
            writer.println("}");
        }
    }

    private static List<String> plainTextClassFiles(Path classesDirectory) throws IOException {
        List<String> classFiles;
        try (Stream<Path> files = Files.walk(classesDirectory)) {
            classFiles = files
                .filter(path -> path.getFileName().toString().endsWith(PLAIN_TEXT_CLASS_SUFFIX))
                .map(path -> classesDirectory.relativize(path).toString().replace(File.separatorChar, '/'))
                .sorted(Comparator.naturalOrder())
                .toList();
        }
        if (classFiles.isEmpty()) {
            throw new IOException("No Rocker plain text classes found in " + classesDirectory);
        }
        return classFiles;
    }

    private static String className(String classFile) {
        return classFile.substring(0, classFile.length() - ".class".length()).replace('/', '.');
    }
}
