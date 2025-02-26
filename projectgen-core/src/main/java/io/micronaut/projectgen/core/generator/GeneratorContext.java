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

import io.micronaut.projectgen.core.template.Writable;
import io.micronaut.projectgen.core.template.markdownLink;
import com.fizzed.rocker.RockerModel;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.projectgen.core.buildtools.BuildPlugin;
import io.micronaut.projectgen.core.buildtools.BuildProperties;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.buildtools.dependencies.*;
import io.micronaut.projectgen.core.buildtools.maven.Profile;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.Features;
import io.micronaut.projectgen.core.feature.config.ApplicationConfiguration;
import io.micronaut.projectgen.core.feature.config.BootstrapConfiguration;
import io.micronaut.projectgen.core.feature.config.Configuration;
import io.micronaut.projectgen.core.openrewrite.RecipeFetcher;
import io.micronaut.projectgen.core.options.*;
import io.micronaut.projectgen.core.rocker.RockerTemplate;
import io.micronaut.projectgen.core.rocker.RockerWritable;
import io.micronaut.projectgen.core.rocker.TestRockerModelProvider;
import io.micronaut.projectgen.core.template.Template;
import io.micronaut.projectgen.core.utils.OptionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A context object used when generating projects.
 *
 * @author graemerocher
 * @since 1.0.0
 */
public class GeneratorContext implements DependencyContext {

    private final Project project;
    private final OperatingSystem operatingSystem;

    private final BuildProperties buildProperties = new BuildProperties();
    private final ApplicationConfiguration configuration = new ApplicationConfiguration();
    private final Map<String, ApplicationConfiguration> applicationEnvironmentConfiguration = new LinkedHashMap<>();
    private final Map<String, BootstrapConfiguration> bootstrapEnvironmentConfiguration = new LinkedHashMap<>();
    private final BootstrapConfiguration bootstrapConfiguration = new BootstrapConfiguration();
    private final Set<Configuration> otherConfiguration = new HashSet<>();

    private final Map<String, Template> templates = new LinkedHashMap<>();
    private final List<Writable> helpTemplates = new ArrayList<>(8);
    private final String command;
    private final Features features;
    private final Options options;
    private final CoordinateResolver coordinateResolver;
    private final DependencyContext dependencyContext;
    private final Set<Profile> profiles = new HashSet<>();
    private final Set<BuildPlugin> buildPlugins = new HashSet<>();
    private final RecipeFetcher recipeFetcher;

    public GeneratorContext(Project project,
                            String type,
                            Options options,
                            @Nullable OperatingSystem operatingSystem,
                            Set<Feature> features,
                            CoordinateResolver coordinateResolver,
                            RecipeFetcher recipeFetcher) {
        this.command = type;
        this.project = project;
        this.operatingSystem = operatingSystem;
        this.features = new Features(this, features, options);
        this.options = options;
        this.coordinateResolver = coordinateResolver;
        this.dependencyContext = new DependencyContextImpl(coordinateResolver);
        this.recipeFetcher = recipeFetcher;
    }

    /**
     * Adds a template.
     * @param name The name of the template
     * @param template The template
     */
    public void addTemplate(String name, Template template) {
        template.setUseModule(features.hasMultiProjectFeature());
        templates.put(name, template);
    }

    /**
     * Adds a template.
     * @param name The name of the template
     */
    public void removeTemplate(String name) {
        templates.remove(name);
    }

    /**
     * Adds a template which will be consolidated into a single help file.
     *
     * @param writable The template
     */
    public void addHelpTemplate(Writable writable) {
        helpTemplates.add(writable);
    }

    /**
     * Ads a Link to a single help file.
     * @param label Link's label
     * @param href Link's uri
     */
    public void addHelpLink(String label, String href) {
        addHelpTemplate(new RockerWritable(markdownLink.template(label, href)));
    }

    /**
     * @return The build properties
     */
    @NonNull public BuildProperties getBuildProperties() {
        return buildProperties;
    }

    /**
     * @return The configuration
     */
    @NonNull public ApplicationConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * @param env Environment
     * @return The configuration
     */
    @Nullable public ApplicationConfiguration getConfiguration(String env) {
        return applicationEnvironmentConfiguration.get(env);
    }

