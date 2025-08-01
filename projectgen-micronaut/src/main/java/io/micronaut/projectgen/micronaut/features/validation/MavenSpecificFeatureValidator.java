/*
 * Copyright 2017-2023 original authors
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
package io.micronaut.projectgen.micronaut.features.validation;

import io.micronaut.projectgen.core.feature.FeatureValidator;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.buildtools.maven.MavenSpecificFeature;
import io.micronaut.projectgen.core.options.Options;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Set;

/**
 * Feature validator that ensures Maven-specific features are only used with Maven build tool.
 * This validator checks that features marked as Maven-specific are not selected when using other build tools.
 */
@Singleton
public class MavenSpecificFeatureValidator implements FeatureValidator {
    private static final Logger LOG = LoggerFactory.getLogger(MavenSpecificFeatureValidator.class);

    @Override
    public void validatePreProcessing(Options options, Set<Feature> features) {
    }

    @Override
    public void validatePostProcessing(Options options, Set<Feature> features) {
        Optional<Feature> featureOptional = features.stream().filter(MavenSpecificFeature.class::isInstance).findFirst();
        if (featureOptional.isPresent()) {
            LOG.info("Feature {} only supported by Maven", featureOptional.get().getName());
        }
        if (featureOptional.isPresent() && !OptionUtils.hasMavenBuildTool(options)) {
            throw new IllegalArgumentException("Feature " + featureOptional.get().getName() + " only supported by Maven");
        }
    }
}
