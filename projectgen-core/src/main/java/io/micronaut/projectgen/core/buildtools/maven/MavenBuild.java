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
import io.micronaut.projectgen.core.buildtools.dependencies.Coordinate;
import io.micronaut.projectgen.core.buildtools.dependencies.DependencyCoordinate;
import io.micronaut.projectgen.core.template.Writable;
import io.micronaut.projectgen.core.template.WritableUtils;
import io.micronaut.sourcegen.annotations.Builder;

import java.util.Collection;

import java.util.List;
import java.util.Objects;

/**
 * Maven Build.
 * @param name Name
 * @param description description
 * @param coordinate coordinate
 * @param packaging packaging
 * @param parentPom Parent Pom
 * @param annotationProcessorCombineAttribute Annotation Processor combine attribute
 * @param testAnnotationProcessorCombineAttribute Test Annotation Processor combine attribute
 * @param testAnnotationProcessors Test annotation processors
 * @param annotationProcessors annotation processors
 * @param dependencies Dependencies
 * @param plugins Plugins
 * @param properties properties
 * @param profiles profiles
 * @param repositories repositories
 */
@Builder
public record MavenBuild(@Nullable String name,
                         @Nullable String description,
                         @Nullable Coordinate coordinate,
                         @Nullable String packaging,
                         @Nullable ParentPom parentPom,
                         @Nullable MavenCombineAttribute annotationProcessorCombineAttribute,
                         @Nullable MavenCombineAttribute testAnnotationProcessorCombineAttribute,
                         List<DependencyCoordinate> testAnnotationProcessors,
                         List<DependencyCoordinate> annotationProcessors,
                         List<MavenDependency> dependencies,
                         List<MavenPlugin> plugins,
                         List<Property> properties,
                         Collection<Profile> profiles,
                         List<MavenRepository> repositories) {
    /**
     *
     * @param indentationSpaces Indentation Spaces
     * @return rendered string
     */
    public @NonNull String renderRepositories(int indentationSpaces) {
        return WritableUtils.renderWritableList(this.repositories.stream()
            .map(Writable.class::cast)
            .toList(), indentationSpaces);
    }

    /**
     *
     * @param indentationSpaces Indentation Spaces
     * @return rendered string
     */
    public @NonNull String renderPlugins(int indentationSpaces) {
        List<Writable> writableList = plugins.stream()
            .map(MavenPlugin::getExtension)
            .filter(Objects::nonNull)
            .toList();
        return WritableUtils.renderWritableList(writableList, indentationSpaces);
    }

    /**
     *
     * @param indentationSpaces Indentation Spaces
     * @return rendered string
     */
    public @NonNull String renderProfiles(int indentationSpaces) {
        List<Writable> writableList = profiles.stream()
            .map(Profile::getExtension)
            .filter(Objects::nonNull)
            .toList();
        return WritableUtils.renderWritableList(writableList, indentationSpaces);
    }

    /**
     *
     * @param pom pom
     * @return Dependencies
     */
    public @NonNull List<MavenDependency> getDependencies(boolean pom) {
        return dependencies
            .stream()
            .filter(it -> it.isPom() == pom)
            .toList();
    }

    /**
     *
     * @return Has Pom dependencies
     */
    public boolean hasPomDependency() {
        return dependencies.stream().anyMatch(Coordinate::isPom);
    }
}
