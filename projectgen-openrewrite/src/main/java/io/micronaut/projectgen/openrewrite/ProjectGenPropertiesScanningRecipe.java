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
package io.micronaut.projectgen.openrewrite;

import io.micronaut.core.annotation.Internal;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.gradle.GradleDsl;
import io.micronaut.projectgen.core.buildtools.maven.Packaging;
import io.micronaut.projectgen.core.options.ConfigurationFormat;
import io.micronaut.projectgen.core.options.GenericOptionsBuilder;
import io.micronaut.projectgen.core.options.JdkVersion;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.options.OperatingSystem;
import io.micronaut.projectgen.core.options.TestFramework;
import org.jspecify.annotations.Nullable;
import org.openrewrite.ExecutionContext;
import org.openrewrite.ScanningRecipe;
import org.openrewrite.SourceFile;
import org.openrewrite.TreeVisitor;
import org.openrewrite.properties.PropertiesIsoVisitor;
import org.openrewrite.properties.tree.Properties;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static io.micronaut.projectgen.openrewrite.PropertiesUtils.parseValue;

/**
 * abstract class for a scanning recipe which reads a `projectgen.properties` file and populates {@link GenericOptionsBuilder}.
 */
@Internal
public abstract class ProjectGenPropertiesScanningRecipe extends ScanningRecipe<GenericOptionsBuilder> {
    @Nullable
    protected Path projectDir;

    @Override
    public GenericOptionsBuilder getInitialValue(ExecutionContext ctx) {
        return GenericOptionsBuilder.builder();
    }

    @Override
    public TreeVisitor<?, ExecutionContext> getScanner(GenericOptionsBuilder acc) {
        return new PropertiesIsoVisitor<ExecutionContext>() {
            @Override
            public Properties.File visitFile(Properties.File file, ExecutionContext ctx) {
                SourceFile sourceFile = getCursor().firstEnclosing(SourceFile.class);
                if (sourceFile != null) {
                    Path sourcePath = sourceFile.getSourcePath();
                    if ("projectgen.properties".equals(sourcePath.getFileName().toString())) {
                        projectDir = sourcePath.getParent() == null
                            ? Paths.get("")
                            : sourcePath.getParent();
                    }
                }
                return super.visitFile(file, ctx);
            }

            @Override
            public Properties.Entry visitEntry(Properties.Entry entry, ExecutionContext executionContext) {
                parseValue(entry, "name").ifPresent(acc::name);
                parseValue(entry, "operatingSystem").map(OperatingSystem::valueOf).ifPresent(acc::operatingSystem);
                parseValue(entry, "template").ifPresent(acc::template);
                parseValue(entry, "language").flatMap(Language::of).ifPresent(acc::language);
                for (int i = 0; i < 10; i++) {
                    String keyName = "buildTools[" + i + "]";
                    if (entry.getKey().equals(keyName)) {
                        acc.buildTools(buildTools(entry, keyName));
                    }
                }
                parseValue(entry, "configurationFormat").map(ConfigurationFormat::valueOf).ifPresent(acc::configurationFormat);
                parseValue(entry, "gradleDsl").map(GradleDsl::valueOf).ifPresent(acc::gradleDsl);
                parseValue(entry, "group").ifPresent(acc::group);
                parseValue(entry, "artifact").ifPresent(acc::artifact);
                parseValue(entry, "java").map(JdkVersion::valueOf).ifPresent(acc::java);
                parseValue(entry, "packageName").ifPresent(acc::packageName);
                parseValue(entry, "version").ifPresent(acc::version);
                parseValue(entry, "packaging").map(Packaging::valueOf).ifPresent(acc::packaging);
                parseValue(entry, "testFramework").flatMap(TestFramework::of).ifPresent(acc::testFramework);
                for (int i = 0; i < 100; i++) {
                    String keyName = "features[" + i + "]";
                    if (entry.getKey().equals(keyName)) {
                        acc.features(features(entry, keyName));
                    }
                }
                return entry;
            }

            private List<BuildTool> buildTools(Properties.Entry entry, String keyName) {
                List<BuildTool> buildTools = new ArrayList<>(acc.build().buildTools());
                parseValue(entry, keyName).flatMap(BuildTool::of).ifPresent(buildTools::add);
                return buildTools;
            }

            private List<String> features(Properties.Entry entry, String keyName) {
                List<String> features = new ArrayList<>(acc.build().features());
                parseValue(entry, keyName).ifPresent(features::add);
                return features;
            }
        };
    }
}
