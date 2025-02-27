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
package io.micronaut.projectgen.core.generator;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.projectgen.core.buildtools.dependencies.CoordinateResolver;
import io.micronaut.projectgen.core.feature.*;
import io.micronaut.projectgen.core.io.ConsoleOutput;
import io.micronaut.projectgen.core.openrewrite.RecipeFetcher;
import io.micronaut.projectgen.core.options.OperatingSystem;
import io.micronaut.projectgen.core.options.Options;
import jakarta.inject.Singleton;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

/**
 * Context Factory.
 */
@Singleton
public class ContextFactory {

    private final FeatureValidator featureValidator;
    private final ProjectNameValidator projectNameValidator;
    private final CoordinateResolver coordinateResolver;
    @Nullable
    private final RecipeFetcher recipeFetcher;

    public ContextFactory(FeatureValidator featureValidator,
                          CoordinateResolver coordinateResolver,
                          ProjectNameValidator projectNameValidator,
                          @Nullable RecipeFetcher recipeFetcher) {
        this.featureValidator = featureValidator;
        this.coordinateResolver = coordinateResolver;
        this.projectNameValidator = projectNameValidator;
        this.recipeFetcher = recipeFetcher;
    }

    /**
     *
     * @param availableFeatures Available Features
     * @param selectedFeatures Selected Features
     * @param applicationType Application Type
     * @param options Options
     * @param operatingSystem Operating System
     * @return Feature Context
     */
    public FeatureContext createFeatureContext(AvailableFeatures availableFeatures,
                                               List<String> selectedFeatures,
                                               String applicationType,
                                               Options options,
                                               @Nullable OperatingSystem operatingSystem) {
        final Set<Feature> features = Collections.newSetFromMap(new IdentityHashMap<>(8));
        for (String name: selectedFeatures) {
            Feature feature = availableFeatures.findFeature(name, true).orElse(null);
            if (feature != null) {
                features.add(feature);
            } else {
                throw new IllegalArgumentException("The requested feature does not exist: " + name);
            }
        }
        DefaultFeature.forEach(availableFeatures.getAllFeatures(), options, features, features::add);
        featureValidator.validatePreProcessing(options, features);
        return new FeatureContext(options, features);
    }

    /**
     *
     * @param project Project
     * @param featureContext Feature Context
     * @param consoleOutput Console Output
     * @return Generator Context
     */
    public GeneratorContext createGeneratorContext(Project project,
                                                   FeatureContext featureContext,
                                                   ConsoleOutput consoleOutput) {
        if (project != null) {
            projectNameValidator.validate(project);
        }

        featureContext.processSelectedFeatures();

        Set<Feature> featureList = featureContext.getFinalFeatures(consoleOutput);

        featureValidator.validatePostProcessing(featureContext.getOptions(), featureList);

        return new GeneratorContext(project,
            featureContext.getOptions(),
            featureList,
            coordinateResolver,
            recipeFetcher);
    }

}
