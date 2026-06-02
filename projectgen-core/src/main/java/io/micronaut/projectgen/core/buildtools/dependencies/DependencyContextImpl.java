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
package io.micronaut.projectgen.core.buildtools.dependencies;

import org.jspecify.annotations.NonNull;
import io.micronaut.projectgen.core.buildtools.BuildTool;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Default implementation of {@link DependencyContext}.
 */
public class DependencyContextImpl implements DependencyContext {
    private final CoordinateResolver coordinateResolver;
    private final Set<Dependency> dependencies = new HashSet<>();
    private final Map<BuildTool, Set<Dependency>> dependenciesByTool = new HashMap<>();

    public DependencyContextImpl(CoordinateResolver coordinateResolver) {
        this.coordinateResolver = coordinateResolver;
    }

    @Override
    public void addDependency(@NonNull Dependency dependency) {
        dependencies.add(resolveDependency(dependency));
    }

    private Dependency resolveDependency(@NonNull Dependency dependency) {
        if (dependency.requiresLookup()) {
            Coordinate coordinate = coordinateResolver.resolve(dependency.getArtifactId())
                .orElseThrow(() -> new LookupFailedException(dependency.getArtifactId()));
            return dependency.resolved(coordinate);
        }
        return dependency;
    }

    @Override
    public void addDependencyOnlyForBuild(Dependency dependency, BuildTool buildTool) {
        dependenciesByTool.computeIfAbsent(buildTool, t -> new HashSet<>())
            .add(resolveDependency(dependency));
    }

    @Override
    public @NonNull Set<Dependency> getDependencies() {
        return dependencies;
    }

    @Override
    public @NonNull Collection<Dependency> getDependenciesByBuildTool(@NonNull BuildTool buildTool) {
        Set<Dependency> result = new HashSet<>();
        result.addAll(dependencies);
        Collection<Dependency> dependencyByBuildTools = dependenciesByTool.get(buildTool);
        if (dependencyByBuildTools != null) {
            result.addAll(dependencyByBuildTools);
        }
        return result;
    }

    /**
     *
     * @param artifactId Artifact ID
     * @return Coordinate
     */
    public Coordinate resolveCoordinate(String artifactId) {
        return coordinateResolver.resolve(artifactId)
            .orElseThrow(() -> new LookupFailedException(artifactId));
    }

}
