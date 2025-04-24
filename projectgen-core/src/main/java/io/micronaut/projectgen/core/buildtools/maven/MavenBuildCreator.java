/*
 * Copyright 2017-2024 original authors
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
import io.micronaut.core.order.OrderUtil;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.buildtools.*;
import io.micronaut.projectgen.core.buildtools.dependencies.Coordinate;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.buildtools.dependencies.DependencyCoordinate;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.options.Options;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

/**
 * Maven Build Creator.
 */
@Singleton
public class MavenBuildCreator {

    /**
     *
     * @param moduleContext Module Context
     * @param options Options
     * @return Maven Build
     */
    @NonNull
    public MavenBuild create(ModuleContext moduleContext,
                             Options options) {
        List<MavenDependency> dependencies = MavenDependency.listOf(moduleContext.dependencyContext(),
            options.language());
        BuildProperties buildProperties = moduleContext.buildProperties();
        List<DependencyCoordinate> annotationProcessorsCoordinates = new ArrayList<>();
        List<DependencyCoordinate> testAnnotationProcessorsCoordinates = new ArrayList<>();
        boolean isKotlin = options.language() == Language.KOTLIN;
        MavenCombineAttribute combineAttribute = isKotlin ? MavenCombineAttribute.OVERRIDE : MavenCombineAttribute.APPEND;
        MavenCombineAttribute testCombineAttribute = combineAttribute;

        for (Dependency dependency : moduleContext.getDependencies()) {
            if (dependency.getScope().getPhases().contains(Phase.ANNOTATION_PROCESSING)) {
                if (dependency.getScope().getSource() == Source.MAIN && options.language() != Language.GROOVY) {
                    // Don't add these for Groovy projects: it results in multiple dependencies.
                    // DependencyContext has already resolved Groovy annotation processors as dependencies
                    annotationProcessorsCoordinates.add(new DependencyCoordinate(dependency, true));
                    if (dependency.isAnnotationProcessorPriority()) {
                        combineAttribute = MavenCombineAttribute.OVERRIDE;
                    }
                }
                if (dependency.getScope().getSource() == Source.TEST) {
                    testAnnotationProcessorsCoordinates.add(new DependencyCoordinate(dependency, true));
                    if (dependency.isAnnotationProcessorPriority()) {
                        testCombineAttribute = MavenCombineAttribute.OVERRIDE;
                    }
                }
            }
        }


        annotationProcessorsCoordinates.sort(Coordinate.COMPARATOR);
        testAnnotationProcessorsCoordinates.sort(Coordinate.COMPARATOR);

        List<MavenPlugin> plugins = moduleContext.buildPlugins()
            .stream()
            .filter(MavenPlugin.class::isInstance)
            .map(MavenPlugin.class::cast)
            .sorted(OrderUtil.COMPARATOR)
            .toList();

        return new MavenBuild(options.group(),
            StringUtils.isNotEmpty(options.artifact())
                ? options.artifact()
                : options.name(),
            options.version(),
            annotationProcessorsCoordinates,
            testAnnotationProcessorsCoordinates,
            dependencies,
            buildProperties.getProperties(),
            plugins,
            MavenRepository.listOf(moduleContext.repositories()),
            combineAttribute,
            testCombineAttribute,
            moduleContext.profiles());
    }
}
