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
package io.micronaut.projectgen.micronaut.features.test;

import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.core.feature.TestFeature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.core.options.TestFramework;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.javalibs.test.junit.JunitJupiterApi;
import io.micronaut.projectgen.javalibs.test.junit.JunitJupiterEngine;
import jakarta.inject.Singleton;

/**
 * Micronaut Test JUnit 5.
 */
@Singleton
public class MicronautTestJunit5 implements TestFeature, OpenRewriteFeature {

    private final JunitJupiterApi junitJupiterApi;
    private final JunitJupiterEngine junitJupiterEngine;

    public MicronautTestJunit5(JunitJupiterApi junitJupiterApi,
                               JunitJupiterEngine junitJupiterEngine) {
        this.junitJupiterApi = junitJupiterApi;
        this.junitJupiterEngine = junitJupiterEngine;
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public TestFramework getTestFramework() {
        return TestFramework.JUNIT;
    }

    @Override
    public String getName() {
        return "junit";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (OptionUtils.hasMavenBuildTool(featureContext.getOptions())) {
            featureContext.addFeatureIfNotPresent(JunitJupiterApi.class, junitJupiterApi);
            featureContext.addFeatureIfNotPresent(JunitJupiterEngine.class, junitJupiterEngine);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (OptionUtils.hasMavenBuildTool(generatorContext.getOptions())) {
            OpenRewriteFeature.super.apply(generatorContext);
        }
    }

    @Override
    public String getRecipeName() {
        return "io.micronaut.starter.feature.micronaut-test-junit5";
    }
}
