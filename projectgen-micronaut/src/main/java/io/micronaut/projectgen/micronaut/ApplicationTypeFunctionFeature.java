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
package io.micronaut.projectgen.micronaut;

import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.core.feature.gitignore.GitIgnore;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.features.cli.MicronautCli;
import io.micronaut.projectgen.micronaut.features.httpclient.HttpClientFeature;
import io.micronaut.projectgen.micronaut.features.httpclient.HttpClientTest;
import io.micronaut.projectgen.micronaut.features.logging.Logback;
import io.micronaut.projectgen.micronaut.features.test.MicronautTestJunit5;
import io.micronaut.projectgen.micronaut.features.test.MicronautTestSpock;
import jakarta.inject.Singleton;

import java.util.Set;

/**
 * Feature definition for Micronaut applications of type FUNCTION.
 * Adds required features and ensures correct HTTP client test setup.
 */
@Singleton
public class ApplicationTypeFunctionFeature extends ApplicationTypeFeature {
    private final HttpClientTest httpClientTest;

    /**
     * Constructs the ApplicationTypeFunctionFeature.
     *
     * @param micronautCli       The Micronaut CLI feature
     * @param micronautTestJunit5 The JUnit 5 test feature
     * @param micronautTestSpock The Spock test feature
     * @param logback            The Logback logging feature
     * @param gitIgnore          The .gitignore feature
     * @param httpClientTest     The HTTP client test feature
     */
    public ApplicationTypeFunctionFeature(
        MicronautCli micronautCli,
        MicronautTestJunit5 micronautTestJunit5,
        MicronautTestSpock micronautTestSpock,
        Logback logback,
        GitIgnore gitIgnore,
        HttpClientTest httpClientTest
    ) {
        super(micronautCli, micronautTestJunit5, micronautTestSpock, logback, gitIgnore);
        this.httpClientTest = httpClientTest;
    }

    /**
     * Determines if this feature should be applied for the selected options.
     *
     * @param options          The selected options
     * @param selectedFeatures The set of selected features
     * @return true if applicable
     */
    @Override
    public boolean shouldApply(Options options, Set<Feature> selectedFeatures) {
        ApplicationType applicationType = ApplicationType.of(options.template());
        return applicationType == ApplicationType.FUNCTION;
    }

    /**
     * Processes and adds the required features for FUNCTION application type.
     *
     * @param featureContext The feature context
     */
    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        super.processSelectedFeatures(featureContext);

        if (!featureContext.isPresent(HttpClientFeature.class)) {
            featureContext.addFeatureIfNotPresent(HttpClientFeature.class, httpClientTest);
        }
    }

    /**
     * Returns the feature name.
     *
     * @return the feature name string
     */
    @Override
    public String getName() {
        return "application-type-function";
    }
}
