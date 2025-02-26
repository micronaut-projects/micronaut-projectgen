/*
 * Copyright 2017-2021 original authors
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
package io.micronaut.projectgen.core.buildtools.dependencies;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.projectgen.core.buildtools.Scope;

import java.util.*;

/**
 * Dependency.
 */
public final class Dependency implements Coordinate {

    public static final Comparator<Dependency> COMPARATOR = (o1, o2) -> {
        int comparison = Integer.compare(o1.getScope().getOrder(), o2.getScope().getOrder());
        if (comparison != 0) {
            return comparison;
        }
        return Coordinate.COMPARATOR.compare(o1, o2);
    };

    @Nullable
    private final Scope scope;

    @Nullable
    private final String groupId;

    @NonNull
    private final String artifactId;

    @Nullable
    private final String version;

    @Nullable
    private final String versionProperty;
    private final boolean requiresLookup;
    private final int order;
    private final boolean annotationProcessorPriority;
    private final boolean pom;

    @Nullable
    private final List<Dependency> exclusions;

    @Nullable
    private final List<Substitution> substitutions;

    private Dependency(Scope scope,
                       String groupId,
                       String artifactId,
                       String version,
                       String versionProperty,
                       boolean requiresLookup,
                       boolean annotationProcessorPriority,
                       int order,
                       boolean pom,
                       @Nullable List<Dependency> exclusions,
                       @Nullable List<Substitution> substitutions) {
        this.scope = scope;
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.versionProperty = versionProperty;
        this.requiresLookup = requiresLookup;
        this.annotationProcessorPriority = annotationProcessorPriority;
        this.order = order;
        this.pom = pom;
        this.exclusions = exclusions;
        this.substitutions = substitutions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Dependency that = (Dependency) o;

        if (requiresLookup != that.requiresLookup) {
            return false;
        }
        if (order != that.order) {
            return false;
        }
        if (annotationProcessorPriority != that.annotationProcessorPriority) {
            return false;
        }
        if (pom != that.pom) {
            return false;
        }
        if (scope != null ? !scope.equals(that.scope) : that.scope != null) {
            return false;
        }
        if (groupId != null ? !groupId.equals(that.groupId) : that.groupId != null) {
            return false;
        }
        if (!artifactId.equals(that.artifactId)) {
            return false;
        }
        if (version != null ? !version.equals(that.version) : that.version != null) {
            return false;
        }
        if (versionProperty != null ? !versionProperty.equals(that.versionProperty) : that.versionProperty != null) {
            return false;
        }
        if (exclusions != null ? !exclusions.equals(that.exclusions) : that.exclusions != null) {
            return false;
        }
        return substitutions != null ? substitutions.equals(that.substitutions) : that.substitutions == null;
    }

    @Override
    public int hashCode() {
        int result = scope != null ? scope.hashCode() : 0;
        result = 31 * result + (groupId != null ? groupId.hashCode() : 0);
        result = 31 * result + artifactId.hashCode();
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (versionProperty != null ? versionProperty.hashCode() : 0);
        result = 31 * result + (requiresLookup ? 1 : 0);
        result = 31 * result + order;
        result = 31 * result + (annotationProcessorPriority ? 1 : 0);
        result = 31 * result + (pom ? 1 : 0);
        result = 31 * result + (exclusions != null ? exclusions.hashCode() : 0);
        result = 31 * result + (substitutions != null ? substitutions.hashCode() : 0);
        return result;
    }

    @Nullable
    public List<Dependency> getExclusions() {
        return exclusions;
    }

    @Nullable
    public List<Substitution> getSubstitutions() {
        return substitutions;
    }

    public Scope getScope() {
        return scope;
    }

    @Override
    public String getGroupId() {
        return groupId;
    }

    @Override
    public String getArtifactId() {
        return artifactId;
    }

    @Nullable
    @Override
    public String getVersion() {
        return version;
    }

    @Nullable
    public String getVersionProperty() {
        return versionProperty;
    }

    public int getOrder() {
        return order;
    }

