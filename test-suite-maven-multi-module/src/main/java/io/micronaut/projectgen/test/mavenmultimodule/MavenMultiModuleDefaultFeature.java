package io.micronaut.projectgen.test.mavenmultimodule;

import io.micronaut.projectgen.core.buildtools.maven.Maven;
import io.micronaut.projectgen.core.feature.DefaultFeature;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.utils.OptionUtils;
import jakarta.inject.Singleton;

import java.util.Set;

@Singleton
public class MavenMultiModuleDefaultFeature implements DefaultFeature {
    private final Maven maven;
    private final ApplicationModule applicationModule;
    private final LibraryModule libraryModule;

    public MavenMultiModuleDefaultFeature(Maven maven,
                                          ApplicationModule applicationModule,
                                          LibraryModule libraryModule) {
        this.maven = maven;
        this.applicationModule = applicationModule;
        this.libraryModule = libraryModule;
    }

    @Override
    public boolean shouldApply(Options options, Set<Feature> selectedFeatures) {
        return true;
    }

    @Override
    public String getName() {
        return "maven-multi-module";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (OptionUtils.hasMavenBuildTool(featureContext.getOptions())) {
            featureContext.addFeatureIfNotPresent(Maven.class, maven);
        }
        featureContext.addFeatureIfNotPresent(ApplicationModule.class, applicationModule);
        featureContext.addFeatureIfNotPresent(LibraryModule.class, libraryModule);
    }
}
