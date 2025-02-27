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

import io.micronaut.context.annotation.Primary;

import io.micronaut.projectgen.core.options.Options;
import jakarta.inject.Singleton;
import java.util.List;
import java.util.Set;

/**
 * Composite pattern for {@link FeatureValidator}.
 */
@Primary
@Singleton
public class CompositeFeatureValidator implements FeatureValidator {

    private final List<FeatureValidator> featureValidators;

    public CompositeFeatureValidator(List<FeatureValidator> featureValidators) {
        this.featureValidators = featureValidators;
    }

    @Override
    public void validatePreProcessing(Options options, Set<Feature> features) {
        for (FeatureValidator featureValidator: featureValidators) {
            featureValidator.validatePreProcessing(options, features);
        }
    }

    @Override
    public void validatePostProcessing(Options options, Set<Feature> features) {
        for (FeatureValidator featureValidator: featureValidators) {
            featureValidator.validatePostProcessing(options, features);
        }
    }
}
