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
package io.micronaut.projectgen.core.buildtools.maven;

import io.micronaut.sourcegen.annotations.Builder;

import java.util.List;

/**
 * Configuration for the Maven Compiler Plugin
 * @param version version
 * @param configurationCombine Configuration combine attribute
 * @param incrementalCompilation incremental compilation
 * @param source source
 * @param target target
 * @param compilerArgs compiler args
 */
@Builder
public record MavenCompilerPluginConfiguration(
    String version,
    String configurationCombine,
    Boolean incrementalCompilation,
    String source,
    String target,
    List<String> compilerArgs
) {
}
