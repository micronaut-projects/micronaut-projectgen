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
package io.micronaut.projectgen.core.feature;

import io.micronaut.projectgen.core.options.Options;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
class DefaultFeatureFetcher implements FeatureFetcher {

    private final FeaturesMapper featuresMapper;
    private final List<Feature> features;

    DefaultFeatureFetcher(FeaturesMapper featuresMapper,
                          List<Feature> features) {
        this.featuresMapper = featuresMapper;
        this.features = features;
    }

    @Override
    public List<FeatureResponse> fetch(Options options) {
        return features.stream()
            .filter(Feature::isVisible)
            .filter(f -> f.supports(options))
            .map(featuresMapper::toFeatureResponse)
            .toList();
    }
}
