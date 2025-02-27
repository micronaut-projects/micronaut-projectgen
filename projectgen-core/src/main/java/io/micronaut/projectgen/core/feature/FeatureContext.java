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

import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.io.ConsoleOutput;
import io.micronaut.projectgen.core.options.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;

/**
 * Feature Context.
 */
public class FeatureContext {

    private final Set<Feature> selectedFeatures;
    private final Options options;
    private final List<Feature> features = new ArrayList<>();
    private final List<FeaturePredicate> exclusions = new ArrayList<>();
    private ListIterator<Feature> iterator;

    public FeatureContext(Options options,
                          Set<Feature> selectedFeatures) {
        this.selectedFeatures = selectedFeatures;
        this.options = options;
    }

    /**
     * Process the selected features.
     */
    public void processSelectedFeatures() {
        features.addAll(0, selectedFeatures);
        features.sort(Comparator.comparingInt(Feature::getOrder));
        iterator = features.listIterator();
        while (iterator.hasNext()) {
            Feature feature = iterator.next();
            feature.processSelectedFeatures(this);
        }
        iterator = null;
    }

    /**
     *
     * @param exclusion Exclusion predicate
     */
    public void exclude(FeaturePredicate exclusion) {
        exclusions.add(exclusion);
    }

    /**
     *
     * @param consoleOutput Console Output
     * @return features
     */
    public Set<Feature> getFinalFeatures(ConsoleOutput consoleOutput) {
        return features.stream().filter(feature -> {
            for (FeaturePredicate predicate: exclusions) {
                if (predicate.test(feature)) {
                    predicate.getWarning().ifPresent(consoleOutput::warning);
                    return false;
                }
            }
            return true;
        }).collect(collectingAndThen(toSet(), Collections::unmodifiableSet));
    }

    /**
     *
     * @return Language
     */
    public Language getLanguage() {
        return options.language();
    }

    /**
     *
     * @return Test framework
     */
    public TestFramework getTestFramework() {
        return options.testFramework();
    }

    /**
     *
     * @return Build Tool.
     */
    public List<BuildTool> getBuildTools() {
        return options.buildTools();
    }

    /**
     *
     * @return Jdk Version
     */
    public JdkVersion getJavaVersion() {
        return options.javaVersion();
    }

    /**
     *
     * @return Options
     */
    public Options getOptions() {
        return options;
    }

    /**
     *
     * @return Selected Features
     */
    public Set<Feature> getSelectedFeatures() {
        return Collections.unmodifiableSet(selectedFeatures);
    }

    /**
     * Adds a feature to be applied. The added feature is processed immediately.
     *
     * @param feature The feature to add
     */
    public void addFeature(Feature feature) {
        if (iterator != null) {
            iterator.add(feature);
        } else {
            features.add(feature);
        }
        feature.processSelectedFeatures(this);
    }

    /**
     *
     * @param feature Feature
     * @return Whether the feature is present
     */
    public boolean isPresent(Class<? extends Feature> feature) {
        return features.stream()
            .filter(f -> exclusions.stream().noneMatch(e -> e.test(f)))
            .map(Feature::getClass)
            .anyMatch(feature::isAssignableFrom);
    }

    /**
     *
     * @param feature Feature class
     * @return Feature if present
     */
    public Optional<Feature> getFeature(Class<? extends Feature> feature) {
        return features.stream()
            .filter(f -> exclusions.stream().noneMatch(e -> e.test(f)))
            .filter(f -> feature.isAssignableFrom(f.getClass()))
            .findFirst();
    }

    /**
     *
     * @param featureClass Feature class
     * @param feature feature to add if not present
     */
    public void addFeatureIfNotPresent(Class<? extends Feature> featureClass, Feature feature) {
        if (!isPresent(featureClass)) {
            addFeature(feature);
        }
    }
}
