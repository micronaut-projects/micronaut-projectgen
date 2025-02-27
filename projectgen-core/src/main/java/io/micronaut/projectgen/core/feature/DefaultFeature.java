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
package io.micronaut.projectgen.core.feature;

import io.micronaut.core.order.OrderUtil;
import io.micronaut.projectgen.core.options.Options;

import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * A default feature is one that should be applied to a
 * project conditionally without being explicitly selected.
 * If a feature must be chosen by the user in order to be applied,
 * then the feature is not a default feature.
 *
 * @author James Kleeh
 * @since 2.0.0
 */
public interface DefaultFeature extends Feature {

    /**
     * Determines if the feature should be applied to the project.
     *
     * Default features do not need to be concerned if the feature was already
     * selected and therefore is already in the list of selected features. The
     * addition to the set is based on the identity of the feature instance and
     * all features are singletons.
     *
     * @param options          The options
     * @param selectedFeatures The features manually selected by the user
     * @return True if the feature should apply
     */
    boolean shouldApply(Options options,
                        Set<Feature> selectedFeatures);

    static void forEach(Stream<Feature> featureStream, Options options, Set<Feature> features, Consumer<Feature> featureConsumer) {
        featureStream
            .filter(DefaultFeature.class::isInstance)
            .sorted(OrderUtil.COMPARATOR.reversed())
            .filter(f -> ((DefaultFeature) f).shouldApply(options, features))
            .forEach(featureConsumer);
    }
}
