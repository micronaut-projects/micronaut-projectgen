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
import io.micronaut.projectgen.core.buildtools.BuildProperties;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.options.Options;
import jakarta.inject.Singleton;

import java.util.List;

/**
 * Maven Build Creator.
 */
@Singleton
public class MavenBuildCreator {
    /**
     *
     * @param module Module Context
     * @param options Options
     * @return Maven Build
     */
    @NonNull
    public MavenBuild create(ModuleContext module,
                             Options options) {
        List<MavenDependency> dependencies = MavenDependency.listOf(module.dependencyContext(),
            options.language());
        BuildProperties buildProperties = module.buildProperties();


        List<MavenPlugin> plugins = module.buildPlugins()
            .stream()
            .filter(MavenPlugin.class::isInstance)
            .map(MavenPlugin.class::cast)
            .sorted(OrderUtil.COMPARATOR)
            .toList();

        MavenCompilerPluginAnnotationProcessors ann = MavenCompilerPluginAnnotationProcessors.of(module, options.language());
        return MavenBuildBuilder.builder()
            .parentPom(module.moduleAttributes().getParentPom())
            .packaging(module.moduleAttributes().getPackaging())
            .coordinate(module.moduleAttributes().getCoordinate())
            .name(module.moduleAttributes().getName())
            .description(module.moduleAttributes().getDescription())
            .repositories(MavenRepository.listOf(module.repositories()))
            .plugins(plugins)
            .properties(buildProperties.getProperties())
            .annotationProcessorCombineAttribute(ann.combineAttribute())
            .testAnnotationProcessorCombineAttribute(ann.testCombineAttribute())
            .profiles(module.profiles())
            .dependencies(dependencies)
            .annotationProcessors(ann.annotationProcessors())
            .testAnnotationProcessors(ann.testAnnotationProcessors())
            .build();
    }
}
