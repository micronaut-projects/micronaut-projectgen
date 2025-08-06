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
package io.micronaut.starter.feature.discovery;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import jakarta.inject.Singleton;

import java.util.List;

/**
 * A {@link DiscoveryFeature} implementation that enables service discovery using Netflix Eureka.
 * <p>
 * This feature adds the necessary configuration for integrating Eureka
 * with Micronaut applications. It also ensures that {@link DiscoveryClient} is present.
 */
@Requires(property = "micronaut.starter.feature.discovery.eureka.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Eureka implements DiscoveryFeature, OpenRewriteFeature {
    private final DiscoveryClient discoveryClient;

    public Eureka(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Override
    public String getName() {
        return "discovery-eureka";
    }

    @Override
    public String getTitle() {
        return "Eureka Service Discovery";
    }

    @Override
    public String getDescription() {
        return "Adds support for Service Discovery with Eureka";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeatureIfNotPresent(DiscoveryClient.class, discoveryClient);
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return List.of("io.micronaut.starter.feature.eureka");
    }
}
