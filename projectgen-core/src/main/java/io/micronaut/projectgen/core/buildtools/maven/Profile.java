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

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import io.micronaut.projectgen.core.buildtools.Property;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.rocker.RockerWritable;
import io.micronaut.projectgen.core.template.Writable;
import io.micronaut.projectgen.core.template.profile;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a Maven profile.
 */
public class Profile {

    private final @NonNull String id;

    @Nullable
    private final Writable extension;

    @Nullable
    private Set<Property> activationProperties;

    @Nullable
    private Set<Dependency> dependencies;

    public Profile(@NonNull String id,
                   @Nullable Set<Property> activationProperties,
                   @Nullable Set<Dependency> dependencies,
                   @Nullable Writable extension) {
        this.id = id;
        this.activationProperties = activationProperties;
        this.dependencies = dependencies;
        this.extension = extension;
    }

    /**
     *
     * @return Profile ID
     */
    public @NonNull String getId() {
        return id;
    }

    /**
     * Returns the extension used in the build plugin, if any.
     *
     * @return the extension or {@code null} if not set
     */
    @Nullable
    public Writable getExtension() {
        return extension;
    }

    /**
     *
     * @return Activation properties
     */
    @Nullable
    public Set<Property> getActivationProperties() {
        return activationProperties;
    }

    /**
     *
     * @return Dependencies
     */
    @Nullable
    public Set<Dependency> getDependencies() {
        return dependencies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Profile profile = (Profile) o;

        if (!id.equals(profile.id)) {
            return false;
        }
        if (activationProperties != null ? !activationProperties.equals(profile.activationProperties) : profile.activationProperties != null) {
            return false;
        }
        return dependencies != null ? dependencies.equals(profile.dependencies) : profile.dependencies == null;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (activationProperties != null ? activationProperties.hashCode() : 0);
        result = 31 * result + (dependencies != null ? dependencies.hashCode() : 0);
        return result;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Adds the specified activation properties to the existing set.
     * If the current set is {@code null}, it will be initialized.
     *
     * @param activationProperties the activation properties to add, or {@code null}
     */
    public void addActivationProperties(@Nullable Set<Property> activationProperties) {
        if (activationProperties != null) {
            if (this.activationProperties == null) {
                this.activationProperties = activationProperties;
            } else {
                this.activationProperties.addAll(activationProperties);
            }
        }
    }

    /**
     * Adds the specified dependencies to the existing set.
     * If the current set is {@code null}, it will be initialized.
     *
     * @param dependencies the dependencies to add, or {@code null}
     */
    public void addDependencies(@Nullable Set<Dependency> dependencies) {
        if (dependencies != null) {
            if (this.dependencies == null) {
                this.dependencies = dependencies;
            } else {
                this.dependencies.addAll(dependencies);
            }
        }
    }

    /**
     * Builder.
     */
    public static class Builder {
        @Nullable
        private String id;
        @Nullable
        private Set<Dependency> dependencies;
        @Nullable
        private Set<Property> activationProperties;
        @Nullable
        private Writable extension;

        /**
         *
         * @param extension Extension
         * @return Builder
         */
        public @NonNull Builder extension(@NonNull Writable extension) {
            this.extension = extension;
            return this;
        }

        /**
         *
         * @param dependency Dependency
         * @return Builder
         */
        public @NonNull Builder dependency(@NonNull Dependency dependency) {
            if (dependencies == null) {
                dependencies = new HashSet<>();
            }
            dependencies.add(dependency);
            return this;
        }

        /**
         *
         * @param property property
         * @return Builder
         */
        public @NonNull Builder activationProperty(@NonNull Property property) {
            if (activationProperties == null) {
                activationProperties = new HashSet<>();
            }
            activationProperties.add(property);
            return this;
        }

        /**
         *
         * @param id id
         * @return Builder
         */
        public @NonNull Builder id(@NonNull String id) {
            this.id = id;
            return this;
        }

        /**
         *
         * @return Instantiate a profile
         */
        public Profile build() {
            String profileId = Objects.requireNonNull(id);
            if (extension == null) {
                extension = new RockerWritable(profile.template(new Profile(profileId, activationProperties, dependencies, null)));
            }
            return new Profile(profileId, activationProperties, dependencies, extension);
        }
    }
}
