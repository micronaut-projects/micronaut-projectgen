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

import io.micronaut.projectgen.core.options.GenericOptionsBuilder;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.options.Options;
import org.openrewrite.*;
import org.openrewrite.yaml.YamlIsoVisitor;
import org.openrewrite.yaml.tree.Yaml;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;

public class GenerateConfFileRecipe extends ScanningRecipe<GenericOptionsBuilder> {

    private static final String MICRONAUT_CLI_SOURCE_LANGUAGE = "sourceLanguage";
    //TODO ADD constants for the keys in micronaut-cli.yml
    private final String FILENAME_MICRONAUT_CLI = "micronaut-cli.yml";

    @Override
    public String getDisplayName() {
        return "Generate configuration file";
    }

    @Override
    public String getDescription() {
        return "Reads micronaut-cli.yml, extracts 'sourceLanguage', and writes 'Hello {value}' to projectgen.conf.";
    }
    @Override
    public GenericOptionsBuilder getInitialValue(ExecutionContext ctx) {
        return GenericOptionsBuilder.builder();
    }

    //get the yaml file
    @Override
    public TreeVisitor<?, ExecutionContext> getScanner(GenericOptionsBuilder optionsBuilder) {
        return new YamlIsoVisitor<ExecutionContext>() {
            @Override
            public Yaml.Document visitDocument(Yaml.Document document, ExecutionContext ctx) {
                SourceFile sourceFile = getCursor().firstEnclosing(SourceFile.class);
                if (sourceFile != null) {
                    Path sourcePath = sourceFile.getSourcePath();
                    if (sourcePath != null && (
                        FILENAME_MICRONAUT_CLI.equals(sourcePath.getFileName().toString()) ||
                            "file.yaml".equals(sourcePath.getFileName().toString()) // TODO it seems test names the yaml file file.yml
                    )) {
                        return super.visitDocument(document, ctx);
                    }
                }
                return document;
            }

            //extract the value from sourceLanguage
            @Override
            public Yaml.Mapping.Entry visitMappingEntry(Yaml.Mapping.Entry entry, ExecutionContext ctx) {
                if (entry.getKey() instanceof Yaml.Scalar) {
                    Yaml.Scalar key = (Yaml.Scalar) entry.getKey();
                    if (MICRONAUT_CLI_SOURCE_LANGUAGE.equals(key.getValue())) {
                        if (entry.getValue() instanceof Yaml.Scalar) {
                            Yaml.Scalar value = (Yaml.Scalar) entry.getValue();
                            Language.of(value.getValue()).ifPresent(optionsBuilder::language);
                        }
                    }
                    //TODO add if statement for test framework
                    //TODO add if statement for type -> Options::template
                    //TODO add if statement for defaultPackage -> packageName
                    //TODO add if statement for features -> features
                    //TODO add if statement for buildTool -> buildTool and dsl
                }
                return super.visitMappingEntry(entry, ctx);
            }
        };
    }

    @Override
    public Collection<SourceFile> generate(GenericOptionsBuilder optionsBuilder, ExecutionContext ctx) {
        Options options = optionsBuilder.build();
//        if (acc.sourceLanguage == null) {
//            return Collections.emptyList();
//        }
//
//        // Generate projectgen.conf with Hello {sourceLanguage value}
//        String content = "Hello " + acc.sourceLanguage;
//
//        PlainText plainText = PlainText.builder()
//                .text(content)
//                .sourcePath(Paths.get("projectgen.conf"))
//                .markers(Markers.EMPTY)
//                .build();
//
//        return Collections.singletonList(plainText);
        return Collections.emptyList();
    }

}
