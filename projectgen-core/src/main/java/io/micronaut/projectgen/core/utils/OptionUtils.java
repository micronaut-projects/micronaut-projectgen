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
package io.micronaut.projectgen.core.utils;

import io.micronaut.core.annotation.Internal;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.options.Options;

/**
 * Utility methods for {@link Options}.
 */
@Internal
public final class OptionUtils {
    private OptionUtils() {
    }

    public static boolean hasGradleBuildTool(Options options) {
        return options.buildTools().stream().anyMatch(BuildTool::isGradle);
    }

    public static boolean hasMavenBuildTool(Options options) {
        return options.buildTools().stream().anyMatch(bt -> bt == BuildTool.MAVEN);
    }
}
