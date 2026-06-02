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

import org.jspecify.annotations.NonNull;
import io.micronaut.core.util.StringUtils;
import java.util.Locale;
import java.util.Optional;

/**
 * Build tool.
 */
public enum BuildTool {
    GRADLE("build/libs"),
    MAVEN("target");

    public static final BuildTool DEFAULT_OPTION = BuildTool.GRADLE;
    private final String jarDirectory;

    BuildTool(String jarDirectory) {
        this.jarDirectory = jarDirectory;
    }

    public String getJarDirectory() {
        return jarDirectory;
    }

    @Override
    public String toString() {
        return getName();
    }

    public @NonNull String getName() {
        return name().toLowerCase(Locale.ENGLISH);
    }

    public static @NonNull Optional<BuildTool> of(@NonNull String str) {
        for (BuildTool bt : values()) {
            if (bt.name().equalsIgnoreCase(str)) {
                return Optional.of(bt);
            }
        }
        return Optional.empty();
    }

    public String getTitle() {
        return StringUtils.capitalize(name().toLowerCase(Locale.ENGLISH));
    }
}
