package io.micronaut.projectgen.demo;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.projectgen.core.buildtools.BuildProperties;
import io.micronaut.projectgen.core.buildtools.MavenCentral;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.buildtools.gradle.GradlePlugin;
import io.micronaut.projectgen.core.buildtools.maven.MavenPlugin;
import io.micronaut.projectgen.core.feature.DefaultFeature;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.utils.OptionUtils;
import jakarta.inject.Singleton;
import io.micronaut.projectgen.core.template.StringTemplate;
import java.util.Set;

@Singleton
class Root implements DefaultFeature {

    private final GradleJavaPluginFeature gradleJavaPluginFeature;
    private final GradleApplicationPluginFeature gradleApplicationPluginFeature;
    private final JunitJupiter junitJupiter;
    private final MavenSurefirePlugin mavenSurefirePlugin;
    private final MavenJarPluginFeature mavenJarPluginFeature;
    private final SampleCode sampleCode;
    private final MavenCompilerProperties mavenCompilerProperties;

    Root(GradleJavaPluginFeature gradleJavaPluginFeature,
         GradleApplicationPluginFeature gradleApplicationPluginFeature,
         JunitJupiter junitJupiter,
         MavenSurefirePlugin mavenSurefirePlugin,
         MavenJarPluginFeature mavenJarPluginFeature,
         SampleCode sampleCode, MavenCompilerProperties mavenCompilerProperties) {
        this.gradleJavaPluginFeature = gradleJavaPluginFeature;
        this.gradleApplicationPluginFeature = gradleApplicationPluginFeature;
        this.junitJupiter = junitJupiter;
        this.mavenSurefirePlugin = mavenSurefirePlugin;
        this.mavenJarPluginFeature = mavenJarPluginFeature;
        this.sampleCode = sampleCode;
        this.mavenCompilerProperties = mavenCompilerProperties;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeature(gradleJavaPluginFeature);
        featureContext.addFeature(gradleApplicationPluginFeature);
        featureContext.addFeature(junitJupiter);
        featureContext.addFeature(mavenSurefirePlugin);
        featureContext.addFeature(mavenJarPluginFeature);
        featureContext.addFeature(sampleCode);
        featureContext.addFeature(mavenCompilerProperties);
    }

    @Override
    public boolean shouldApply(Options options, Set<Feature> selectedFeatures) {
        return true;
    }

    @Override
    public String getName() {
        return "entry-point";
    }

    @Override
    public String getDescription() {
        return "It generates a Hello World Maven and Gradle project";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        ModuleContext module = generatorContext.getRootModule();
        Options options = generatorContext.getOptions();
        populateModuleAttributes(module, options);
    }

    private void populateModuleAttributes(ModuleContext module, Options options) {
        module.moduleAttributes()
            .setCoordinate(Dependency.builder()
                .groupId(options.group())
                .artifactId(options.artifact())
                .version(options.version())
                .build());
    }
}
