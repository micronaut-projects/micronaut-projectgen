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

import io.micronaut.projectgen.core.buildtools.gradle.Gradle;
import io.micronaut.projectgen.core.buildtools.gradle.MavenPublishGradlePlugin;
import io.micronaut.projectgen.core.buildtools.gradle.SigningGradlePlugin;
import io.micronaut.projectgen.core.buildtools.gradle.SpotlessGradlePlugin;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.core.feature.license.Apache2LicenseFeature;
import io.micronaut.projectgen.core.feature.license.LicenseFeature;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.micronaut.gradle.MicronautLibraryGradlePlugin;
import jakarta.inject.Singleton;

import java.util.Set;

@Singleton
public class ApplicationTypeLibraryFeature extends ApplicationTypeFeature {
    private final LicenseFeature license;
    private final MicronautLibraryGradlePlugin micronautLibraryGradlePlugin;
    private final MavenPublishGradlePlugin mavenPublishGradlePlugin;
    private final SigningGradlePlugin signingGradlePlugin;
    private final SpotlessGradlePlugin spotlessGradlePlugin;

    public ApplicationTypeLibraryFeature(Apache2LicenseFeature license,
                                         Gradle gradle,
                                         MicronautLibraryGradlePlugin micronautLibraryGradlePlugin,
                                         MavenPublishGradlePlugin mavenPublishGradlePlugin,
                                         SigningGradlePlugin signingGradlePlugin, SpotlessGradlePlugin spotlessGradlePlugin) {
        super(gradle);
        this.license = license;
        this.micronautLibraryGradlePlugin = micronautLibraryGradlePlugin;
        this.mavenPublishGradlePlugin = mavenPublishGradlePlugin;
        this.signingGradlePlugin = signingGradlePlugin;
        this.spotlessGradlePlugin = spotlessGradlePlugin;
    }

    @Override
    public boolean shouldApply(Options options, Set<Feature> selectedFeatures) {
        return options instanceof MicronautOptions micronautOptions && micronautOptions.applicationType() == ApplicationType.LIBRARY;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        super.processSelectedFeatures(featureContext);
        if (OptionUtils.hasGradleBuildTool(featureContext.getOptions())) {
            featureContext.addFeatureIfNotPresent(MicronautLibraryGradlePlugin.class, micronautLibraryGradlePlugin);
            featureContext.addFeatureIfNotPresent(MavenPublishGradlePlugin.class, mavenPublishGradlePlugin);
            featureContext.addFeatureIfNotPresent(SigningGradlePlugin.class, signingGradlePlugin);
            featureContext.addFeatureIfNotPresent(SpotlessGradlePlugin.class, spotlessGradlePlugin);
        }
        featureContext.addFeatureIfNotPresent(LicenseFeature.class, license);
    }

    @Override
    public String getName() {
        return "application-type-library";
    }
}
