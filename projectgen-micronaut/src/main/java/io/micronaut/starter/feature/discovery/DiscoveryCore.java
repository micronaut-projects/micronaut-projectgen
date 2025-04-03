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
package io.micronaut.starter.feature.discovery;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.micronaut.MicronautOptions;
import io.micronaut.starter.feature.Category;
import jakarta.inject.Singleton;

import java.util.List;

@Requires(property = "micronaut.starter.feature.discovery.core.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class DiscoveryCore implements OpenRewriteFeature {
    public static final String ARTIFACT_ID_MICRONAUT_DISCOVERY_CORE = "micronaut-discovery-core";

    @NonNull
    @Override
    public String getName() {
        return "discovery-core";
    }

    @Override
    public String getTitle() {
        return "Micronaut Discovery Core";
    }

    @Override
    public String getDescription() {
        return "Adds micronaut-discovery-core dependency for base service discovery features.";
    }

    @Override
    public boolean supports(Options options) {
        return options instanceof MicronautOptions mnOptions && mnOptions.applicationType() != ApplicationType.CLI;
    }

    @Override
    public String getCategory() {
        return Category.SERVICE_DISCOVERY;
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return List.of("io.micronaut.starter.feature.discovery-core");
    }
}
