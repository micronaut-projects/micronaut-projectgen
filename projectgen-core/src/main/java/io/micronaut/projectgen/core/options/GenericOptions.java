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
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.gradle.GradleDsl;
import io.micronaut.projectgen.core.buildtools.maven.Packaging;
import io.micronaut.sourcegen.annotations.Builder;

import java.util.*;

/**
 * Options Implementation.
 * @param name Project name
 * @param version Project Version
 * @param operatingSystem Operating System
 * @param template template
 * @param language language
 * @param buildTools build tools
 * @param configurationFormat configuration format
 * @param gradleDsl Gradle DSL
 * @param group group
 * @param artifact artifact
 * @param java Java version
 * @param packageName package name
 * @param packaging packaging
 * @param features Features
 * @param testFramework Test framework
 */
@Builder
public record GenericOptions(@NonNull String name,
                             @Nullable String version,
                             @Nullable OperatingSystem operatingSystem,
                             @Nullable String template,
                             @Nullable Language language,
                             @Nullable List<BuildTool> buildTools,
                             @Nullable ConfigurationFormat configurationFormat,
                             @Nullable GradleDsl gradleDsl,
                             @Nullable String group,
                             @Nullable String artifact,
                             @Nullable JdkVersion java,
                             @Nullable String packageName,
                             @Nullable Packaging packaging,
                             @Nullable List<String> features,
                             @Nullable TestFramework testFramework) implements Options {
    @Override
    public Options withoutFeatures() {
        return GenericOptionsBuilder.builder()
            .name(this.name())
            .configurationFormat(this.configurationFormat())
            .operatingSystem(this.operatingSystem())
            .template(this.template())
            .buildTools(this.buildTools())
            .gradleDsl(this.gradleDsl())
            .group(this.group())
            .artifact(this.artifact())
            .java(this.java())
            .packageName(this.packageName())
            .packaging(this.packaging())
            .testFramework(this.testFramework())
            .build();
    }

    @Override
    public List<String> features() {
        return CollectionUtils.isEmpty(this.features) ? Collections.emptyList() : this.features;
    }

    @Override
    public List<BuildTool> buildTools() {
        return CollectionUtils.isEmpty(this.buildTools) ? Collections.emptyList() : this.buildTools;
    }

    @Override
    public String name() {
        return StringUtils.isEmpty(this.name) ? artifact() : this.name;
    }
}
