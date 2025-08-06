/*
 * Copyright 2017-2022 original authors
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
package io.micronaut.projectgen.micronaut.features.opentelemetry;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;

/**
 * Utility class for creating OpenTelemetry dependencies in Micronaut applications.
 * Provides helper methods for building OpenTelemetry and OpenTelemetry instrumentation dependencies.
 */
public final class OpenTelemetryDependencyUtils {
    public static final String GROUP_ID_OPENTELEMETRY = "io.opentelemetry";
    public static final String GROUP_ID_OPENTELEMETRY_INSTRUMENTATION = "io.opentelemetry.instrumentation";

    private OpenTelemetryDependencyUtils() {

    }

    /**
     * Creates a builder for OpenTelemetry dependencies.
     *
     * @return a Dependency.Builder for OpenTelemetry dependencies
     */
    @NonNull
    public static Dependency.Builder openTelemetryDependency() {
        return dependency(GROUP_ID_OPENTELEMETRY);
    }

    /**
     * Creates a builder for OpenTelemetry instrumentation dependencies.
     *
     * @return a Dependency.Builder for OpenTelemetry instrumentation dependencies
     */
    @NonNull
    public static Dependency.Builder openTelemetryInstrumentationDependency() {
        return dependency(GROUP_ID_OPENTELEMETRY_INSTRUMENTATION);
    }

    /**
     * Creates a dependency builder with the specified group ID.
     *
     * @param groupId the group ID for the dependency
     * @return a Dependency.Builder with the specified group ID
     */
    @NonNull
    private static Dependency.Builder dependency(@NonNull String groupId) {
        return Dependency.builder()
            .groupId(groupId);
    }
}
