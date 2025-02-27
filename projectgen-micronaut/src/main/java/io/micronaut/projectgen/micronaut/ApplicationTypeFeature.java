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

import io.micronaut.projectgen.core.buildtools.MavenCentral;
import io.micronaut.projectgen.core.buildtools.Repository;
import io.micronaut.projectgen.core.buildtools.RequiresRepository;
import io.micronaut.projectgen.core.buildtools.gradle.Gradle;
import io.micronaut.projectgen.core.feature.DefaultFeature;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.core.utils.OptionUtils;
import java.util.List;

public abstract class ApplicationTypeFeature implements DefaultFeature, RequiresRepository {
    private final Gradle gradle;

    protected ApplicationTypeFeature(Gradle gradle) {
        this.gradle = gradle;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (OptionUtils.hasGradleBuildTool(featureContext.getOptions())) {
            featureContext.addFeatureIfNotPresent(Gradle.class, gradle);
        }
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public List<Repository> getRepositories() {
        return List.of(new MavenCentral());
    }
}
