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

import com.fizzed.rocker.RockerModel;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.order.OrderUtil;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.BuildToolUtils;
import io.micronaut.projectgen.core.buildtools.Repository;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.rocker.RockerTemplate;
import io.micronaut.projectgen.core.template.Template;
import io.micronaut.projectgen.core.template.genericBuildGradle;
import io.micronaut.projectgen.core.utils.OptionUtils;

import java.util.Collection;
import java.util.List;

/**
 * Gradle Build Creator.
 */
public final class GradleBuildCreator {
    private static final GradleDsl DEFAULT_GRADLE_DSL = GradleDsl.KOTLIN;

    private GradleBuildCreator() {
    }

    public static final boolean DEFAULT_USER_VERSION_CATALOGUE = false;

    public static RockerTemplate buildFileTemplate(GeneratorContext generatorContext, ModuleContext moduleContext, String module) {
        GradleBuild build = create(generatorContext, moduleContext, generatorContext.getOptions());
        BuildTool buildTool = generatorContext.getOptions().buildTools().stream().filter(bt ->  bt == BuildTool.GRADLE).findFirst().orElseThrow();
        RockerModel rockerModel = genericBuildGradle.template(generatorContext.getProject(),
            build,
            generatorContext.getFeatures().mainClass().orElse(null),
            generatorContext.getOptions().version(),
            StringUtils.isNotEmpty(generatorContext.getOptions().group()) ? generatorContext.getOptions().group() : generatorContext.getProject().getPackageName());
        String buildFileName = BuildToolUtils.buildFileName(buildTool, generatorContext.getOptions().gradleDsl());
        return new RockerTemplate(StringUtils.isEmpty(module)
            ? buildFileName
            : module + "/" + buildFileName, rockerModel);
    }

    @NonNull
    public static GradleBuild create(@NonNull GeneratorContext generatorContext,
                                     @NonNull ModuleContext module,
                                     Options options) {
        return create(generatorContext, module, options, DEFAULT_USER_VERSION_CATALOGUE);
    }

    @NonNull
    public static GradleBuild create(@NonNull GeneratorContext generatorContext,
                              @NonNull ModuleContext module,
                              Options options, boolean useVersionCatalogue) {
        if (!OptionUtils.hasGradleBuildTool(generatorContext.getOptions())) {
            throw new IllegalArgumentException("GradleBuildCreator can only create Gradle builds");
        }
        GradleDsl gradleDsl = options.gradleDsl();
        if (gradleDsl == null) {
            gradleDsl = DEFAULT_GRADLE_DSL;
        }
        List<GradlePlugin> gradlePlugins = module.buildPlugins()
                .stream()
                .filter(GradlePlugin.class::isInstance)
                .map(GradlePlugin.class::cast)
                .sorted(OrderUtil.COMPARATOR)
                .toList();
        return new GradleBuild(module.moduleAttributes().getCoordinate(),
            gradleDsl,
                GradleDependency.listOf(generatorContext, module.dependencyContext(), generatorContext.getOptions(), useVersionCatalogue),
                gradlePlugins,
                getRepositories(options, module.repositories()));
    }

    /**
     *
     * @param options Options
     * @param repositories Repositories
     * @return Gradle Repositories
     */
    @NonNull
    private static List<GradleRepository> getRepositories(@NonNull Options options,
                                                     Collection<Repository> repositories) {
        BuildTool buildTool = options.buildTools().stream()
            .filter(bt -> bt == BuildTool.GRADLE).findFirst().orElseThrow();
        return GradleRepository.listOf(options.gradleDsl() == null ? DEFAULT_GRADLE_DSL : options.gradleDsl(), repositories);
    }

}
