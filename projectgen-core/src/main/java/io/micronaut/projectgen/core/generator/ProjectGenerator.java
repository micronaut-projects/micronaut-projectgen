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
import io.micronaut.projectgen.core.io.ConsoleOutput;
import io.micronaut.projectgen.core.io.OutputHandler;
import io.micronaut.projectgen.core.options.OperatingSystem;
import io.micronaut.projectgen.core.options.Options;

import java.util.List;

/**
 * Project Generator API.
 */
@DefaultImplementation(DefaultProjectGenerator.class)
public interface ProjectGenerator {

    void generate(String applicationType,
                  Project project,
                  Options options,
                  @Nullable OperatingSystem operatingSystem,
                  List<String> selectedFeatures,
                  OutputHandler outputHandler,
                  ConsoleOutput consoleOutput) throws Exception;

    void generate(
            String applicationType,
            Project project,
            OutputHandler outputHandler,
            GeneratorContext generatorContext) throws Exception;

    GeneratorContext createGeneratorContext(
            String applicationType,
            Project project,
            Options options,
            @Nullable OperatingSystem operatingSystem,
            List<String> selectedFeatures,
            ConsoleOutput consoleOutput);

    default void generate(@NonNull Options options, @NonNull OutputHandler outputHandler) throws Exception {
        generate(options, outputHandler, ConsoleOutput.NOOP);
    }

    void generate(@NonNull Options options, @NonNull OutputHandler outputHandler, ConsoleOutput consoleOutput) throws Exception;
}
