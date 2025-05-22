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

import com.fizzed.rocker.RockerModel;
import io.micronaut.context.env.Environment;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.buildtools.BuildPlugin;
import io.micronaut.projectgen.core.buildtools.BuildProperties;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.Repository;
import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.buildtools.dependencies.CoordinateResolver;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.buildtools.dependencies.DependencyContext;
import io.micronaut.projectgen.core.buildtools.dependencies.DependencyContextImpl;
import io.micronaut.projectgen.core.buildtools.maven.Profile;
import io.micronaut.projectgen.core.feature.config.ApplicationConfiguration;
import io.micronaut.projectgen.core.feature.config.BootstrapConfiguration;
import io.micronaut.projectgen.core.feature.config.Configuration;
import io.micronaut.projectgen.core.openrewrite.FileContents;
import io.micronaut.projectgen.core.openrewrite.RecipeFetcher;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.rocker.RockerTemplate;
import io.micronaut.projectgen.core.rocker.RockerWritable;
import io.micronaut.projectgen.core.rocker.TestRockerModelProvider;
import io.micronaut.projectgen.core.template.StringTemplate;
import io.micronaut.projectgen.core.template.Template;
import io.micronaut.projectgen.core.template.Writable;
import io.micronaut.projectgen.core.template.markdownLink;
import io.micronaut.projectgen.core.utils.OptionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public record ModuleContext(CoordinateResolver coordinateResolver,
                            RecipeFetcher recipeFetcher,
                            ModuleAttributes moduleAttributes,
                            BuildProperties buildProperties,
                            ApplicationConfiguration configuration,
                            Map<String, ApplicationConfiguration> configurationByEnvironment,
                            BootstrapConfiguration bootstrapConfiguration,
                            Map<String, BootstrapConfiguration> bootstrapConfigurationByEnvironment,
                            DependencyContext dependencyContext,
                            Set<BuildPlugin> buildPlugins,
                            Map<String, Template> templates,
                            Set<Profile> profiles,
                            Set<Repository> repositories,
                            List<Writable> helpTemplates) {

    public ModuleContext(CoordinateResolver coordinateResolver,
                         RecipeFetcher recipeFetcher,
                         ModuleAttributes moduleAttributes,
                         BuildProperties buildProperties,
                         ApplicationConfiguration configuration,
                         Map<String, ApplicationConfiguration> configurationByEnvironment,
                         BootstrapConfiguration bootstrapConfiguration,
                         Map<String, BootstrapConfiguration> bootstrapConfigurationByEnvironment,
                         DependencyContext dependencyContext,
                         Set<BuildPlugin> buildPlugins,
                         Map<String, Template> templates,
                         Set<Profile> profiles,
                         Set<Repository> repositories,
                         List<Writable> helpTemplates) {
        this.coordinateResolver = coordinateResolver;
        this.recipeFetcher = recipeFetcher;
        this.moduleAttributes = moduleAttributes;
        this.buildProperties = buildProperties;
        this.configuration = configuration;
        this.configurationByEnvironment = configurationByEnvironment;
        this.bootstrapConfiguration = bootstrapConfiguration;
        this.bootstrapConfigurationByEnvironment = bootstrapConfigurationByEnvironment;
        this.dependencyContext = dependencyContext;
        this.buildPlugins = buildPlugins;
        this.templates = templates;
        this.profiles = profiles;
        this.repositories = repositories;
        this.helpTemplates = helpTemplates;
    }

    public ModuleContext(CoordinateResolver coordinateResolver, RecipeFetcher recipeFetcher) {
        this(null, coordinateResolver, recipeFetcher);
    }

    public ModuleContext(String name, CoordinateResolver coordinateResolver, RecipeFetcher recipeFetcher) {
        this(coordinateResolver,
            recipeFetcher,
            new ModuleAttributes(),
            new BuildProperties(),
            new ApplicationConfiguration(),
            new HashMap<>(),
            new BootstrapConfiguration(),
            new HashMap<>(),
            new DependencyContextImpl(coordinateResolver),
            new HashSet<>(),
            new LinkedHashMap<>(),
            new HashSet<>(),
            new HashSet<>(),
            new ArrayList<>());
        this.moduleAttributes.setName(name);
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

    public void addDependency(@NonNull Dependency dependency) {
        dependencyContext.addDependency(dependency);
    }

    @NonNull
    public Collection<Dependency> getDependencies() {
        return dependencyContext.getDependencies();
    }

    /**
     *
     * @return Build plugins
     */
    @NonNull
    public Set<BuildPlugin> getBuildPlugins() {
        return buildPlugins;
    }

    public void addBuildPlugin(@NonNull BuildPlugin buildPlugin) {
        if (buildPlugin.requiresLookup()) {
            buildPlugins.add(buildPlugin.resolved(coordinateResolver));
        } else {
            buildPlugins.add(buildPlugin);
        }
    }

    /**
     * @return All Configurations
     */
    @NonNull
    public Set<Configuration> getConfigurations() {
        Set<Configuration> allConfigurations = new HashSet<>();
        allConfigurations.add(configuration);
        allConfigurations.addAll(configurationByEnvironment.values());
        allConfigurations.add(bootstrapConfiguration);
        allConfigurations.addAll(bootstrapConfigurationByEnvironment.values());
        return allConfigurations;
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
     * @param recipeName recipe Name
     */
    public void addDependenciesByRecipeName(Options options, String recipeName) {
        List<Dependency> dependencies = new ArrayList<>();
        if (OptionUtils.hasMavenBuildTool(options)) {
            for (Dependency d : recipeFetcher.findAllByRecipeNameAndBuildTool(recipeName, BuildTool.MAVEN)) {
                dependencies.add(d);
            }
        }
        if (OptionUtils.hasGradleBuildTool(options)) {
            for (Dependency d : recipeFetcher.findAllByRecipeNameAndBuildTool(recipeName, BuildTool.GRADLE)) {
                if (dependencies.stream().noneMatch(dep ->
                    dep.getGroupId().equals(d.getGroupId()) &&
                        dep.getArtifactId().equals(d.getArtifactId()) &&
                        dep.getScope().equals(d.getScope())
                )) {
                    dependencies.add(d);
                }
            }
        }
        for (Dependency d : dependencies) {
            addDependency(d);
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

    private Configuration getConfiguration() {
        return configuration;
    }

    private BootstrapConfiguration getBootstrapConfiguration() {
        return bootstrapConfiguration;
    }

    /**
     *
     * @param recipeName recipe Name
     */
    public void addBootstrapConfigurationByRecipeName(@NonNull String recipeName) {
        Configuration config = getBootstrapConfiguration();
        recipeFetcher.findBootstrapPropertiesByRecipeName(recipeName).ifPresent(properties -> {
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                config.addNested(entry.getKey().toString(), entry.getValue());
            }
        });
    }

    /**
     * Adds a template.
     * @param name The name of the template
     * @param template The template
     */
    public void addTemplate(String name, Template template) {
        templates.put(name, template);
    }

    public void addTemplate(String templateName, String path, RockerModel rockerModel) {
        addTemplate(templateName, new RockerTemplate(
            StringUtils.isEmpty(moduleAttributes().getName())
                ? path
                : moduleAttributes().getName() + "/" + path, rockerModel));
    }



    /**
     *
     * @param name name
     * @param path path
     * @param testRockerModelProvider testRockerModelProvider
     */
    public void addTemplate(Options options, String name, String path, TestRockerModelProvider testRockerModelProvider) {
        RockerModel rockerModel = testRockerModelProvider.findModel(options.language(), options.testFramework());
        if (rockerModel != null) {
            addTemplate(name, new RockerTemplate(path, rockerModel));
        }
    }

    /**
     *
     * @param javaTemplate
     * @param kotlinTemplate
     * @param groovyTemplate
     * @return Rocker Model
     */
    private RockerModel parseModel(Language language,
                                     RockerModel javaTemplate,
                                     RockerModel kotlinTemplate,
                                     RockerModel groovyTemplate) {
        switch (language) {
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
     * @param templateName template name
     * @param triggerFile trigger file
     * @param javaTemplate java template
     * @param kotlinTemplate kotlin template
     * @param groovyTemplate groovy template
     */
    public void addTemplate(Language language,
                            String templateName,
                            String triggerFile,
                            RockerModel javaTemplate,
                            RockerModel kotlinTemplate,
                            RockerModel groovyTemplate) {
        RockerModel rockerModel = parseModel(language, javaTemplate, kotlinTemplate, groovyTemplate);
        addTemplate(templateName, new RockerTemplate(triggerFile, rockerModel));
    }

    public void addTemplatesByRecipeName(String recipeName) {
        for (FileContents fileContents : recipeFetcher.findAllFilesByRecipeName(recipeName)) {
            addTemplate(fileContents.relativeFileName(), new StringTemplate(fileContents.relativeFileName(), fileContents.fileContents()));
        }
    }

    /**
     * Adds a template.
     * @param name The name of the template
     */
    public void removeTemplate(String name) {
        templates.remove(name);
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

    public void addDependency(Dependency.Builder dependencyBuilder) {
        dependencyContext.addDependency(dependencyBuilder);
    }

    public Configuration getConfigurationByEnvironmentOrDefaultConfig(String env, ApplicationConfiguration defaultConfig) {
        return configurationByEnvironment.computeIfAbsent(env, key -> defaultConfig);
    }

    public Configuration getConfigurationByEnvironment(String env) {
        return getConfigurationByEnvironmentOrDefaultConfig(env, new ApplicationConfiguration(env));
    }

    public Configuration getBootstrapConfigurationByEnvironment(String env) {
        return getBootstrapConfigurationByEnvironmentOrDefaultConfig(env, new BootstrapConfiguration(env));
    }

    public Configuration getBootstrapConfigurationByEnvironmentOrDefaultConfig(String env, BootstrapConfiguration defaultConfig) {
        return bootstrapConfigurationByEnvironment.computeIfAbsent(env, key -> defaultConfig);
    }

    public Configuration testConfiguration() {
        return getConfigurationByEnvironmentOrDefaultConfig(Environment.TEST, ApplicationConfiguration.testConfig());
    }

    public Configuration devConfiguration() {
        return getConfigurationByEnvironmentOrDefaultConfig(Environment.DEVELOPMENT, ApplicationConfiguration.devConfig());
    }

    public boolean hasConfigurationByEnvironment(String env) {
        return configurationByEnvironment.containsKey(env);
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

    public Configuration devBootstrapConfiguration() {
        return getBootstrapConfigurationByEnvironment(Environment.DEVELOPMENT);
    }
}
