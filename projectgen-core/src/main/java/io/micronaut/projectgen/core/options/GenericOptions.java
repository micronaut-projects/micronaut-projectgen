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

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.gradle.GradleDsl;
import io.micronaut.projectgen.core.buildtools.maven.Packaging;
import io.micronaut.sourcegen.annotations.Builder;

import java.util.Collections;
import java.util.List;

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
    public String template() {
        return StringUtils.isEmpty(this.template) ? "DEFAULT" : this.template;
    }

    @Override
    public Language language() {
        return this.language == null ? Language.JAVA : this.language;
    }

    @Override
    public JdkVersion java() {
        return this.java == null ? JdkVersion.JDK_25 : this.java;
    }

    @Override
    public TestFramework testFramework() {
        return this.testFramework == null ? TestFramework.DEFAULT_OPTION : this.testFramework;
    }

    @Override
    public Options withoutFeatures() {
        return new GenericOptions(
            this.name(),
            this.version(),
            this.operatingSystem(),
            this.template(),
            this.language(),
            this.buildTools(),
            this.configurationFormat(),
            this.gradleDsl(),
            this.group(),
            this.artifact(),
            this.java(),
            this.packageName(),
            this.packaging(),
            Collections.emptyList(),
            this.testFramework());
    }

    @Override
    public String packageName() {
        return StringUtils.isEmpty(this.packageName) ? "example" : this.packageName;
    }

    @Override
    public String artifact() {
        if (StringUtils.isEmpty(this.artifact)) {
            return name();
        }
        return this.artifact;
    }

    @Override
    public String group() {
        if (StringUtils.isEmpty(this.group)) {
            return StringUtils.isEmpty(packageName()) ? "example" : packageName();
        }
        return this.group;
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
