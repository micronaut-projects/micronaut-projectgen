package io.micronaut.monolith.features;

import io.micronaut.projectgen.core.buildtools.gradle.Gradle;
import io.micronaut.projectgen.core.feature.DefaultFeature;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.utils.OptionUtils;
import jakarta.inject.Singleton;

import java.util.Set;

@Singleton
public class MicronautMonolithFeature implements DefaultFeature {
    private final Gradle gradle;

    public MicronautMonolithFeature(Gradle gradle) {
        this.gradle = gradle;
    }

    @Override
    public boolean shouldApply(Options options, Set<Feature> selectedFeatures) {
        return true;
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public String getName() {
        return "micronaut-monolith";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (OptionUtils.hasGradleBuildTool(featureContext.getOptions())) {
            featureContext.addFeatureIfNotPresent(Gradle.class, gradle);
        }
    }
}
