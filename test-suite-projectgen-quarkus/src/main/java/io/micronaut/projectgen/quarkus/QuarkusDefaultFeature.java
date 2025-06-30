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
package io.micronaut.projectgen.quarkus;

import io.micronaut.projectgen.core.feature.DefaultFeature;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.core.feature.gitignore.GitIgnore;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.options.TestFramework;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.quarkus.features.buildtools.gradle.JavaGradlePlugin;
import io.micronaut.projectgen.quarkus.features.QuarkusArc;
import io.micronaut.projectgen.quarkus.features.QuarkusBom;
import io.micronaut.projectgen.quarkus.features.QuarkusJunit5;
import io.micronaut.projectgen.quarkus.features.QuarkusRest;
import io.micronaut.projectgen.quarkus.features.buildtools.gradle.QuarkusGradlePlugin;
import jakarta.inject.Singleton;

import java.util.Set;

@Singleton
public class QuarkusDefaultFeature implements DefaultFeature {
    private final GitIgnore gitIgnore;
    private final JavaGradlePlugin javaGradlePlugin;
    private final QuarkusGradlePlugin quarkusGradlePlugin;
    private final QuarkusArc quarkusArc;
    private final QuarkusRest quarkusRest;
    private final QuarkusJunit5 quarkusJunit5;
    private final QuarkusBom quarkusBom;

    public QuarkusDefaultFeature(GitIgnore gitIgnore,
                                 JavaGradlePlugin javaGradlePlugin,
                                 QuarkusGradlePlugin quarkusGradlePlugin,
                                 QuarkusArc quarkusArc,
                                 QuarkusRest quarkusRest,
                                 QuarkusJunit5 quarkusJunit5,
                                 QuarkusBom quarkusBom) {
        this.gitIgnore = gitIgnore;
        this.javaGradlePlugin = javaGradlePlugin;
        this.quarkusGradlePlugin = quarkusGradlePlugin;
        this.quarkusArc = quarkusArc;
        this.quarkusRest = quarkusRest;
        this.quarkusJunit5 = quarkusJunit5;
        this.quarkusBom = quarkusBom;
    }

    @Override
    public boolean shouldApply(Options options, Set<Feature> selectedFeatures) {
        return true;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeatureIfNotPresent(GitIgnore.class, gitIgnore);
        if (OptionUtils.hasGradleBuildTool(featureContext.getOptions())) {
            featureContext.addFeatureIfNotPresent(JavaGradlePlugin.class, javaGradlePlugin);
            featureContext.addFeatureIfNotPresent(QuarkusGradlePlugin.class, quarkusGradlePlugin);
        }
        if (featureContext.getOptions().testFramework() == TestFramework.JUNIT) {
            featureContext.addFeatureIfNotPresent(QuarkusJunit5.class, quarkusJunit5);
        }
        featureContext.addFeatureIfNotPresent(QuarkusArc.class, quarkusArc);
        featureContext.addFeatureIfNotPresent(QuarkusRest.class, quarkusRest);
        featureContext.addFeatureIfNotPresent(QuarkusBom.class, quarkusBom);
    }

    @Override
    public String getName() {
        return "quarkus-default-feature";
    }

    @Override
    public boolean isVisible() {
        return false;
    }
}
