/*
 * Copyright 2017-2021 original authors
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
package io.micronaut.starter.feature.azure;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.micronaut.features.config.MicronautDistributedConfigurationFeature;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.starter.feature.discovery.DiscoveryClient;
import io.micronaut.projectgen.core.feature.DistributedConfigFeature;
import jakarta.inject.Singleton;

import java.util.List;

/**
 * Azure Key Vault Feature.
 *
 * @author sbodvanski
 * @since 3.8.0
 */
@Requires(property = "micronaut.starter.feature.azure.key.vault.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class AzureKeyVaultFeature implements DistributedConfigFeature, OpenRewriteFeature {

    private final MicronautDistributedConfigurationFeature micronautDistributedConfigurationFeature;
    private final DiscoveryClient discoveryClient;

    public AzureKeyVaultFeature(DiscoveryClient discoveryClient,
        MicronautDistributedConfigurationFeature micronautDistributedConfigurationFeature) {
        this.micronautDistributedConfigurationFeature = micronautDistributedConfigurationFeature;
        this.discoveryClient = discoveryClient;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(DiscoveryClient.class)) {
            featureContext.addFeature(discoveryClient);
        }
        if (!featureContext.isPresent(MicronautDistributedConfigurationFeature.class)) {
            featureContext.addFeature(micronautDistributedConfigurationFeature);
        }
    }

    @Override
    public String getTitle() {
        return "Azure Key Vault";
    }

    @Override
    public String getName() {
        return "azure-key-vault";
    }

    @Override
    public String getDescription() {
        return "Adds support for Distributed Configuration with Azure Key Vault";
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return List.of("io.micronaut.starter.feature.azure-key-vault");
    }

}
