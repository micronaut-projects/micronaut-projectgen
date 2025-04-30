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
import io.micronaut.projectgen.core.buildtools.Property;
import io.micronaut.projectgen.core.buildtools.dependencies.Coordinate;
import io.micronaut.projectgen.core.buildtools.dependencies.DependencyCoordinate;
import io.micronaut.projectgen.core.template.Writable;
import io.micronaut.projectgen.core.template.WritableUtils;
import io.micronaut.sourcegen.annotations.Builder;
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
@Builder
public record MavenBuild(String name,
                         String description,
                         Coordinate coordinate,
                         @Nullable String packaging,
                         ParentPom parentPom,
                         MavenCombineAttribute annotationProcessorCombineAttribute,
                         MavenCombineAttribute testAnnotationProcessorCombineAttribute,
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
    @NonNull
    public String renderRepositories(int indentationSpaces) {
        return WritableUtils.renderWritableList(this.repositories.stream()
            .map(Writable.class::cast)
            .toList(), indentationSpaces);
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
            .toList();
        return WritableUtils.renderWritableList(writableList, indentationSpaces);
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
