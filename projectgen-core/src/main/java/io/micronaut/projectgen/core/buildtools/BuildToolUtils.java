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
import io.micronaut.core.annotation.Nullable;
import io.micronaut.projectgen.core.buildtools.gradle.GradleDsl;

public final class BuildToolUtils {

    private static final String DOT_GRADLE_KTS = ".gradle.kts";
    private static final String DOT_GRADLE = ".gradle";
    private static final String DEFAULT_GRADLE_FILE_EXTENSION = DOT_GRADLE;
    public static final String DOT_XML = ".xml";

    private BuildToolUtils() {
    }

    @NonNull
    public static String buildFileNameWithoutExtension(@NonNull BuildTool buildTool) {
        return switch (buildTool) {
            case GRADLE -> "build";
            case MAVEN -> "pom";
        };
    }

    @NonNull
    public static String settingsFileNameWithoutExtension(@NonNull BuildTool buildTool) {
        return switch (buildTool) {
            case GRADLE -> "settings";
            case MAVEN ->  throw new IllegalStateException("no settings file for maven builds");
        };
    }

    @NonNull
    public static String settingsFileName(@NonNull BuildTool buildTool, @Nullable GradleDsl dsl) {
        return settingsFileNameWithoutExtension(buildTool) + fileExtension(buildTool, dsl);
    }

    @NonNull
    public static String buildFileName(@NonNull BuildTool buildTool, @Nullable GradleDsl dsl) {
        return buildFileNameWithoutExtension(buildTool) + fileExtension(buildTool, dsl);
    }

    @NonNull
    public static String fileExtension(@NonNull BuildTool buildTool,
                                          @Nullable GradleDsl dsl) {
        return switch (buildTool) {
            case GRADLE -> {
                if (dsl != null) {
                    switch (dsl) {
                        case KOTLIN:
                            yield DOT_GRADLE_KTS;

                        case GROOVY:
                            yield DOT_GRADLE;
                        default:
                            throw new IllegalStateException("Unexpected value: " + dsl);
                    }
                }
                yield DEFAULT_GRADLE_FILE_EXTENSION;
            }
            case MAVEN -> DOT_XML;
        };
    }

}
