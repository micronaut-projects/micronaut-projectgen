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

import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.options.JdkVersion;
import io.micronaut.projectgen.core.options.Options;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Features container.
 */
public class Features extends ArrayList<String> {

    private final Set<Feature> featureList;
    private final List<BuildTool> buildTools;
    private final GeneratorContext context;
    private ApplicationFeature applicationFeature;
    private LanguageFeature languageFeature;
    private TestFeature testFeature;
    private final JdkVersion javaVersion;

    public Features(GeneratorContext context, Set<Feature> featureList, Options options) {
        super(featureList.stream().map(Feature::getName).collect(Collectors.toList()));
        this.featureList = featureList;
        this.context = context;
        for (Feature feature: featureList) {
            if (applicationFeature == null && feature instanceof ApplicationFeature applicationFeature1) {
                applicationFeature = applicationFeature1;
            }
            if (languageFeature == null && feature instanceof LanguageFeature languageFeature1) {
                languageFeature = languageFeature1;
            }
            if (testFeature == null && feature instanceof TestFeature testFeature1) {
                testFeature = testFeature1;
            }
        }
        this.javaVersion = options.javaVersion();
        this.buildTools = options.buildTools();
    }

    /**
     *
     * @param clazz Feature Class
     * @return Whether the feature is present
     */
    public boolean hasFeature(Class<?> clazz) {
        return getFeatures().stream().anyMatch(clazz::isInstance);
    }

    /**
     *
     * @return Whether the project has a multi-project feature
     */
    public boolean hasMultiProjectFeature() {
        return getFeatures().stream().anyMatch(MultiProjectFeature.class::isInstance);
    }

    /**
     *
     * @return The BuildTool
     */
    public List<BuildTool> buildTools() {
        return buildTools;
    }

    /**
     *
     * @return The Application Feature
     */
    public ApplicationFeature application() {
        return applicationFeature;
    }

    /**
     *
     * @return The Language
     */
    public LanguageFeature language() {
        return languageFeature;
    }

    /**
     *
     * @return The Test Feature
     */
    public TestFeature testFramework() {
        return testFeature;
    }

    /**
     *
     * @return The Features
     */
    public Set<Feature> getFeatures() {
        return featureList;
    }

    /**
     *
     * @return The Java version
     */
    public JdkVersion javaVersion() {
        return javaVersion;
    }

    /**
     * @return The main class
     */
    public Optional<String> mainClass() {
        ApplicationFeature application = application();
        if (application != null && context != null) {
            return Optional.ofNullable(application.mainClassName(context));
        }
        return Optional.empty();
    }

    /**
     *
     * @param feature Feature
     * @return Whether the feature is present
     */
    public boolean isFeaturePresent(Class<? extends Feature> feature) {
        Objects.requireNonNull(feature, "The feature class cannot be null");
        return getFeatures().stream()
            .map(Feature::getClass)
            .anyMatch(feature::isAssignableFrom);
    }

    /**
     *
     * @param feature Feature
     * @return The feature if present
     * @param <T> feature Type
     */
    public <T extends Feature> Optional<T> getFeature(Class<T> feature) {
        Objects.requireNonNull(feature, "The feature class cannot be null");
        for (Feature f : featureList) {
            if (feature.isInstance(f)) {
                return Optional.of((T) f);
            }
        }
        return Optional.empty();
    }

    /**
     *
     * @param feature The feature class
     * @return The feature if present
     * @param <T> feature Type
     * @throws IllegalStateException if the feature is not present
     */
    public <T extends Feature> T getRequiredFeature(Class<T> feature) {
        Objects.requireNonNull(feature, "The feature class cannot be null");
        for (Feature f : featureList) {
            if (feature.isInstance(f)) {
                return (T) f;
            }
        }
        throw new IllegalStateException("The required feature type %s does not exist".formatted(feature.getName()));
    }
}
