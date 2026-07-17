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

import org.jspecify.annotations.NonNull;
import io.micronaut.core.order.OrderUtil;
import io.micronaut.projectgen.core.buildtools.BuildProperties;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.options.Language;
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
    public @NonNull MavenBuild create(ModuleContext module,
                             Options options) {
        Language language = options.language();
        List<MavenDependency> dependencies = MavenDependency.listOf(module.dependencyContext(),
            language);
        BuildProperties buildProperties = module.buildProperties();


        List<MavenPlugin> plugins = module.buildPlugins()
            .stream()
            .filter(MavenPlugin.class::isInstance)
            .map(MavenPlugin.class::cast)
            .sorted(OrderUtil.COMPARATOR)
            .toList();

        MavenCompilerPluginAnnotationProcessors ann = MavenCompilerPluginAnnotationProcessors.of(module, language);
        return new MavenBuild(
            module.moduleAttributes().getName(),
            module.moduleAttributes().getDescription(),
            module.moduleAttributes().getCoordinate(),
            module.moduleAttributes().getPackaging(),
            module.moduleAttributes().getParentPom(),
            ann.combineAttribute(),
            ann.testCombineAttribute(),
            ann.testAnnotationProcessors(),
            ann.annotationProcessors(),
            dependencies,
            plugins,
            buildProperties.getProperties(),
            module.profiles(),
            MavenRepository.listOf(module.repositories()));
    }
}
