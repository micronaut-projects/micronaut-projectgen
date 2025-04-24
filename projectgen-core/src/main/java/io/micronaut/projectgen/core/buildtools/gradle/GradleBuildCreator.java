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
package io.micronaut.projectgen.core.buildtools.gradle;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.order.OrderUtil;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.Repository;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.options.Options;
import jakarta.inject.Singleton;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Gradle Build Creator.
 */
@Singleton
public class GradleBuildCreator {

    /**
     *
     * @param generatorContext Generator Context
     * @param moduleContext Module Context
     * @param useVersionCatalogue Use version catalogue
     * @return Gradle Build
     */
    @NonNull
    public GradleBuild create(@NonNull GeneratorContext generatorContext,
                              @NonNull ModuleContext moduleContext,
                              Options options, boolean useVersionCatalogue) {
        BuildTool buildTool = options.buildTools().stream().filter(BuildTool::isGradle).findFirst().orElseThrow();
        GradleDsl gradleDsl = buildTool
                .getGradleDsl()
                .orElseThrow(() -> new IllegalArgumentException("GradleBuildCreator can only create Gradle builds"));
        List<GradlePlugin> gradlePlugins = moduleContext.buildPlugins()
                .stream()
                .filter(GradlePlugin.class::isInstance)
                .map(GradlePlugin.class::cast)
                .sorted(OrderUtil.COMPARATOR)
                .collect(Collectors.toList());
        return new GradleBuild(gradleDsl,
                GradleDependency.listOf(generatorContext, moduleContext.dependencyContext(), generatorContext.getOptions(), useVersionCatalogue),
                gradlePlugins,
                getRepositories(options, moduleContext.repositories()));
    }

    /**
     *
     * @param options Options
     * @param repositories Repositories
     * @return Gradle Repositories
     */
    @NonNull
    protected List<GradleRepository> getRepositories(@NonNull Options options,
                                                     Collection<Repository> repositories) {
        BuildTool buildTool = options.buildTools().stream().filter(BuildTool::isGradle).findFirst().orElseThrow();
        return GradleRepository.listOf(buildTool.getGradleDsl()
                .orElse(GradleDsl.GROOVY), repositories);
    }

}
