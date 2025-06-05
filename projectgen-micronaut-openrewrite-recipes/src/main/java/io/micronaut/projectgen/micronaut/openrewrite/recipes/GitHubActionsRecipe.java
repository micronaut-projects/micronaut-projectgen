/*
 * Copyright 2017-2025 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.projectgen.micronaut.openrewrite.recipes;

import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.gradle.GradleDsl;
import io.micronaut.projectgen.core.options.GenericOptionsBuilder;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.options.TestFramework;
import org.openrewrite.*;
import org.openrewrite.marker.Markers;
import org.openrewrite.text.PlainText;
import org.openrewrite.yaml.YamlIsoVisitor;
import org.openrewrite.yaml.tree.Yaml;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class GitHubActionsRecipe extends ScanningRecipe<GenericOptionsBuilder> {

    private static final String MICRONAUT_CLI_FEATURES = "features";
    private static final String MICRONAUT_CLI_APPLICATION_TYPE = "applicationType";
    private static final String MICRONAUT_CLI_BUILD_TOOL = "buildTool";
    private static final String MICRONAUT_CLI_DEFAULT_PACKAGE = "defaultPackage";
    private static final String MICRONAUT_CLI_TEST_FRAMEWORK = "testFramework";
    private static final String MICRONAUT_CLI_SOURCE_LANGUAGE = "sourceLanguage";
    //TODO ADD constants for the keys in micronaut-cli.yml
    private final String FILENAME_MICRONAUT_CLI = "micronaut-cli.yml";
    private AtomicBoolean done = new AtomicBoolean(false);

    @Override
    public String getDisplayName() {
        return "Generate a Github Actions workflow file";
    }

    @Override
    public String getDescription() {
        return "It generate a Github Actions workflow file based on the build tool defined in `micronaut-cli.yml`.";
    }
    @Override
    public GenericOptionsBuilder getInitialValue(ExecutionContext ctx) {
        return GenericOptionsBuilder.builder();
    }

    @Override
    public TreeVisitor<?, ExecutionContext> getScanner(GenericOptionsBuilder optionsBuilder) {
        return new YamlIsoVisitor<ExecutionContext>() {
            @Override
            public Yaml.Document visitDocument(Yaml.Document document, ExecutionContext ctx) {
                SourceFile sourceFile = getCursor().firstEnclosing(SourceFile.class);
                if (sourceFile != null) {
                    Path sourcePath = sourceFile.getSourcePath();
                    if (sourcePath != null && (
                        FILENAME_MICRONAUT_CLI.equals(sourcePath.getFileName().toString())
                    )) {
                        return super.visitDocument(document, ctx);
                    }
                }
                return document;
            }

            @Override
            public Yaml.Mapping.Entry visitMappingEntry(Yaml.Mapping.Entry entry, ExecutionContext ctx) {
                parseValue(entry, MICRONAUT_CLI_SOURCE_LANGUAGE).ifPresent(value -> {
                    Language.of(value).ifPresent(optionsBuilder::language);
                });
                optionsBuilder.features(parseValues(entry, MICRONAUT_CLI_FEATURES));
                parseValue(entry, MICRONAUT_CLI_APPLICATION_TYPE).ifPresent(optionsBuilder::template);

                parseValue(entry, MICRONAUT_CLI_TEST_FRAMEWORK).ifPresent(value -> {
                    TestFramework.of(value).ifPresent(optionsBuilder::testFramework);
                });
                parseValue(entry, MICRONAUT_CLI_BUILD_TOOL).ifPresent(value -> {
                     if (value.equals("maven")) {
                        optionsBuilder.buildTools(List.of(BuildTool.MAVEN));
                     } else if (value.equals("gradle")) {
                         optionsBuilder.buildTools(List.of(BuildTool.GRADLE))
                             .gradleDsl(GradleDsl.GROOVY);

                     } else if (value.equals("gradle_kotlin")) {
                         optionsBuilder.buildTools(List.of(BuildTool.GRADLE))
                             .gradleDsl(GradleDsl.KOTLIN);
                     }
                });
                parseValue(entry, MICRONAUT_CLI_DEFAULT_PACKAGE)
                    .ifPresent(optionsBuilder::packageName);
                return super.visitMappingEntry(entry, ctx);
            }
        };
    }

    @Override
    public Collection<SourceFile> generate(GenericOptionsBuilder optionsBuilder, ExecutionContext ctx) {
        Options options = optionsBuilder.build();
        Path path = Paths.get(".github/workflows/gradle.ym");
        if (!done.get()) {
            done.compareAndSet(false, true);
            return Collections.emptyList();
        }
        PlainText plainText = PlainText.builder()
                .text("""
                    name: Java CI with Maven
                    on:
                      push:
                        branches: [ main ]
                      pull_request:
                        branches: [ main ]

                    jobs:
                      build:
                        runs-on: ubuntu-latest
                        steps:
                        - uses: actions/checkout@v3
                        - name: Set up JDK 21
                          uses: actions/setup-java@v3
                          with:
                            java-version: 21
                            distribution: temurin
                            cache: maven
                       - name: Build with Maven
                         run: mvn -B verify --file pom.xml""")
                .sourcePath(path)
                .build();
        return Collections.singletonList(plainText);
    }

    private static List<String> parseValues(Yaml.Mapping.Entry entry, String keyName) {
        List<String> result = new ArrayList<>();
        if (entry.getKey() instanceof Yaml.Scalar) {
            Yaml.Scalar key = (Yaml.Scalar) entry.getKey();
            if (keyName.equals(key.getValue())) {
                Yaml.Block value = entry.getValue();
                if (value instanceof Yaml.Sequence sequence) {
                    result.addAll(sequence.getEntries().stream()
                        .map(Yaml.Sequence.Entry::getBlock)
                        .filter(block -> block instanceof Yaml.Scalar)
                        .map(block -> (Yaml.Scalar) block)
                        .map(Yaml.Scalar::getValue)
                        .toList());
                }
            }
        }
        return result;
    }

    private static Optional<String> parseValue(Yaml.Mapping.Entry entry, String keyName) {
        if (entry.getKey() instanceof Yaml.Scalar) {
            Yaml.Scalar key = (Yaml.Scalar) entry.getKey();
            if (keyName.equals(key.getValue())) {
                if (entry.getValue() instanceof Yaml.Scalar) {
                    Yaml.Scalar value = (Yaml.Scalar) entry.getValue();
                    return Optional.of(value.getValue());
                }
            }
        }
        return Optional.empty();
    }
}
