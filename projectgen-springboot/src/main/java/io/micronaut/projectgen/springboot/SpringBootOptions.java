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
package io.micronaut.projectgen.springboot;

import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.options.*;
import io.micronaut.sourcegen.annotations.Builder;

import java.util.*;

/**
 * Options for generating a project.
 * @param group
 * @param version
 * @param operatingSystem Operating System
 * @param name name
 * @param packageName packageName
 * @param features Features
 * @param framework Framework
 * @param language Language
 * @param testFramework Test framework
 * @param buildTools Build Tools
 * @param javaVersion Java Version
 */
@Builder
public record SpringBootOptions(
    String group,
    String version,
    OperatingSystem operatingSystem,
    String name,
    String packageName,
    List<String> features,
    String framework,
    Language language,
    TestFramework testFramework,
    List<BuildTool> buildTools,
    JdkVersion javaVersion) implements Options {
}