    /**
     *
     * @param env Environment
     * @return whether it has configuration for a particular environment
     */
    public boolean hasConfigurationEnvironment(@NonNull String env) {
        return applicationEnvironmentConfiguration.containsKey(env);
    }

    /**
     *
     * @param env Environment
     * @param defaultConfig Default Configuration
     * @return Application Configuration
     */
    @NonNull public ApplicationConfiguration getConfiguration(String env, ApplicationConfiguration defaultConfig) {
        return applicationEnvironmentConfiguration.computeIfAbsent(env, key -> defaultConfig);
    }

    /**
     * @param env Environment
     * @return The configuration
     */
    @Nullable public BootstrapConfiguration getBootstrapConfiguration(String env) {
        return bootstrapEnvironmentConfiguration.get(env);
    }

    /**
     *
     * @param env environment
     * @param defaultConfig Bootstrap Configuration
     * @return Bootstrap configuration
     */
    @NonNull public BootstrapConfiguration getBootstrapConfiguration(String env, BootstrapConfiguration defaultConfig) {
        return bootstrapEnvironmentConfiguration.computeIfAbsent(env, key -> defaultConfig);
    }

    /**
     * @return The bootstrap config
     */
    @NonNull public BootstrapConfiguration getBootstrapConfiguration() {
        return bootstrapConfiguration;
    }

    /**
     *
     * @param configuration Configuration
     */
    public void addConfiguration(@NonNull Configuration configuration) {
        otherConfiguration.add(configuration);
    }

    /**
     *
     * @return All Configurations
     */
    @NonNull
    public Set<Configuration> getAllConfigurations() {
        Set<Configuration> allConfigurations = new HashSet<>();
        allConfigurations.add(configuration);
        allConfigurations.add(bootstrapConfiguration);
        allConfigurations.addAll(applicationEnvironmentConfiguration.values());
        allConfigurations.addAll(bootstrapEnvironmentConfiguration.values());
        allConfigurations.addAll(otherConfiguration);
        return allConfigurations;
    }

    /**
     * @return The templates
     */
    @NonNull public Map<String, Template> getTemplates() {
        return Collections.unmodifiableMap(templates);
    }

    /**
     * @return The templates
     */
    @NonNull public List<Writable> getHelpTemplates() {
        return Collections.unmodifiableList(helpTemplates);
    }

    /**
     * @return The language
     */
    @NonNull public Language getLanguage() {
        return options.language();
    }

    @NonNull
    public Options getOptions() {
        return options;
    }

    /**
     * @return The test framework
     */
    @NonNull
    public TestFramework getTestFramework() {
        return options.testFramework();
    }

    /**
     * @return The framework
     */
    @NonNull
    public String getFramework() {
        return options.framework();
    }

    /**
     * @return The project
     */
    @NonNull public Project getProject() {
        return project;
    }

    /**
     * @return The application type
     */
    @NonNull public String getApplicationType() {
        return command;
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
        return options.javaVersion();
    }

