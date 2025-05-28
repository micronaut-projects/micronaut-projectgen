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
package io.micronaut.projectgen.core.options;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.gradle.GradleDsl;
import io.micronaut.projectgen.core.buildtools.maven.Packaging;
import io.micronaut.projectgen.core.validation.JavaPackageName;

import java.util.*;

/**
 * Project creation options.
*/
public interface Options {
    @NonNull
    String name();

    @Nullable
    OperatingSystem operatingSystem();

    @Nullable
    String template();

    @Nullable
    Language language();

    @Nullable
    List<BuildTool> buildTools();

    @Nullable
    GradleDsl gradleDsl();

    @Nullable
    String group();

    @Nullable
    String artifact();

    @Nullable
    JdkVersion java();

    @Nullable
    @JavaPackageName
    String packageName();

    @Nullable
    String version();

    @Nullable
    Packaging packaging();

    @NonNull
    List<String> features();

    @Nullable
    TestFramework testFramework();

    default BuildTool getBuildTool() {
        List<BuildTool> tools = buildTools();
        return tools.isEmpty() ? null : tools.get(0);
    }

    Options withoutFeatures();
}
