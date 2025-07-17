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
package io.micronaut.starter.feature.consul;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.feature.config.Configuration;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.DistributedConfigFeature;

import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Requires(property = "micronaut.starter.feature.consul.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Consul implements OpenRewriteFeature {

    private static final String CONSUL_CONFIGURATION = "io.micronaut.starter.feature.consul-configuration";
    private static final String CONSUL_CONFIGURATION_BOOTSTRAP = "io.micronaut.starter.feature.consul-configuration-bootstrap";

    @NonNull
    @Override
    public String getName() {
        return "consul";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        List<String> recipes = new ArrayList<>();
        if(generatorContext.isFeaturePresent(DistributedConfigFeature.class)){
            recipes.add(CONSUL_CONFIGURATION_BOOTSTRAP);
        } else{
            recipes.add(CONSUL_CONFIGURATION);
        }
        return recipes;
    }

}
