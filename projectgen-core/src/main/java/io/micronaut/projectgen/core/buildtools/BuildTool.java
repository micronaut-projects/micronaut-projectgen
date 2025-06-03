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
package io.micronaut.projectgen.core.buildtools;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.projectgen.core.generator.Project;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/**
 * Build tool.
 */
public enum BuildTool {
    GRADLE("build/libs", "-*-all.jar", "Gradle (Groovy)"),
    MAVEN("target", "-*.jar", "Maven");

    public static final BuildTool DEFAULT_OPTION = BuildTool.GRADLE;

    private final String jarDirectory;
    private final String shadeJarPattern;
    private final String title;

    BuildTool(String jarDirectory, String shadeJarPattern, String title) {
        this.jarDirectory = jarDirectory;
        this.shadeJarPattern = shadeJarPattern;
        this.title = title;
    }

    public String getJarDirectory() {
        return jarDirectory;
    }

    public String getShadeJarDirectoryPattern(Project project) {
        Objects.requireNonNull(project, "Project should not be null");
        return getJarDirectory() + '/' + project.getName() + shadeJarPattern;
    }

    @Override
    public String toString() {
        return getName();
    }

    @NonNull
    public String getName() {
        return name().toLowerCase(Locale.ENGLISH);
    }

    @NonNull
    public static Optional<BuildTool> of(@NonNull String str) {
        for (BuildTool bt : values()) {
            if (bt.name().equalsIgnoreCase(str)) {
                return Optional.of(bt);
            }
        }
        return Optional.empty();
    }

    public String getTitle() {
        return title;
    }
}
