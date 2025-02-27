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

import java.util.*;
import java.util.stream.Collectors;

/**
 * A context object used when generating projects.
 *
 * @author graemerocher
 * @since 1.0.0
 */
public class GeneratorContext implements DependencyContext {
    public static final String ROOT_PROJECT = "ROOT";
    private final Project project;
    private final BuildProperties buildProperties = new BuildProperties();
    private final Map<String, ApplicationConfiguration> configuration = new LinkedHashMap<>();
    private final Map<String, Map<String, ApplicationConfiguration>> applicationEnvironmentConfiguration = new LinkedHashMap<>();
    private final Map<String, Map<String, BootstrapConfiguration>> bootstrapEnvironmentConfiguration = new LinkedHashMap<>();
    private final Map<String, BootstrapConfiguration> bootstrapConfiguration = new LinkedHashMap<>();
    private final Map<String, Set<Configuration>> otherConfiguration = new LinkedHashMap<>();

    private final Map<String, Template> templates = new LinkedHashMap<>();
    private final List<Writable> helpTemplates = new ArrayList<>(8);
    private final Features features;
    private final Options options;
    private final CoordinateResolver coordinateResolver;
    private final DependencyContext dependencyContext;
    private final Set<Profile> profiles = new HashSet<>();
    private final Map<String, Set<BuildPlugin>> buildPlugins = new LinkedHashMap<>();
    private final RecipeFetcher recipeFetcher;

