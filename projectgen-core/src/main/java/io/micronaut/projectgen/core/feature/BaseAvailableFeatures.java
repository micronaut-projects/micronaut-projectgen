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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Default implementation of {@link AvailableFeatures}.
 */
@Singleton
public class BaseAvailableFeatures implements AvailableFeatures {
    private final Map<String, Feature> features;

    @Inject
    public BaseAvailableFeatures(List<Feature> features) {
        this(features, f -> true);
    }

    public BaseAvailableFeatures(List<Feature> features, @Nullable Predicate<Feature> predicate) {
        this.features = features.stream()
                .filter(f -> predicate == null || predicate.test(f))
                .collect(Collectors.toMap(
                        Feature::getName,
                        Function.identity(),
                        (u, v) -> {
                            throw new IllegalArgumentException("Duplicate feature found " + u.getName());
                        },
                        LinkedHashMap::new));
    }

    @Override
    public Iterator<String> iterator() {
        return getFeatures()
                .map(Feature::getName)
                .iterator();
    }

    @Override
    public Optional<Feature> findFeature(@NonNull String name) {
        return findFeature(name, false);
    }

    @Override
    public Optional<Feature> findFeature(@NonNull String name, boolean ignoreVisibility) {
        Feature feature = features.get(name);
        if (feature != null) {
            if (ignoreVisibility || feature.isVisible()) {
                return Optional.of(feature);
            }
        }
        return Optional.empty();
    }

    @Override
    public Stream<Feature> getFeatures() {
        return getAllFeatures().filter(Feature::isVisible);
    }

    @Override
    public Stream<Feature> getAllFeatures() {
        return features.values().stream();
    }
}
