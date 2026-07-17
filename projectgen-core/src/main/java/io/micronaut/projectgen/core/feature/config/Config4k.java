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
package io.micronaut.projectgen.core.feature.config;

import io.micronaut.context.annotation.Requires;
import org.jspecify.annotations.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.feature.ConfigurationFeature;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.core.feature.FeaturePhase;
import io.micronaut.projectgen.core.feature.FeaturePredicate;
import io.micronaut.projectgen.core.feature.KotlinSpecificFeature;
import io.micronaut.projectgen.core.options.ConfigurationFormat;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.template.Config4kTemplate;
import io.micronaut.projectgen.core.template.Template;
import jakarta.inject.Singleton;

import java.util.Optional;
import java.util.function.Function;

/**
 * {@link ConfigurationFeature} for Config4K.
 */
@Requires(property = "projectgen.features.config4k.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Config4k implements ConfigurationFeature, KotlinSpecificFeature {

    private static final String EXTENSION = "conf";

    @Override
    public @NonNull String getName() {
        return "config4k";
    }

    @Override
    public @NonNull String getTitle() {
        return "Config4k - Config for Kotlin";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (featureContext.getLanguage() != Language.KOTLIN) {
            featureContext.exclude(new FeaturePredicate() {
                @Override
                public boolean test(Feature feature) {
                    return feature instanceof Config4k;
                }

                @Override
                public Optional<String> getWarning() {
                    return Optional.of("config4k feature only supports Kotlin");
                }
            });
        }
    }

    @Override
    public @NonNull String getDescription() {
        return "Define configuration with config4k, a typesafe configuration format for Kotlin based on HOCON";
    }

    @Override
    public int getOrder() {
        return FeaturePhase.HIGHEST.getOrder();
    }

    @Override
    public Function<Configuration, Template> createTemplate(String module) {
        return config -> {
            String path = StringUtils.isEmpty(module)
                ? config.getFullPath(EXTENSION)
                : module + "/" + config.getFullPath(EXTENSION);
            return new Config4kTemplate(path, config);
        };
    }

    @Override
    public ConfigurationFormat configurationFormat() {
        return ConfigurationFormat.CONFIG4K;
    }
}
