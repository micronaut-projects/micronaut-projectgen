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
package io.micronaut.starter.feature.picocli;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.feature.DefaultFeature;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.options.Options;

import jakarta.inject.Singleton;

import java.util.List;
import java.util.Set;

/**
 * Core feature for enabling Picocli support in CLI applications.
 *
 * <p>Applies automatically for CLI application types and adds OpenRewrite recipe support.</p>
 */
@Requires(property = "micronaut.starter.feature.picocli.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Picocli implements DefaultFeature, OpenRewriteFeature {

    @Override
    public String getName() {
        return "picocli";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public boolean shouldApply(Options options, Set<Feature> selectedFeatures) {
        ApplicationType applicationType = ApplicationType.of(options.template());
        return applicationType == ApplicationType.CLI;
    }

    @Override
    public String getTitle() {
        return "PicoCLI";
    }

    @Override
    public String getDescription() {
        return "Support for creating PicoCLI applications";
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return List.of("io.micronaut.starter.feature.picocli");
    }

    @Override
    public boolean supports(Options options) {
        ApplicationType applicationType = ApplicationType.of(options.template());
        return applicationType == ApplicationType.CLI;
    }
}
