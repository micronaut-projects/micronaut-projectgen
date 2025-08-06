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
package io.micronaut.starter.feature.lang.java;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.feature.JavaApplicationFeature;
import io.micronaut.projectgen.core.options.GenericOptionsBuilder;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.starter.feature.ApplicationFeature;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.core.feature.LanguageFeature;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.options.Options;

import jakarta.inject.Singleton;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Represents the Java language feature for project generation.
 */
@Requires(property = "micronaut.starter.feature.java.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Java implements LanguageFeature {

    protected final List<JavaApplicationFeature> applicationFeatures;

    public Java(List<JavaApplicationFeature> applicationFeatures) {
        this.applicationFeatures = applicationFeatures;
    }

    @Override
    @NonNull
    public String getName() {
        return "java";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        processSelectedFeatures(featureContext, feature -> true);
    }

    /**
     * Processes and adds the first matching application feature to the feature context based on the provided filter.
     * <p>
     * If the feature context does not already contain an {@link ApplicationFeature}, this method searches the
     * available application features, applies the given filter, and checks if the feature supports the current
     * application type. If a matching feature is found, it is added to the feature context.
     *
     * @param featureContext the feature context to update
     * @param featureFilter a predicate used to filter features before selection
     */
    protected void processSelectedFeatures(FeatureContext featureContext, Predicate<Feature> featureFilter) {
        if (!featureContext.isPresent(ApplicationFeature.class)) {
            ApplicationType type = ApplicationType.of(featureContext.getOptions().template());
            applicationFeatures.stream()
                .filter(featureFilter)
                .filter(f -> f.supports(GenericOptionsBuilder.builder().template(type.toString()).build()))
                .findFirst()
                .ifPresent(featureContext::addFeature);
        }
    }

    @Override
    public boolean isJava() {
        return true;
    }

    @Override
    public boolean shouldApply(Options options, Set<Feature> selectedFeatures) {
        ApplicationType applicationType = ApplicationType.of(options.template());
        return options.language() == Language.JAVA && applicationType != ApplicationType.LIBRARY;
        //TODO remove defaultFeature
    }
}