    /**
     * @return The current OS
     */
    @Nullable public OperatingSystem getOperatingSystem() {
        return operatingSystem;
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
     * @param javaTemplate
     * @param kotlinTemplate
     * @param groovyTemplate
     * @return Rocker Model
     */
    protected RockerModel parseModel(RockerModel javaTemplate,
                                     RockerModel kotlinTemplate,
                                     RockerModel groovyTemplate) {
        switch (getLanguage()) {
            case GROOVY:
                return groovyTemplate;
            case KOTLIN:
                return kotlinTemplate;
            case JAVA:
            default:
                return javaTemplate;
        }
    }

    /**
     *
     * @param name name
     * @param path path
     * @param testRockerModelProvider testRockerModelProvider
     */
    public void addTemplate(String name, String path, TestRockerModelProvider testRockerModelProvider) {
        RockerModel rockerModel = testRockerModelProvider.findModel(getLanguage(), getTestFramework());
        if (rockerModel != null) {
            addTemplate(name, new RockerTemplate(path, rockerModel));
        }
    }

    /**
     *
     * @param templateName template name
     * @param triggerFile trigger file
     * @param javaTemplate java template
     * @param kotlinTemplate kotlin template
     * @param groovyTemplate groovy template
     */
    public void addTemplate(String templateName,
                            String triggerFile,
                            RockerModel javaTemplate,
                            RockerModel kotlinTemplate,
                            RockerModel groovyTemplate) {
        RockerModel rockerModel = parseModel(javaTemplate, kotlinTemplate, groovyTemplate);
        addTemplate(templateName, new RockerTemplate(triggerFile, rockerModel));
    }

    /**
     *
     * @param buildPlugin Build plugin
     */
    public void addBuildPlugin(BuildPlugin buildPlugin) {
        if (buildPlugin.requiresLookup()) {
            this.buildPlugins.add(buildPlugin.resolved(coordinateResolver));
        } else {
            this.buildPlugins.add(buildPlugin);
        }
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
     * @param dependency dependency
     */
    @Override
    public void addDependency(@NonNull Dependency dependency) {
        dependencyContext.addDependency(dependency);
    }

    @NonNull
    @Override
    public Collection<Dependency> getDependencies() {
        return dependencyContext.getDependencies();
    }

    /**
     *
     * @return Build plugins
     */
    public Set<BuildPlugin> getBuildPlugins() {
        return buildPlugins;
    }

    /**
     *
     * @return Module Names
     */
    public Collection<String> getModuleNames() {
        return templates.values()
            .stream()
            .map(Template::getModule)
            .filter(s -> !Template.ROOT.equals(s))
            .distinct()
            .collect(Collectors.toList());
    }

    /**
     *
     * @param profile Profile
     */
    public void addProfile(@NonNull Profile profile) {
        Optional<Profile> optionalProfile = profiles.stream().filter(it -> it.getId().equals(profile.getId())).findFirst();
        if (optionalProfile.isPresent()) {
            optionalProfile.get().addActivationProperties(profile.getActivationProperties());
            optionalProfile.get().addDependencies(profile.getDependencies());
        } else {
            profiles.add(profile);
        }

    }

    /**
     *
     * @return Profiles
     */
    @NonNull
    public Collection<Profile> getProfiles() {
        return profiles;
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
     * @param groupId Group ID
     * @param artifactId Artifact ID
     * @param scope Scope
     * @return Whether the dependency is present
     */
    public boolean hasDependencyInScope(@NonNull String groupId,
                                        @NonNull String artifactId,
                                        @NonNull Scope scope) {
        return getDependencies().stream()
            .anyMatch(dependency -> dependency.getGroupId().equals(groupId) &&
                dependency.getArtifactId().equals(artifactId)
                && dependency.getScope() == scope);
    }

    /**
     *
     * @param groupId Group ID
     * @param artifactId artifact ID
     * @return Whether the dependency is present
     */
    public boolean hasDependency(@NonNull String groupId,
                                 @NonNull String artifactId) {
        return getDependencies().stream()
            .anyMatch(dependency -> dependency.getGroupId().equals(groupId) &&
                dependency.getArtifactId().equals(artifactId));
    }

    /**
     *
     * @param groupId groupID
     * @return number of dependencies with groupId
     */
    public long countDependencies(@NonNull String groupId) {
        return getDependencies().stream()
            .filter(dependency -> dependency.getGroupId().equals(groupId))
            .count();
    }

    /**
     *
     * @param recipeName recipe Name
     */
    public void addDependenciesByRecipeName(String recipeName) {
        if (OptionUtils.hasGradleBuildTool(getOptions())) {
            for (Dependency d : recipeFetcher.findAllByRecipeNameAndBuildTool(recipeName, BuildTool.GRADLE)) {
                addDependency(d);
            }
        } else if (OptionUtils.hasMavenBuildTool(getOptions())) {
            for (Dependency d : recipeFetcher.findAllByRecipeNameAndBuildTool(recipeName, BuildTool.MAVEN)) {
                addDependency(d);
            }
        }
    }

    /**
     *
     * @param recipeName recipe Name
     */
    public void addConfigurationByRecipeName(@NonNull String recipeName) {
        Configuration config = getConfiguration();
        recipeFetcher.findPropertiesByRecipeName(recipeName).ifPresent(properties -> {
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                config.addNested(entry.getKey().toString(), entry.getValue());
            }
        });
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
