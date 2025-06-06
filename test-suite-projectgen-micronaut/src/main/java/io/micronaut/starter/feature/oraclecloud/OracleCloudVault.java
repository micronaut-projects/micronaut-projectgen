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
package io.micronaut.starter.feature.oraclecloud;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.micronaut.features.config.MicronautDistributedConfigurationFeature;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.projectgen.core.feature.DistributedConfigFeature;
import jakarta.inject.Singleton;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Requires(property = "micronaut.starter.feature.oracle.cloud.vault.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class OracleCloudVault implements DistributedConfigFeature, OpenRewriteFeature {
    private final MicronautDistributedConfigurationFeature micronautDistributedConfigurationFeature;

    public OracleCloudVault(MicronautDistributedConfigurationFeature micronautDistributedConfigurationFeature) {
        this.micronautDistributedConfigurationFeature = micronautDistributedConfigurationFeature;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(MicronautDistributedConfigurationFeature.class)) {
            featureContext.addFeature(micronautDistributedConfigurationFeature);
        }
    }

    @NonNull
    @Override
    public String getName() {
        return "oracle-cloud-vault";
    }

    @Override
    public String getTitle() {
        return "Oracle Cloud Vault Distributed Configuration";
    }

    @NonNull
    @Override
    public String getDescription() {
        return "Adds support for Distributed Configuration with Oracle Cloud Vault";
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return List.of("io.micronaut.starter.feature.oracle-cloud-vault");
    }

}
