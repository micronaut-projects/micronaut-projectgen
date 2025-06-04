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
package io.micronaut.projectgen.core.buildtools.maven;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.projectgen.core.buildtools.BuildPlugin;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.dependencies.CoordinateResolver;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.template.StringWritable;
import io.micronaut.projectgen.core.template.Writable;
import java.util.Objects;

/**
 * Maven Plugin.
 */
public class MavenPlugin implements BuildPlugin {
    private final String groupId;
    private final String artifactId;
    private final String version;
    private final Writable extension;
    private final int order;

    public MavenPlugin(String groupId,
                       String artifactId,
                       String version,
                       Writable extension, int order) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.extension = extension;
        this.order = order;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof MavenPlugin that)) return false;

        return Objects.equals(groupId, that.groupId) && Objects.equals(artifactId, that.artifactId) && Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(groupId);
        result = 31 * result + Objects.hashCode(artifactId);
        result = 31 * result + Objects.hashCode(version);
        return result;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public boolean requiresLookup() {
        return false;
    }

    @Override
    public MavenPlugin resolved(CoordinateResolver coordinateResolver) {
        throw new UnsupportedOperationException();
    }

    public static MavenPlugin.Builder builder() {
        return new Builder();
    }

    @NonNull
    @Override
    public BuildTool getBuildTool() {
        return BuildTool.MAVEN;
    }

    @Override
    @Nullable
    public Writable getExtension() {
        return extension;
    }

    @Nullable
    public String getGroupId() {
        return groupId;
    }

    @Nullable
    public String getVersion() {
        return version;
    }

    /**
     *
     * @return artifact ID
     */
    @Nullable
    public String getArtifactId() {
        return artifactId;
    }

    /**
     * Builder.
     */
    public static final class Builder {

        private String artifactId;
        private String groupId;
        private String version;
        private Writable extension;
        private int order;

        private Builder() {
        }

        @NonNull
        public MavenPlugin.Builder extension(@Nullable String extension) {
            this.extension = new StringWritable(extension);
            return this;
        }

        @NonNull
        public MavenPlugin.Builder extension(@Nullable Writable extension) {
            this.extension = extension;
            return this;
        }

        @NonNull
        public MavenPlugin.Builder order(int order) {
            this.order = order;
            return this;
        }

        @NonNull
        public MavenPlugin.Builder artifactId(String artifactId) {
            this.artifactId = artifactId;
            return this;
        }

        @NonNull
        public MavenPlugin.Builder groupId(String groupId) {
            this.groupId = groupId;
            return this;
        }
        public MavenPlugin.Builder version(String version) {
            this.version = version;
            return this;
        }

        @NonNull
        public MavenPlugin build() {
            Objects.requireNonNull(artifactId, "The artifact id must be set");
            if (groupId != null && extension == null) {
                extension = new StringWritable(
"<plugin>\n" +
    "  <groupId>" + groupId + "</groupId>\n" +
    "  <artifactId>" + artifactId + "</artifactId>\n" +
    (version != null ? "  <version>" + version + "</version>\n" : "") +
    "</plugin>\n");
            }
            Objects.requireNonNull(extension, "Maven plugins require an extension or a groupId");
            return new MavenPlugin(groupId, artifactId, version, extension, order);
        }
    }
}
