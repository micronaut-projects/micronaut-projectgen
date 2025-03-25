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
package io.micronaut.starter.feature.reactor;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.starter.feature.other.HttpClient;
import io.micronaut.starter.feature.reactive.ReactiveFeature;
import jakarta.inject.Singleton;

import java.util.List;

@Requires(property = "micronaut.starter.feature.reactor.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Reactor implements ReactiveFeature, OpenRewriteFeature {

    private final ReactorHttpClient reactorHttpClient;

    public Reactor(ReactorHttpClient reactorHttpClient) {
        this.reactorHttpClient = reactorHttpClient;
    }

    @NonNull
    @Override
    public String getName() {
        return "reactor";
    }

    @Override
    public String getTitle() {
        return "Reactor";
    }

    @Override
    public String getDescription() {
        return "Adds Reactive support using Project Reactor";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (featureContext.isPresent(HttpClient.class)) {
            featureContext.addFeature(reactorHttpClient);
        }
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return List.of("io.micronaut.starter.feature.reactor");
    }
}
