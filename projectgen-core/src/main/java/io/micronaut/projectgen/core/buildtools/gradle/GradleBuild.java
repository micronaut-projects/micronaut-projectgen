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
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.projectgen.core.buildtools.dependencies.Coordinate;
import io.micronaut.projectgen.core.buildtools.dependencies.DependencyCoordinate;
import io.micronaut.projectgen.core.buildtools.dependencies.Substitution;
import io.micronaut.projectgen.core.rocker.RockerWritable;
import io.micronaut.projectgen.core.template.Writable;
import io.micronaut.projectgen.core.template.WritableUtils;
import io.micronaut.sourcegen.annotations.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import io.micronaut.projectgen.core.template.settingsPluginManagement;
import io.micronaut.projectgen.core.template.substitutions;

/**
 * Representation of a Gradle Build.
 */
@Builder
public record GradleBuild(Coordinate coordinate,
                          GradleDsl dsl,
                          List<GradleDependency> dependencies,
                          List<GradlePlugin> plugins,
                          List<GradleRepository> repositories) {
    private static final Logger LOG = LoggerFactory.getLogger(GradleBuild.class);

    /**
     *
     * @return Plugins
     */
    @NonNull
    public List<GradlePlugin> getPlugins() {
        return plugins.stream().filter(gradlePlugin -> gradlePlugin.getGradleFile() == GradleFile.BUILD).toList();
    }

    /**
     *
     * @return Settings Imports
     */
    @NonNull
    public List<String> getSettingsImports() {
        return plugins.stream().filter(gradlePlugin -> gradlePlugin.getGradleFile() == GradleFile.SETTINGS).map(GradlePlugin::getSettingsImports).flatMap(Collection::stream).toList();
    }

    /**
     *
     * @return Settings Plugins
     */
    @NonNull
    public List<GradlePlugin> getSettingsPlugins() {
        return plugins.stream().filter(gradlePlugin -> gradlePlugin.getGradleFile() == GradleFile.SETTINGS).collect(Collectors.toList());
    }

    /**
     *
     * @return substitutions rendered
     */
    @NonNull
    public String renderSubstitutions() {
        Set<Substitution> uniqueSubstitutions = new HashSet<>();
        dependencies
            .stream()
            .map(DependencyCoordinate::getSubstitutions)
            .filter(Objects::nonNull)
            .forEach(uniqueSubstitutions::addAll);
        return CollectionUtils.isEmpty(uniqueSubstitutions) ? "" :
            renderWritableExtensions(Stream.of(
                new RockerWritable(substitutions.template(uniqueSubstitutions))));
    }

    /**
     *
     * @return extensions rendered
     */
    @NonNull
    public String renderExtensions() {
        List<Writable> extensions = plugins.stream()
            .map(GradlePlugin::getExtension)
            .filter(Objects::nonNull)
            .toList();
        return renderWritableExtensions(extensions.stream());
    }

    /**
     *
     * @return settings extensions rendered
     */
    @NonNull
    public String renderSettingsExtensions() {
        return renderWritableExtensions(getSettingsExtensionsStream());
    }

    public List<Writable> getSettingsExtensions() {
        return getSettingsExtensionsStream()
            .toList();
    }

    private Stream<Writable> getSettingsExtensionsStream() {
        return plugins.stream()
            .map(GradlePlugin::getSettingsExtension)
            .filter(Objects::nonNull);
    }

    /**
     *
     * @return repositories rendered
     */
    @NonNull
    public String renderRepositories() {
        String result = WritableUtils.renderWritableList(repositories.stream()
            .map(Writable.class::cast)
            .toList(), 4);

        if (result.endsWith("\n")) {
            return result.substring(0, result.lastIndexOf("\n"));
        }
        return result;
    }

    /**
     *
     * @return settings plugins management rendered
     */
    @NonNull
    public String renderSettingsPluginsManagement() {
        List<GradleRepository> repos = getPluginsManagementRepositories();
        if (CollectionUtils.isEmpty(repos)) {
            return "";
        }
        return WritableUtils.renderWritable(new RockerWritable(settingsPluginManagement.template(repos)), 0);
    }

    public List<GradleRepository> getPluginsManagementRepositories() {
        return plugins.stream()
            .flatMap(plugin -> plugin.getPluginsManagementRepositories().stream())
            .distinct()
            .toList();
    }

    /**
     *
     * @return writable extensions rendered
     */
    @NonNull
    private String renderWritableExtensions(Stream<Writable> extensions) {
        StringBuilder result = new StringBuilder();
        extensions
            .filter(Objects::nonNull)
            .forEach(writable -> {
                try {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    writable.write(outputStream);
                    result.append(outputStream.toString(StandardCharsets.UTF_8))
                        .append(System.lineSeparator());
                } catch (IOException e) {
                    if (LOG.isErrorEnabled()) {
                        LOG.error("IO Exception rendering Gradle Plugin extension", e);
                    }
                }
            });
        return result.toString();
    }

    /**
     *
     * @return plugins imports
     */
    @NonNull
    public Set<String> getPluginsImports() {
        Set<String> imports = new HashSet<>();
        for (GradlePlugin p : plugins) {
            Set<String> pluginImports = p.getBuildImports();
            if (pluginImports != null) {
                imports.addAll(pluginImports);
            }
        }
        return imports.stream().map(it -> it + System.lineSeparator()).collect(Collectors.toSet());
    }
}
