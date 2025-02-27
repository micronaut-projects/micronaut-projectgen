/*
 * Copyright 2017-2025 original authors
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
package io.micronaut.projectgen.micronaut.features.serde;

import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.core.feature.JsonFeature;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import jakarta.inject.Singleton;

/**
 * Micronaut Serialization Jackson.
 */
@Singleton
public class MicronautSerdeJackson implements OpenRewriteFeature, JsonFeature {

    private final MicronautSerializationProcessor micronautSerializationProcessor;

    public MicronautSerdeJackson(MicronautSerializationProcessor micronautSerializationProcessor) {
        this.micronautSerializationProcessor = micronautSerializationProcessor;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeatureIfNotPresent(MicronautSerializationProcessor.class, micronautSerializationProcessor);
    }

    @Override
    public String getName() {
        return "serialization-jackson";
    }

    @Override
    public String getTitle() {
        return "Micronaut Serialization Jackson";
    }

    @Override
    public String getDescription() {
        return "Adds support using Micronaut Serialization with Jackson Core";
    }

    @Override
    public String getRecipeName() {
        return "io.micronaut.feature.micronaut-serde-jackson";
    }
}
