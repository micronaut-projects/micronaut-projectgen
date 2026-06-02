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

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.projectgen.core.buildtools.BuildPlugin;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.dependencies.Coordinate;
import io.micronaut.projectgen.core.buildtools.dependencies.CoordinateResolver;
import io.micronaut.projectgen.core.buildtools.dependencies.LookupFailedException;
import io.micronaut.projectgen.core.template.StringWritable;
import io.micronaut.projectgen.core.template.Writable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Gradle Plugin.
 */
public class GradlePlugin implements BuildPlugin {
    public static final int ORDER = -10;

    private final GradleFile gradleFile;
    @Nullable
    private final String id;

    /**
     * @see <a href="https://docs.gradle.org/current/kotlin-dsl/gradle/org.gradle.plugin.use/-plugin-dependencies-spec/alias.html">alias</a>.
     */
    @Nullable
    private final String alias; // Notation coming from a version catalgoue

    @Nullable
    private final String version;
    @Nullable
    private final Boolean apply;
    @Nullable
    private final String artifactId;
    @Nullable
    private final Writable extension;
    @Nullable
    private final Writable settingsExtension;
    private final boolean requiresLookup;
    @Nullable
    private final List<GradleRepository> pluginsManagementRepositories;
    private final Set<String> buildImports;
    private final Set<String> settingsImports;
    private final int order;

    @Deprecated(since = "4.2.0", forRemoval = true)
    public GradlePlugin(@NonNull GradleFile gradleFile,
                        @Nullable String id,
                        @Nullable String version,
                        @Nullable String alias,
                        @Nullable Boolean apply,
                        @Nullable String artifactId,
                        @Nullable Writable extension,
                        @Nullable Writable settingsExtension,
                        @Nullable List<GradleRepository> pluginsManagementRepositories,
                        boolean requiresLookup,
                        int order,
                        Set<String> buildImports) {
        this(
            gradleFile,
            id,
            version,
            alias,
            apply,
            artifactId,
            extension,
            settingsExtension,
            pluginsManagementRepositories,
            requiresLookup,
            order,
            buildImports,
            Collections.emptySet()
        );
    }

    /**
     *
     * @param gradleFile Gradle File
     * @param id Id
     * @param version version
     * @param alias alias
     * @param apply apply
     * @param artifactId ArtifactID
     * @param extension extension
     * @param settingsExtension settings extensions
     * @param pluginsManagementRepositories plugin management repositories
     * @param requiresLookup requires lookup
     * @param order order
     * @param buildImports build imports
     * @param settingsImports settings imports
     */
    @SuppressWarnings("ParameterNumber")
    public GradlePlugin(@NonNull GradleFile gradleFile,
                        @Nullable String id,
                        @Nullable String version,
                        @Nullable String alias,
                        @Nullable Boolean apply,
                        @Nullable String artifactId,
                        @Nullable Writable extension,
                        @Nullable Writable settingsExtension,
                        @Nullable List<GradleRepository> pluginsManagementRepositories,
                        boolean requiresLookup,
                        int order,
                        Set<String> buildImports,
                        Set<String> settingsImports) {
        this.gradleFile = gradleFile;
        this.id = id;
        this.version = version;
        this.alias = alias;
        this.apply = apply;
        this.artifactId = artifactId;
        this.extension = extension;
        this.settingsExtension = settingsExtension;
        this.pluginsManagementRepositories = pluginsManagementRepositories;
        this.requiresLookup = requiresLookup;
        this.order = order;
        this.buildImports = buildImports;
        this.settingsImports = settingsImports;
    }

    /**
     *
     * @return apply
     */
    @Nullable
    public Boolean getApply() {
        return apply;
    }

    /**
     *
     * @param id Plugin ID
     * @param lookupArtifactId Plugin Artifact ID
     * @return Gradle Plugin
     */
    public static GradlePlugin of(String id, String lookupArtifactId) {
        return GradlePlugin.builder()
            .id(id)
            .lookupArtifactId(lookupArtifactId)
            .order(ORDER)
            .build();
    }

    /**
     *
     * @return Build Imports.
     */
    @Nullable
    public Set<String> getBuildImports() {
        return buildImports;
    }

    /**
     *
     * @return Settings imports.
     */
    @Nullable
    public Set<String> getSettingsImports() {
        return settingsImports;
    }

    /**
     *
     * @return Gradle file
     */
    public @NonNull GradleFile getGradleFile() {
        return gradleFile;
    }

    /**
     *
     * @return plugin id
     */
    @Nullable
    public String getId() {
        return id;
    }

