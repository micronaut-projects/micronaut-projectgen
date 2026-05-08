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
package io.micronaut.projectgen.core.generator;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.dependencies.Coordinate;
import io.micronaut.projectgen.core.buildtools.dependencies.CoordinateResolver;
import io.micronaut.projectgen.core.buildtools.dependencies.LookupFailedException;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.Features;
import io.micronaut.projectgen.core.openrewrite.RecipeFetcher;
import io.micronaut.projectgen.core.options.JdkVersion;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.options.TestFramework;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * A context object used when generating projects.
 *
 * @author graemerocher
 * @since 1.0.0
 */
public class GeneratorContext {
    public static final String ROOT_PROJECT = "ROOT";
    private final Project project;
    private final Features features;
    private final Options options;
    private final CoordinateResolver coordinateResolver;
    private final RecipeFetcher recipeFetcher;
    private ModuleContext rootModule;
    private Map<String, ModuleContext> modules = new HashMap<>();

    public GeneratorContext(Project project,
                            Options options,
                            Set<Feature> features,
                            CoordinateResolver coordinateResolver,
                            RecipeFetcher recipeFetcher) {
        this.project = project;
        this.features = new Features(this, features, options);
        this.options = options;
        this.coordinateResolver = coordinateResolver;
        this.rootModule = new ModuleContext(coordinateResolver, recipeFetcher);
        this.recipeFetcher = recipeFetcher;
    }

    /**
     *
     * @return Root Module
     */
    public ModuleContext getRootModule() {
        return rootModule;
    }

    /**
     *
     * @param name Module Name
     * @return A Module
     */
    public ModuleContext getModuleByName(String name) {
        return modules.computeIfAbsent(name, k -> new ModuleContext(k, coordinateResolver, recipeFetcher));
    }

    /**
     * @return The language
     */
    @NonNull public Language getLanguage() {
        return options.language();
    }

    /**
     *
     * @return Options
     */
    @NonNull
    public Options getOptions() {
        return options;
    }

    /**
     * @return The test framework
     */
    @Deprecated(forRemoval = true)
    @NonNull
    public BuildTool getBuildTool() {
        return options.getBuildTool();
    }

    /**
     * @return The test framework
     */
    @NonNull
    public TestFramework getTestFramework() {
        return options.testFramework();
    }

    /**
     * @return The project
     */
    @NonNull public Project getProject() {
        return project;
    }

    /**
     * @return The selected features
     */
    @NonNull public Features getFeatures() {
        return features;
    }

    /**
     * @return The JDK version
     */
    @NonNull public JdkVersion getJdkVersion() {
        return options.java();
    }

    /**
     * Apply features.
     */
    public void applyFeatures() {
        List<Feature> features = new ArrayList<>(this.features.getFeatures());
        features.sort(Comparator.comparingInt(Feature::getOrder));
        for (Feature feature: features) {
            feature.apply(this);
        }
    }

    /**
     *
     * @param feature Feature is present
     * @return Whether the feature is present
     */
    public boolean isFeaturePresent(Class<? extends Feature> feature) {
        return features.isFeaturePresent(feature);
    }

    /**
     *
     * @param feature Feature is present
     * @return Whether the feature is missing
     */
    public boolean isFeatureMissing(Class<? extends Feature> feature) {
        return !features.isFeaturePresent(feature);
    }

    /**
     *
     * @param feature Feature is present
     * @return the feature
     * @param <T> feature Type
     */
    public <T extends Feature> Optional<T> getFeature(Class<T> feature) {
        return features.getFeature(feature);
    }

    /**
     *
     * @param feature Feature is present
     * @return the feature
     * @param <T> feature Type
     */
    public <T extends Feature> T getRequiredFeature(Class<T> feature) {
        return features.getRequiredFeature(feature);
    }

    /**
     *
     * @param path Path
     * @return source path
     */
    public String getSourcePath(String path) {
        return getLanguage().getSourcePath(path);
    }

    /**
     *
     * @param path Path
     * @return test path
     */
    public String getTestSourcePath(String path) {
        return getTestFramework().getSourcePath(path, getLanguage());
    }

    /**
     *
     * @param artifactId Artifact ID
     * @return The coordinate
     */
    public Coordinate resolveCoordinate(String artifactId) {
        return coordinateResolver.resolve(artifactId)
            .orElseThrow(() -> new LookupFailedException(artifactId));
    }

    /**
     *
     * @return Module Names
     */
    public Collection<String> getModuleNames() {
        return modules.keySet();
    }

    /**
     *
     * @param featureClass Feature class
     * @return Whether the feature is present
     * @param <T> feature Type
     */
    public <T extends Feature> boolean hasFeature(Class<T> featureClass) {
        return getFeature(featureClass).isPresent();
    }

    /**
     *
     * @param recipeName recipe Name
     * @return documentation
     */
    public Optional<String> findFrameworkDocumentationByRecipeName(@NonNull String recipeName) {
        return recipeFetcher.findFrameworkDocumentationByRecipeName(recipeName);
    }

    /**
     *
     * @param recipeName recipe Name
     * @return documentation
     */
    public Optional<String> findThirdPartyDocumentationByRecipeName(@NonNull String recipeName) {
        return recipeFetcher.findThirdPartyDocumentationByRecipeName(recipeName);
    }
}
