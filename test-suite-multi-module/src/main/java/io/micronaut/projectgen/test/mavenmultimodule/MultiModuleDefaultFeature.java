package io.micronaut.projectgen.test.mavenmultimodule;

import io.micronaut.projectgen.core.buildtools.gradle.Gradle;
import io.micronaut.projectgen.core.buildtools.maven.Maven;
import io.micronaut.projectgen.core.feature.ConfigurationFeature;
import io.micronaut.projectgen.core.feature.DefaultFeature;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.core.feature.config.Properties;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.utils.OptionUtils;
import jakarta.inject.Singleton;

import java.util.Set;

@Singleton
public class MultiModuleDefaultFeature implements DefaultFeature {
    private final Maven maven;
    private final ApplicationModule applicationModule;
    private final LibraryModule libraryModule;
    private final RootModule rootModule;
    private final Properties properties;

    public MultiModuleDefaultFeature(Maven maven,
                                     ApplicationModule applicationModule,
                                     LibraryModule libraryModule,
                                     RootModule rootModule,
                                     Properties properties) {
        this.maven = maven;
        this.applicationModule = applicationModule;
        this.libraryModule = libraryModule;
        this.rootModule = rootModule;
        this.properties = properties;
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
        featureContext.addFeatureIfNotPresent(ConfigurationFeature.class, properties);
        if (OptionUtils.hasMavenBuildTool(featureContext.getOptions())) {
            featureContext.addFeatureIfNotPresent(Maven.class, maven);
        }
        featureContext.addFeatureIfNotPresent(ApplicationModule.class, applicationModule);
        featureContext.addFeatureIfNotPresent(LibraryModule.class, libraryModule);
        featureContext.addFeatureIfNotPresent(RootModule.class, rootModule);
    }
}
