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
package io.micronaut.starter.feature.distributedconfig;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.feature.DistributedConfigFeature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.micronaut.features.config.MicronautDistributedConfigurationFeature;
import io.micronaut.starter.feature.consul.Consul;
import jakarta.inject.Singleton;

import java.util.List;

@Requires(property = "micronaut.starter.feature.config.consul.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class DistributedConfigConsul implements DistributedConfigFeature, OpenRewriteFeature {
    private final MicronautDistributedConfigurationFeature micronautDistributedConfigurationFeature;
    private final Consul consul;

    public DistributedConfigConsul(MicronautDistributedConfigurationFeature micronautDistributedConfigurationFeature, Consul consul) {
        this.consul = consul;
        this.micronautDistributedConfigurationFeature = micronautDistributedConfigurationFeature;
    }

    @NonNull
    @Override
    public String getName() {
        return "config-consul";
    }

    @Override
    public String getTitle() {
        return "Consul Distributed Configuration";
    }

    @Override
    public String getDescription() {
        return "Adds support for Distributed Configuration with Consul";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(Consul.class)) {
            featureContext.addFeature(consul);
        }
        if (!featureContext.isPresent(MicronautDistributedConfigurationFeature.class)) {
            featureContext.addFeature(micronautDistributedConfigurationFeature);
        }
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return List.of("io.micronaut.starter.feature.config-consul");
    }
}
