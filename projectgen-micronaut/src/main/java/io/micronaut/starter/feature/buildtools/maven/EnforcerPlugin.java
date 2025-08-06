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
package io.micronaut.starter.feature.buildtools.maven;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.maven.MavenPlugin;
import io.micronaut.starter.feature.Category;
import io.micronaut.projectgen.core.feature.DefaultFeature;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.options.Options;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Set;

@Requires(property = "micronaut.starter.feature.maven.enforcer.plugin.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class EnforcerPlugin implements DefaultFeature, OpenRewriteFeature {
    private static final String NAME = "maven-enforcer-plugin";

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Maven Enforcer Plugin";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds the Maven Enforcer plugin which provides goals to control certain environmental constraints such as Maven version, JDK version and OS family along with many more built-in rules and user created rules.";
    }

    @Override
    public String getCategory() {
        return Category.DEV_TOOLS;
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return List.of("io.micronaut.starter.feature.maven-enforcer-plugin");
    }

    @Override
    public boolean shouldApply(Options options, Set<Feature> selectedFeatures) {
        return OptionUtils.hasMavenBuildTool(options);
    }
}
