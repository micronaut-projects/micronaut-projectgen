/*
 * Copyright 2017-2025 original authors
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

import io.micronaut.projectgen.core.buildtools.Phase;
import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.buildtools.Source;
import io.micronaut.projectgen.core.buildtools.dependencies.Coordinate;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.buildtools.dependencies.DependencyCoordinate;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.options.Language;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @param combineAttribute combine attribute
 * @param annotationProcessors annotation processors
 * @param testCombineAttribute test combine attribute
 * @param testAnnotationProcessors test annotation processors
 */
public record MavenCompilerPluginAnnotationProcessors(
    MavenCombineAttribute combineAttribute,
    List<DependencyCoordinate> annotationProcessors,
    MavenCombineAttribute testCombineAttribute,
    List<DependencyCoordinate> testAnnotationProcessors) {

    public static MavenCompilerPluginAnnotationProcessors of(ModuleContext module, Language language) {
        List<DependencyCoordinate> annotationProcessors = new ArrayList<>();
        List<DependencyCoordinate> testAnnotationProcessors = new ArrayList<>();
        MavenCombineAttribute combineAttribute = mavenCombineAttribute(language);
        MavenCombineAttribute testCombineAttribute = combineAttribute;

        for (Dependency dependency : module.getDependencies()) {
            Scope scope = dependency.getScope();
            if (scope != null && scope.getPhases().contains(Phase.ANNOTATION_PROCESSING)) {
                if (scope.getSource() == Source.MAIN && language != Language.GROOVY) {
                    // Don't add these for Groovy projects: it results in multiple dependencies.
                    // DependencyContext has already resolved Groovy annotation processors as dependencies
                    annotationProcessors.add(new DependencyCoordinate(dependency, true));
                    if (dependency.isAnnotationProcessorPriority()) {
                        combineAttribute = MavenCombineAttribute.OVERRIDE;
                    }
                }
                if (scope.getSource() == Source.TEST) {
                    testAnnotationProcessors.add(new DependencyCoordinate(dependency, true));
                    if (dependency.isAnnotationProcessorPriority()) {
                        testCombineAttribute = MavenCombineAttribute.OVERRIDE;
                    }
                }
            }
        }
        annotationProcessors.sort(Coordinate.COMPARATOR);
        testAnnotationProcessors.sort(Coordinate.COMPARATOR);
        return new MavenCompilerPluginAnnotationProcessors(combineAttribute,
            annotationProcessors,
            testCombineAttribute,
            testAnnotationProcessors);
    }

    public static MavenCombineAttribute mavenCombineAttribute(Language language) {
        boolean isKotlin = language == Language.KOTLIN;
        return isKotlin ? MavenCombineAttribute.OVERRIDE : MavenCombineAttribute.APPEND;
    }
}