    public GeneratorContext(Project project,
                            Options options,
                            Set<Feature> features,
                            CoordinateResolver coordinateResolver,
                            RecipeFetcher recipeFetcher) {
        this.project = project;
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
    @NonNull
    public ApplicationConfiguration getConfiguration() {
        return getConfigurationByModule(ROOT_PROJECT);
    }

    /**
     * @param module Module
     * @return The configuration
     */
    @NonNull
    public ApplicationConfiguration getConfigurationByModule(String module) {
        ApplicationConfiguration applicationConfiguration = configuration.get(module);
        if (applicationConfiguration == null) {
            applicationConfiguration = new ApplicationConfiguration();
            configuration.put(ROOT_PROJECT, applicationConfiguration);
        }
        return applicationConfiguration;
    }

    /**
     *
     * @param environment Environment
     * @return application configuration
     */
    @NonNull
    public ApplicationConfiguration getConfigurationByEnvironment(String environment) {
        return getConfigurationByModuleAndEnvironment(ROOT_PROJECT, environment);
    }

    /**
     * @param module module
     * @param environment Environment
     * @return application configuration
     */
    @Nullable
    public ApplicationConfiguration getConfigurationByModuleAndEnvironment(String module, String environment) {
        Map<String, ApplicationConfiguration> environmentConfigurationMap = applicationEnvironmentConfiguration
            .computeIfAbsent(module, k -> new LinkedHashMap<>());
        ApplicationConfiguration applicationConfiguration = environmentConfigurationMap.get(environment);
        if (applicationConfiguration == null) {
            applicationConfiguration = new ApplicationConfiguration();
            environmentConfigurationMap.put(environment, applicationConfiguration);
        }
        return applicationConfiguration;
    }

    /**
     *
     * @param env Environment
     * @return whether it has configuration for a particular environment
     */
    public boolean hasConfigurationByEnvironment(@NonNull String env) {
        return hasConfigurationByModuleAndEnvironment(ROOT_PROJECT, env);
    }

    /**
     *
     * @param module module
     * @param env Environment
     * @return whether it has configuration for a particular environment
     */
    public boolean hasConfigurationByModuleAndEnvironment(@NonNull String module, @NonNull String env) {
        Map<String, ApplicationConfiguration> configurationMap = applicationEnvironmentConfiguration.get(module);
        if (configurationMap == null) {
            return false;
        }
        ApplicationConfiguration applicationConfiguration = configurationMap.get(env);
        if (applicationConfiguration == null) {
            return false;
        }
        return applicationConfiguration.containsKey(env);
    }

    /**
     * @param env Environment
     * @return The configuration
     */
    @Nullable
    public BootstrapConfiguration getBootstrapConfigurationByEnvironment(String env) {
        return getBootstrapConfigurationByModuleAndEnvironment(ROOT_PROJECT, env);
    }

    /**
     * @param module Module
     * @param env Environment
     * @return The configuration
     */
    @Nullable
    public BootstrapConfiguration getBootstrapConfigurationByModuleAndEnvironment(String module, String env) {
        Map<String, BootstrapConfiguration> configurationMap = bootstrapEnvironmentConfiguration.computeIfAbsent(module, k -> new LinkedHashMap<>());
        BootstrapConfiguration bootstrapConfiguration = configurationMap.get(env);
        if (bootstrapConfiguration == null) {
            bootstrapConfiguration = new BootstrapConfiguration();
            configurationMap.put(env, bootstrapConfiguration);
        }
        return bootstrapConfiguration;
    }

    /**
     *
     * @param module Module
     * @param env Environment
     * @param defaultConfig Default Configuration
     * @return Application Configuration
     */
    @NonNull
    public ApplicationConfiguration getConfigurationByModuleEnvironmentOrDefaultConfig(String module, String env, ApplicationConfiguration defaultConfig) {
        Map<String, ApplicationConfiguration> configurationMap = applicationEnvironmentConfiguration.get(module);
        if (configurationMap == null) {
            configurationMap = new LinkedHashMap<>();
            configurationMap.put(module, defaultConfig);
            return defaultConfig;
        }
        return configurationMap.computeIfAbsent(env, key -> defaultConfig);
    }

    /**
     *
     * @param env Environment
     * @param defaultConfig Default Configuration
     * @return Application Configuration
     */
    @NonNull
    public ApplicationConfiguration getConfigurationByEnvironmentOrDefaultConfig(String env, ApplicationConfiguration defaultConfig) {
        return getConfigurationByModuleEnvironmentOrDefaultConfig(ROOT_PROJECT, env, defaultConfig);
    }

    /**
     *
     * @param env environment
     * @param defaultConfig Bootstrap Configuration
     * @return Bootstrap configuration
     */
    @NonNull
    public BootstrapConfiguration getBootstrapConfigurationByEnvironmentOrDefaultConfig(String env, BootstrapConfiguration defaultConfig) {
        return getBootstrapConfigurationByModuleAndEnvironmentOrDefaultConfig(ROOT_PROJECT, env, defaultConfig);
    }

    /**
     * @param module Module
     * @param env environment
     * @param defaultConfig Bootstrap Configuration
     * @return Bootstrap configuration
     */
    @NonNull
    public BootstrapConfiguration getBootstrapConfigurationByModuleAndEnvironmentOrDefaultConfig(String module,
                                                                                                 String env,
                                                                                                 BootstrapConfiguration defaultConfig) {
        return bootstrapEnvironmentConfiguration.computeIfAbsent(module, k -> new LinkedHashMap<>())
            .computeIfAbsent(env, key -> defaultConfig);
    }

    /**
     * @return The bootstrap config
     */
    @NonNull public BootstrapConfiguration getBootstrapConfiguration() {
        return getBootstrapConfigurationByModule(ROOT_PROJECT);
    }

    /**
     * @param module Module
     * @return The bootstrap config
     */
    @NonNull public BootstrapConfiguration getBootstrapConfigurationByModule(String module) {
        return bootstrapConfiguration.computeIfAbsent(module, k -> new BootstrapConfiguration());
    }

    /**
     *
     * @param configuration Configuration
     */
    public void addConfiguration(@NonNull Configuration configuration) {
        addConfigurationByModule(ROOT_PROJECT, configuration);
    }

    /**
     * @param module Module
     * @param configuration Configuration
     */
    public void addConfigurationByModule(String module, @NonNull Configuration configuration) {
        Set<Configuration> configurations = otherConfiguration.get(module);
        if (configurations == null) {
            configurations = new LinkedHashSet<>();
            otherConfiguration.put(module, configurations);
        }
        configurations.add(configuration);
    }

    /**
     *
     * @return All Configurations
     */
    @NonNull
    public Set<Configuration> getAllConfigurations() {
        return getAllConfigurationsByModule(ROOT_PROJECT);
    }

    /**
     * @param module Module
     * @return All Configurations
     */
    @NonNull
    public Set<Configuration> getAllConfigurationsByModule(String module) {
        Set<Configuration> allConfigurations = new HashSet<>();
        ApplicationConfiguration applicationConfiguration = configuration.get(module);
        if (applicationConfiguration != null) {
            allConfigurations.add(applicationConfiguration);
        }
        BootstrapConfiguration bootstrapConfig = bootstrapConfiguration.get(module);
        if (bootstrapConfig != null) {
            allConfigurations.add(bootstrapConfig);
        }
        Map<String, ApplicationConfiguration> environmentConfigMap = applicationEnvironmentConfiguration.get(module);
        if (environmentConfigMap != null) {
            allConfigurations.addAll(environmentConfigMap.values());
        }
        Map<String, BootstrapConfiguration> bootstrapConfigurationMap = bootstrapEnvironmentConfiguration.get(module);
        if (bootstrapConfigurationMap != null) {
            allConfigurations.addAll(bootstrapConfigurationMap.values());
        }
        Set<Configuration> configurations = otherConfiguration.get(module);
        if (configurations != null) {
            allConfigurations.addAll(configurations);
        }
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
        return options.javaVersion();
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
        addBuildPluginByModule(ROOT_PROJECT, buildPlugin);
    }

    public void addBuildPluginByModule(String module, BuildPlugin buildPlugin) {
        Set<BuildPlugin> plugins = buildPlugins.computeIfAbsent(module, k -> new HashSet<>());
        if (buildPlugin.requiresLookup()) {
            plugins.add(buildPlugin.resolved(coordinateResolver));
        } else {
            plugins.add(buildPlugin);
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
        return getBuildPluginsByModule(ROOT_PROJECT);
    }

    /**
     * @param module Module
     * @return Build plugins
     */
    public Set<BuildPlugin> getBuildPluginsByModule(String module) {
        Set<BuildPlugin> plugins = buildPlugins.get(module);
        if (plugins == null) {
            return Collections.emptySet();
        }
        return plugins;
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