    @Override
    public boolean isPom() {
        return pom;
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean requiresLookup() {
        return requiresLookup;
    }

    public Dependency resolved(Coordinate coordinate) {
        return new Dependency(
            scope,
            coordinate.getGroupId(),
            artifactId,
            coordinate.getVersion(),
            null,
            false,
            annotationProcessorPriority,
            order,
            coordinate.isPom(),
            Collections.emptyList(),
            Collections.emptyList());
    }

    public boolean isAnnotationProcessorPriority() {
        return annotationProcessorPriority;
    }

    public static Dependency of(Dependency dep, Scope scope) {
        return new Dependency(scope,
            dep.getGroupId(),
            dep.getArtifactId(),
            dep.getVersion(),
            dep.getVersionProperty(),
            dep.requiresLookup(),
            dep.isAnnotationProcessorPriority(),
            dep.getOrder(),
            dep.isPom(),
            dep.getExclusions(),
            dep.getSubstitutions());
    }

    /**
     * Builder.
     */
    public static class Builder {

        private Scope scope;
        private String groupId;
        private String artifactId;
        private String version;
        private String versionProperty;
        private boolean requiresLookup;
        private int order;
        private boolean template;
        private boolean annotationProcessorPriority;
        private boolean pom;
        private List<Dependency> exclusions;
        private List<Substitution> substitutions;

        /**
         *
         * @param scope Scope
         * @return The Builder
         */
        public Builder scope(@NonNull Scope scope) {
            if (template) {
                return copy().scope(scope);
            } else {
                this.scope = scope;
                return this;
            }
        }

        /**
         * Development Only Scope.
         * @return Builder
         */
        public Builder developmentOnly() {
            return scope(Scope.DEVELOPMENT_ONLY);
        }

        /**
         * Compile Scope.
         * @return Builder
         */
        public Builder compile() {
            return scope(Scope.COMPILE);
        }

        /**
         * API Scope.
         * @return Builder
         */
        public Builder api() {
            return scope(Scope.API);
        }

        /**
         * Compile Only Scope.
         * @return Builder
         */
        public Builder compileOnly() {
            return scope(Scope.COMPILE_ONLY);
        }

        /**
         * Runtime scope.
         * @return Builder
         */
        public Builder runtime() {
            return scope(Scope.RUNTIME);
        }

        /**
         * Test Scope.
         * @return Builder
         */
        public Builder test() {
            return scope(Scope.TEST);
        }

        /**
         * Test Compile Only Scope.
         * @return Builder
         */
        @SuppressWarnings("unused")
        public Builder testCompileOnly() {
            return scope(Scope.TEST_COMPILE_ONLY);
        }

        /**
         * Test Runtime Scope.
         * @return Builder
         */
        public Builder testRuntime() {
            return scope(Scope.TEST_RUNTIME);
        }

        /**
         * Test Resources Service.
         * @return Builder
         */
        public Builder testResourcesService() {
            return scope(Scope.TEST_RESOURCES_SERVICE);
        }

        /**
         * Native Image Compile Only.
         * @return Builder
         */
        public Builder nativeImageCompileOnly() {
            return scope(Scope.NATIVE_IMAGE_COMPILE_ONLY);
        }

        /**
         * Annotation Processor Scope.
         * @return Builder
         */
        public Builder annotationProcessor() {
            return scope(Scope.ANNOTATION_PROCESSOR);
        }

        /**
         * Annotation Processor Scope.
         * @param requiresPriority Whether the annotation processor requires priority
         * @return Builder
         */
        public Builder annotationProcessor(boolean requiresPriority) {
            this.annotationProcessorPriority = requiresPriority;
            return annotationProcessor();
        }

        /**
         * Test Annotation Processor Scope.
         * @return Builder
         */
        public Builder testAnnotationProcessor() {
            return scope(Scope.TEST_ANNOTATION_PROCESSOR);
        }

        /**
         * Test Annotation Processor Scope.
         * @param requiresPriority Whether the annotation processor requires priority
         * @return Builder
         */
        @SuppressWarnings("unused")
        public Builder testAnnotationProcessor(boolean requiresPriority) {
            this.annotationProcessorPriority = requiresPriority;
            return testAnnotationProcessor();
        }

        /**
         * @param groupId Group ID
         * @return Builder
         */
        public Builder groupId(@Nullable String groupId) {
            if (template) {
                return copy().groupId(groupId);
            } else {
                this.groupId = groupId;
                return this;
            }
        }

        /**
         * @param artifactId Artifact ID
         * @return Builder
         */
        public Builder artifactId(@NonNull String artifactId) {
            if (template) {
                return copy().artifactId(artifactId);
            } else {
                this.artifactId = artifactId;
                return this;
            }
        }

        /**
         * @param artifactId Artifact ID to Lookup
         * @return Builder
         */
        public Builder lookupArtifactId(@NonNull String artifactId) {
            if (template) {
                return copy().lookupArtifactId(artifactId);
            } else {
                this.artifactId = artifactId;
                this.requiresLookup = true;
                return this;
            }
        }

        /**
         * @param version Version
         * @return Builder
         */
        public Builder version(@Nullable String version) {
            if (template) {
                return copy().version(version);
            } else {
                this.version = version;
                return this;
            }
        }

        /**
         * @param versionProperty Version Property
         * @return Builder
         */
        public Builder versionProperty(@Nullable String versionProperty) {
            if (template) {
                return copy().versionProperty(versionProperty);
            } else {
                this.versionProperty = versionProperty;
                return this;
            }
        }

        /**
         * @param dependency Dependency to exclude
         * @return Builder
         */
        public Builder exclude(Dependency dependency) {
            if (this.exclusions == null) {
                this.exclusions = new ArrayList<>();
            }
            this.exclusions.add(dependency);
            return this;
        }

        /**
         * @param substitution Substitution
         * @return Builder
         */
        public Builder substitution(Substitution substitution) {
            if (this.substitutions == null) {
                this.substitutions = new ArrayList<>();
            }
            this.substitutions.add(substitution);
            return this;
        }

        /**
         * @param order order
         * @return Builder
         */
        public Builder order(int order) {
            if (template) {
                return copy().order(order);
            } else {
                this.order = order;
                return this;
            }
        }

        /**
         * Set template true.
         * @return Builder
         */
        public Builder template() {
            this.template = true;
            return this;
        }

        /**
         *
         * @param pom pom
         * @return Builder
         */
        public Builder pom(boolean pom) {
            this.pom = pom;
            return this;
        }

        /**
         * @return Builder
         */
        public Builder pom() {
            return pom(true);
        }

        /**
         *
         * @return instantiate Dependency
         */
        public Dependency build() {
            Objects.requireNonNull(artifactId, "The artifact id must be set");
            return buildInternal();
        }

        /**
         *
         * @return Dependency Coordinate
         */
        public DependencyCoordinate buildCoordinate() {
            return buildCoordinate(false);
        }

        /**
         * @param showVersionProperty Show Version Property
         * @return Dependency Coordinate
         */
        public DependencyCoordinate buildCoordinate(boolean showVersionProperty) {
            Objects.requireNonNull(artifactId, "The artifact id must be set");

            return new DependencyCoordinate(buildInternal(), showVersionProperty);
        }

        private Dependency buildInternal() {
            return new Dependency(
                scope,
                groupId,
                artifactId,
                version,
                versionProperty,
                requiresLookup,
                annotationProcessorPriority,
                order,
                pom,
                exclusions,
                substitutions);
        }

        private Builder copy() {
            Builder builder = new Builder().scope(scope);
            if (requiresLookup) {
                builder.lookupArtifactId(artifactId);
            } else {
                builder.groupId(groupId).artifactId(artifactId);
                if (versionProperty != null) {
                    builder.versionProperty(versionProperty);
                } else {
                    builder.version(version);
                }
            }
            return builder.order(order).pom(pom);
        }
    }
}
