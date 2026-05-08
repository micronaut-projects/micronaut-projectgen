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
package io.micronaut.projectgen.core.buildtools.gradle;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.order.OrderUtil;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.dependencies.Coordinate;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.buildtools.dependencies.DependencyContext;
import io.micronaut.projectgen.core.buildtools.dependencies.DependencyCoordinate;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.options.Options;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import static io.micronaut.core.util.CollectionUtils.isNotEmpty;

/**
 * Gradle Dependency.
 */
public class GradleDependency extends DependencyCoordinate {

    public static final Comparator<GradleDependency> COMPARATOR = (o1, o2) -> {
        int comparison = OrderUtil.COMPARATOR.compare(o1, o2);
        if (comparison != 0) {
            return comparison;
        }
        comparison = Integer.compare(o1.getConfiguration().getOrder(), o2.getConfiguration().getOrder());
        if (comparison != 0) {
            return comparison;
        }
        return Coordinate.COMPARATOR.compare(o1, o2);
    };

    private final Boolean isKotlinDSL;

    @NonNull
    private final GradleConfiguration gradleConfiguration;

    private final boolean useVersionCatalogue;
    private final String project;
    private final String comment;

    public GradleDependency(@NonNull Dependency dependency,
                            @NonNull Options options,
                            @NonNull GeneratorContext generatorContext,
                            boolean useVersionCatalogue,
                            @Nullable String project,
                            @Nullable String comment) {
        super(dependency);
        gradleConfiguration = GradleConfiguration.of(
            dependency.getScope(),
            options.language(),
            options.testFramework(),
            generatorContext
        ).orElseThrow(() ->
            new IllegalArgumentException("Cannot map the dependency scope: [%s] to a Gradle specific scope".formatted(dependency.getScope())));
        isKotlinDSL = generatorContext.getOptions().buildTools().stream()
            .anyMatch(bt -> bt == BuildTool.GRADLE && options.gradleDsl() != null && options.gradleDsl() == GradleDsl.KOTLIN);
        this.useVersionCatalogue = useVersionCatalogue;
        this.project = project;
        this.comment = comment;
    }

    /**
     *
     * @return Gradle Configuration
     */
    @NonNull
    public GradleConfiguration getConfiguration() {
        return gradleConfiguration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        GradleDependency that = (GradleDependency) o;

        return Objects.equals(gradleConfiguration, that.gradleConfiguration);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + gradleConfiguration.hashCode();
        return result;
    }

    /**
     *
     * @return snippet representation
     */
    @NonNull
    public String toSnippet() {
        String snippet = "";
        if (StringUtils.isNotEmpty(comment)) {
            snippet += "/* " + comment + " */\n    ";
        }
        snippet += gradleConfiguration.getConfigurationName();
        if (isPom()) {
            String platformPrefix = " ";
            if (isKotlinDSL) {
                platformPrefix = "(";
            }
            snippet += platformPrefix + "platform";
        }
        snippet += "(";
        if (StringUtils.isNotEmpty(project)) {
            snippet += "project(\":" + project + "\")";
        } else {
            snippet += useVersionCatalogue ? versionCatalog().orElseGet(this::mavenCoordinate) : mavenCoordinate();
        }
        snippet += ")";
        if (isPom() && isKotlinDSL) {
            snippet += ")";
        }

        if (isNotEmpty(getExclusions())) {
            snippet += " {\n";
            final String mapAccessor = isKotlinDSL ? " = " : ": ";
            final StringBuilder exclusionBuilder = new StringBuilder();
            for (DependencyCoordinate exclusion : getExclusions()) {
                exclusionBuilder
                    .append("      exclude(group").append(mapAccessor).append("\"").append(exclusion.getGroupId())
                    .append("\", module").append(mapAccessor).append("\"").append(exclusion.getArtifactId()).append("\")\n");
            }
            snippet += exclusionBuilder.toString();
            snippet += "    }";
        }
        return snippet;
    }

    /**
     *
     * @return Maven Coordinate surrounded by double quotes
     */
    @NonNull
    public String mavenCoordinate() {
        List<String> parts = new ArrayList<>();
        if (getGroupId() != null) {
            parts.add(getGroupId());
        }
        if (getArtifactId() != null) {
            parts.add(getArtifactId());
        }
        if (getVersion() != null) {
            parts.add(getVersion());
        }
        if (CollectionUtils.isEmpty(parts)) {
            return "";
        }
        if (parts.size() > 1) {
            return "\"" +  String.join(":", parts) + "\"";
        } else if (parts.size() == 1) {
            return parts.get(0);
        }
        return "";
    }

    /**
     *
     * @return version catalogue
     */
    @NonNull
    public Optional<String> versionCatalog() {
        if (!getGroupId().startsWith("io.micronaut")) {
            return Optional.empty();
        }
        return Optional.of("mn." + getArtifactId().replace("-", "."));
    }

    @NonNull
    public static List<GradleDependency> listOf(GeneratorContext generatorContext, DependencyContext dependencyContext, Options options, boolean useVersionCatalogue) {
        BuildTool buildTool = options.buildTools().stream()
            .filter(bt -> bt == BuildTool.GRADLE).findFirst().orElseThrow();
        return dependencyContext.removeDuplicates(dependencyContext.getDependenciesByBuildTool(BuildTool.GRADLE), options.language(), buildTool)
            .stream()
            .map(dep -> new GradleDependency(dep, options, generatorContext, useVersionCatalogue, dep.getProject(), dep.getComment()))
            .sorted(GradleDependency.COMPARATOR)
            .toList();
    }
}
