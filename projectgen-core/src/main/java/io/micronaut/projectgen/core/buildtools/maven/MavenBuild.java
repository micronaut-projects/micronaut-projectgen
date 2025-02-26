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
import io.micronaut.projectgen.core.buildtools.Property;
import io.micronaut.projectgen.core.buildtools.dependencies.Coordinate;
import io.micronaut.projectgen.core.buildtools.dependencies.DependencyCoordinate;
import io.micronaut.projectgen.core.template.Writable;
import io.micronaut.projectgen.core.template.WritableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Maven Build.
 */
public class MavenBuild {
    private static final Logger LOG = LoggerFactory.getLogger(MavenBuild.class);

    private final MavenCombineAttribute annotationProcessorCombineAttribute;

    private final MavenCombineAttribute testAnnotationProcessorCombineAttribute;

    private final List<DependencyCoordinate> testAnnotationProcessors;

    private final List<DependencyCoordinate> annotationProcessors;

    private final List<MavenDependency> dependencies;

    private final List<MavenPlugin> plugins;

    private final List<Property> properties;

    private final Collection<Profile> profiles;

    private final List<MavenRepository> repositories;

    @NonNull
    private final String groupId;

    @NonNull
    private final String artifactId;

    @NonNull
    private final String version;

    public MavenBuild(String groupId,
                      String artifactId,
                      String version) {
        this(groupId,
            artifactId,
            version,
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            MavenCombineAttribute.APPEND,
            MavenCombineAttribute.APPEND,
            Collections.emptyList());
    }

    public MavenBuild(@NonNull String groupId,
                      @NonNull String artifactId,
                      @NonNull String version,
                      @NonNull List<MavenDependency> dependencies,
                      @NonNull List<MavenPlugin> plugins,
                      @NonNull List<MavenRepository> repositories) {
        this(groupId,
            artifactId,
            version,
            Collections.emptyList(),
            Collections.emptyList(),
            dependencies,
            Collections.emptyList(),
            plugins,
            repositories,
            MavenCombineAttribute.APPEND,
            MavenCombineAttribute.APPEND,
            Collections.emptyList());
    }

    public MavenBuild(@NonNull String groupId,
                      @NonNull String artifactId,
                      @NonNull String version,
                      @NonNull List<DependencyCoordinate> annotationProcessors,
                      @NonNull List<DependencyCoordinate> testAnnotationProcessors,
                      @NonNull List<MavenDependency> dependencies,
                      @NonNull List<Property> properties,
                      @NonNull List<MavenPlugin> plugins,
                      @NonNull List<MavenRepository> repositories,
                      @NonNull MavenCombineAttribute annotationProcessorCombineAttribute,
                      @NonNull MavenCombineAttribute testAnnotationProcessorCombineAttribute,
                      @NonNull Collection<Profile> profiles) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.annotationProcessors = annotationProcessors;
        this.testAnnotationProcessors = testAnnotationProcessors;
        this.dependencies = dependencies;
        this.properties = properties;
        this.plugins = plugins;
        this.repositories = repositories;
        this.annotationProcessorCombineAttribute = annotationProcessorCombineAttribute;
        this.testAnnotationProcessorCombineAttribute = testAnnotationProcessorCombineAttribute;
        this.profiles = profiles;
    }

    /**
     *
     * @return Group ID
     */
    @NonNull
    public String getGroupId() {
        return groupId;
    }

    /**
     *
     * @return Artifact ID
     */
    @NonNull
    public String getArtifactId() {
        return artifactId;
    }

    /**
     *
     * @return version
     */
    @NonNull
    public String getVersion() {
        return version;
    }

    /**
     *
     * @param indentationSpaces Indentation Spaces
     * @return rendered string
     */
    @NonNull
    public String renderRepositories(int indentationSpaces) {
        return WritableUtils.renderWritableList(this.repositories.stream()
            .map(Writable.class::cast)
            .collect(Collectors.toList()), indentationSpaces);
    }

    /**
     *
     * @param indentationSpaces Indentation Spaces
     * @return rendered string
     */
    @NonNull
    public String renderPlugins(int indentationSpaces) {
        List<Writable> writableList = plugins.stream()
            .map(MavenPlugin::getExtension)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        return WritableUtils.renderWritableList(writableList, indentationSpaces);
    }

    /**
     *
     * @return Annotation Processors
     */
    @NonNull
    public List<DependencyCoordinate> getAnnotationProcessors() {
        return annotationProcessors;
    }

    /**
     *
     * @return Test annotation processors
     */
    @NonNull
    public List<DependencyCoordinate> getTestAnnotationProcessors() {
        return testAnnotationProcessors;
    }

    /**
     *
     * @return Maven Profiles
     */
    @NonNull
    public Collection<Profile> getProfiles() {
        return profiles;
    }

    /**
     *
     * @return Dependencies
     */
    @NonNull
    public List<MavenDependency> getDependencies() {
        return dependencies;
    }

    /**
     *
     * @param pom pom
     * @return Dependencies
     */
    @NonNull
    public List<MavenDependency> getDependencies(boolean pom) {
        return dependencies
            .stream()
            .filter(it -> it.isPom() == pom)
            .collect(Collectors.toList());
    }

    /**
     *
     * @return Has Pom dependencies
     */
    public boolean hasPomDependency() {
        return dependencies.stream().anyMatch(Coordinate::isPom);
    }

    /**
     *
     * @return build properties
     */
    @NonNull
    public List<Property> getProperties() {
        return properties;
    }

    /**
     *
     * @return annotation processors combine attribute
     */
    public MavenCombineAttribute getAnnotationProcessorCombineAttribute() {
        return annotationProcessorCombineAttribute;
    }

    /**
     *
     * @return test annotation processors combine attribute
     */
    @NonNull
    public MavenCombineAttribute getTestAnnotationProcessorCombineAttribute() {
        return testAnnotationProcessorCombineAttribute;
    }
}