    /**
     *
     * @return Alias
     */
    @Nullable
    public String getAlias() {
        return alias;
    }

    /**
     *
     * @return Plugin version
     */
    @Nullable
    public String getVersion() {
        return version;
    }

    @Override
    public @NonNull BuildTool getBuildTool() {
        return BuildTool.GRADLE;
    }

    @Override
    @Nullable
    public Writable getExtension() {
        return extension;
    }

    /**
     *
     * @return Settings extensions
     */
    @Nullable
    public Writable getSettingsExtension() {
        return this.settingsExtension;
    }

    /**
     *
     * @return Plugins management repositories
     */
    public @NonNull List<GradleRepository> getPluginsManagementRepositories() {
        return CollectionUtils.isEmpty(pluginsManagementRepositories) ?
            Collections.emptyList() : pluginsManagementRepositories;
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    @Override
    public boolean requiresLookup() {
        return requiresLookup;
    }

    @Override
    public BuildPlugin resolved(CoordinateResolver coordinateResolver) {
        String lookupArtifactId = Objects.requireNonNull(artifactId, "The artifact id must be set");
        Coordinate coordinate = coordinateResolver.resolve(lookupArtifactId)
            .orElseThrow(() -> new LookupFailedException(lookupArtifactId));
        return new GradlePlugin(gradleFile,
            id,
            coordinate.getVersion(),
            alias,
            apply,
            null,
            extension,
            settingsExtension,
            pluginsManagementRepositories,
            false,
            order,
            buildImports,
            settingsImports);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GradlePlugin that = (GradlePlugin) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     *
     * @return a Builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder.
     */
    public static final class Builder {

        private GradleFile gradleFile = GradleFile.BUILD;
        @Nullable
        private String id;
        @Nullable
        private String artifactId;
        @Nullable
        private Boolean apply;
        @Nullable
        private String version;
        @Nullable
        private String alias;
        @Nullable
        private Writable extension;
        @Nullable
        private Writable settingsExtension;
        @Nullable
        private List<GradleRepository> pluginsManagementRepositories;
        private boolean requiresLookup;
        private int order;
        private Set<String> buildImports = new HashSet<>();
        private Set<String> settingsImports = new HashSet<>();

        private Builder() { }

        public GradlePlugin.@NonNull Builder gradleFile(@NonNull GradleFile file) {
            this.gradleFile = file;
            return this;
        }

        public GradlePlugin.@NonNull Builder id(@NonNull String id) {
            this.id = id;
            return this;
        }

        public GradlePlugin.@NonNull Builder alias(@NonNull String alias) {
            this.alias = alias;
            return this;
        }

        public GradlePlugin.@NonNull Builder buildImports(String... imports) {
            this.buildImports.addAll(Arrays.asList(imports));
            return this;
        }

        public GradlePlugin.@NonNull Builder settingsImports(String... imports) {
            this.settingsImports.addAll(Arrays.asList(imports));
            return this;
        }

        public GradlePlugin.@NonNull Builder lookupArtifactId(@NonNull String artifactId) {
            this.artifactId = artifactId;
            this.requiresLookup = true;
            return this;
        }

        public @NonNull Optional<String> getArtifiactId() {
            return Optional.ofNullable(artifactId);
        }

        public GradlePlugin.@NonNull Builder version(@Nullable String version) {
            this.version = version;
            return this;
        }

        public GradlePlugin.@NonNull Builder extension(@Nullable Writable extension) {
            this.extension = extension;
            return this;
        }

        public GradlePlugin.@NonNull Builder extension(@Nullable String extension) {
            this.extension = extension == null ? null : new StringWritable(extension);
            return this;
        }

        public GradlePlugin.@NonNull Builder settingsExtension(@Nullable Writable settingsExtension) {
            this.settingsExtension = settingsExtension;
            return this;
        }

        public GradlePlugin.@NonNull Builder order(int order) {
            this.order = order;
            return this;
        }

        public GradlePlugin.@NonNull Builder pluginsManagementRepository(GradleRepository repo) {
            if (this.pluginsManagementRepositories == null) {
                this.pluginsManagementRepositories = new ArrayList<>();
            }
            this.pluginsManagementRepositories.add(repo);
            return this;
        }

        public GradlePlugin.Builder apply(boolean apply) {
            this.apply = apply;
            return this;
        }

        public GradlePlugin build() {
            return new GradlePlugin(gradleFile, id, version, alias, apply, artifactId, extension, settingsExtension, pluginsManagementRepositories, requiresLookup, order, buildImports, settingsImports);
        }
    }
}
