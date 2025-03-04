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
package io.micronaut.projectgen.core.generator;

import io.micronaut.context.annotation.DefaultImplementation;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.projectgen.core.feature.AvailableFeatures;
import io.micronaut.projectgen.core.io.ConsoleOutput;
import io.micronaut.projectgen.core.io.OutputHandler;
import io.micronaut.projectgen.core.options.Options;
import jakarta.inject.Provider;

/**
 * Project Generator API.
 */
@DefaultImplementation(DefaultProjectGenerator.class)
public interface ProjectGenerator {

    default void generate(@NonNull Options options, @NonNull OutputHandler outputHandler) throws Exception {
        generate(options, outputHandler, ConsoleOutput.NOOP, null);
    }

    default void generate(@NonNull Options options, @NonNull OutputHandler outputHandler, @Nullable Provider<AvailableFeatures> availableFeaturesProvider) throws Exception {
        generate(options, outputHandler, ConsoleOutput.NOOP, availableFeaturesProvider);
    }

    default void generate(@NonNull Options options, @NonNull OutputHandler outputHandler, ConsoleOutput consoleOutput) throws Exception {
        generate(options, outputHandler, consoleOutput, null);
    }

    void generate(@NonNull Options options, @NonNull OutputHandler outputHandler, ConsoleOutput consoleOutput, @Nullable Provider<AvailableFeatures> availableFeaturesProvider) throws Exception;
}
