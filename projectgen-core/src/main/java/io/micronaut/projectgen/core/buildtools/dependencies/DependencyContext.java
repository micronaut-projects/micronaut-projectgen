/*
 * Copyright 2017-2023 original authors
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
import io.micronaut.projectgen.core.buildtools.Phase;
import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.buildtools.Source;
import io.micronaut.projectgen.core.options.Language;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import static io.micronaut.projectgen.core.buildtools.Phase.COMPILATION;
import static io.micronaut.projectgen.core.buildtools.Phase.RUNTIME;

/**
 * Dependency Context.
 */
public interface DependencyContext {

    Predicate<Dependency> IS_COMPILE_API_OR_RUNTIME = d -> d.getScope() != null && (d.getScope().getPhases().contains(COMPILATION) ||
        d.getScope().getPhases().contains(Phase.PUBLIC_API) ||
        d.getScope().getPhases().contains(RUNTIME));

    @NonNull
    Collection<Dependency> getDependencies();

    @NonNull
    Collection<Dependency> getDependenciesByBuildTool(@NonNull BuildTool buildTool);

    void addDependency(@NonNull Dependency dependency);

    default void addDependency(Dependency.@NonNull Builder dependency) {
        addDependency(dependency.build());
    }

    default @NonNull List<Dependency> removeDuplicates(Collection<Dependency> dependencies, Language language, BuildTool buildTool) {

        List<Dependency> dependenciesNotInMainOrTestClasspath = dependencies.stream()
            .filter(d -> {
                if (language == Language.GROOVY && buildTool == BuildTool.MAVEN) {
                    return !IS_COMPILE_API_OR_RUNTIME.test(d) && (d.getScope() == null || !d.getScope().getPhases().contains(Phase.ANNOTATION_PROCESSING));
                }
                return !IS_COMPILE_API_OR_RUNTIME.test(d);
            })
            .toList();

        List<Dependency> dependenciesInMainClasspath = dependencies.stream()
            .filter(d -> {
                if (d.getScope() == null || d.getScope().getSource() != Source.MAIN) {
                    return false;
                }
                if (language == Language.GROOVY && buildTool == BuildTool.MAVEN) {
                    return IS_COMPILE_API_OR_RUNTIME.test(d) || d.getScope().getPhases().contains(Phase.ANNOTATION_PROCESSING);
                }
                return IS_COMPILE_API_OR_RUNTIME.test(d);

            })
            .toList();
        List<Dependency> dependenciesInMainClasspathWithoutDuplicates = filterDuplicates(dependenciesInMainClasspath);

        List<Dependency> dependenciesInTestClasspath = dependencies.stream()
            .filter(d -> d.getScope() != null && d.getScope().getSource() == Source.TEST && IS_COMPILE_API_OR_RUNTIME.test(d))
            .toList();

        List<Dependency> dependenciesInTestClasspathWithoutDuplicates = filterDuplicates(dependenciesInTestClasspath);

        dependenciesInTestClasspathWithoutDuplicates.removeIf(testDep -> dependenciesInMainClasspathWithoutDuplicates.stream()
            .filter(mainDep -> {
                Scope scope = Objects.requireNonNull(mainDep.getScope());
                return (buildTool == BuildTool.MAVEN && scope.getPhases().contains(RUNTIME)) ||
                    (scope.getPhases().contains(RUNTIME) && scope.getPhases().contains(COMPILATION));
            }).anyMatch(mainDep -> sameCoordinate(mainDep, testDep)));
        List<Dependency> result = new ArrayList<>(dependenciesNotInMainOrTestClasspath);
        result.addAll(dependenciesInMainClasspathWithoutDuplicates);
        result.addAll(dependenciesInTestClasspathWithoutDuplicates);
        return result.stream().sorted(Dependency.COMPARATOR).toList();
    }

    private static List<Dependency> filterDuplicates(List<Dependency> dependencies) {
        List<Dependency> dependenciesWithoutDuplicates = new ArrayList<>();
        Map<Dependency, Scope> dependenciesWithScope = new HashMap<>();
        for (Dependency dep : dependencies.stream().filter(dep -> dep.getScope() != null).sorted(Dependency.COMPARATOR).toList()) {
            Optional<Dependency> duplicate = dependenciesWithScope.keySet()
                .stream()
                .filter(candidate -> sameCoordinate(candidate, dep))
                .findFirst();
            if (duplicate.isPresent()) {
                Dependency duplicateDependency = duplicate.get();
                if (Objects.requireNonNull(dependenciesWithScope.get(duplicateDependency)).getOrder() < dep.getOrder()) {
                    dependenciesWithScope.remove(duplicateDependency);
                    dependenciesWithoutDuplicates.removeIf(f -> sameCoordinate(f, dep));
                    dependenciesWithScope.put(dep, Objects.requireNonNull(dep.getScope()));
                    dependenciesWithoutDuplicates.add(dep);
                }
            } else {
                dependenciesWithScope.put(dep, Objects.requireNonNull(dep.getScope()));
                dependenciesWithoutDuplicates.add(dep);
            }
        }
        return dependenciesWithoutDuplicates;
    }

    private static boolean sameCoordinate(Dependency left, Dependency right) {
        return Objects.equals(left.getGroupId(), right.getGroupId()) &&
            left.getArtifactId().equals(right.getArtifactId()) &&
            Objects.equals(left.getVersion(), right.getVersion());
    }

    void addDependencyOnlyForBuild(@NonNull Dependency dependency, @NonNull BuildTool buildTool);

    default void addDependencyOnlyForBuild(Dependency.@NonNull Builder dependency, @NonNull BuildTool buildTool) {
        addDependencyOnlyForBuild(dependency.build(), buildTool);
    }
}
