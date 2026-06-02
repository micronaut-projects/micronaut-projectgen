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

import org.jspecify.annotations.NonNull;
import jakarta.inject.Singleton;

/**
 * Bean Mapping Utils for {@link Feature}.
 */
@Singleton
public class FeaturesMapper {
    /**
     * Maps from {@link Feature} to {@link FeatureResponse}.
     * @param feature Feature
     * @return A FeatureResponse
     */
    public @NonNull FeatureResponse toFeatureResponse(@NonNull Feature feature) {
        String description = feature.getDescription();
        return new FeatureResponse(feature.getName(),
            feature.getTitle(),
            description.isEmpty() ? null : description,
            feature.isPreview(),
            feature.isCommunity());
    }
}
