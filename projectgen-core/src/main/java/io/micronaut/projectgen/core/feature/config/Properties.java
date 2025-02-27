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
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.feature.ConfigurationFeature;

import io.micronaut.projectgen.core.feature.FeaturePhase;
import io.micronaut.projectgen.core.template.PropertiesTemplate;
import io.micronaut.projectgen.core.template.Template;
import jakarta.inject.Singleton;
import java.util.function.Function;

/**
 * {@link ConfigurationFeature} for Properties.
 */
@Requires(property = "micronaut.starter.feature.properties.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Properties implements ConfigurationFeature {

    public static final String NAME = "properties";
    private static final String EXTENSION = "properties";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Java Properties Configuration";
    }

    @Override
    public String getDescription() {
        return "Creates a properties configuration file";
    }

    @Override
    public int getOrder() {
        return FeaturePhase.HIGHEST.getOrder();
    }

    @Override
    public Function<Configuration, Template> createTemplate() {
        return config -> new PropertiesTemplate(config.getFullPath(EXTENSION), config);
    }
}
