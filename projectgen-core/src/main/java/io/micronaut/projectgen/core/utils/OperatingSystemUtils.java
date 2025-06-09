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

import io.micronaut.core.annotation.Nullable;
import io.micronaut.projectgen.core.options.OperatingSystem;

import static io.micronaut.projectgen.core.options.OperatingSystem.LINUX;
import static io.micronaut.projectgen.core.options.OperatingSystem.MACOS;
import static io.micronaut.projectgen.core.options.OperatingSystem.SOLARIS;
import static io.micronaut.projectgen.core.options.OperatingSystem.WINDOWS;

public final class OperatingSystemUtils {
    private OperatingSystemUtils() {
    }

    @Nullable
    public static OperatingSystem getOperatingSystem() {
        io.micronaut.context.condition.OperatingSystem operatingSystem = io.micronaut.context.condition.OperatingSystem.getCurrent();
        if (operatingSystem.isMacOs()) {
            return MACOS;
        }
        if (operatingSystem.isLinux()) {
            return LINUX;
        }
        if (operatingSystem.isWindows()) {
            return WINDOWS;
        }
        if (operatingSystem.isSolaris()) {
            return SOLARIS;
        }
        return null;
    }
}
