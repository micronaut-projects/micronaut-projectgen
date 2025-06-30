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
package io.micronaut.projectgen.openrewrite;

import io.micronaut.context.exceptions.ConfigurationException;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.buildtools.BuildPlugin;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.buildtools.gradle.GradlePlugin;
import io.micronaut.projectgen.core.buildtools.maven.MavenPlugin;
import io.micronaut.projectgen.core.openrewrite.FileContents;
import io.micronaut.projectgen.core.openrewrite.RecipeFetcher;
import jakarta.inject.Singleton;
import org.openrewrite.Recipe;
import org.openrewrite.RecipeException;
import org.openrewrite.config.DeclarativeRecipe;
import org.openrewrite.config.Environment;
import org.openrewrite.maven.AddAnnotationProcessor;
import org.openrewrite.properties.AddProperty;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;
import java.util.function.Predicate;

import static io.micronaut.projectgen.openrewrite.RecipeUtils.resolveRecipe;

/**
 * {@link io.micronaut.context.annotation.DefaultImplementation} of {@link RecipeFetcher}.
 * <a href="https://docs.openrewrite.org/recipes/java/dependencies/adddependency">Add Gradle or Maven dependency</a>
 * <a href="https://docs.openrewrite.org/recipes/maven/adddependency">Add Maven Dependency</a>
 * <a href="https://docs.openrewrite.org/recipes/gradle/adddependency">Add Gradle dependency</a>
 */
@Singleton
public class DefaultRecipeFetcher implements RecipeFetcher {
    private static final String PRECONDITION_FIND_BOOTSTRAP_PROPERTIES = "io.micronaut.starter.openrewrite.recipes.FindBootstrapProperties";
    private static final String PRECONDITION_FIND_APPLICATION_PROPERTIES = "io.micronaut.starter.openrewrite.recipes.FindApplicationProperties";
    private static final String PRECONDITION_FIND_DEV_PROPERTIES = "io.micronaut.starter.openrewrite.recipes.FindDevProperties";
    private final Environment env;

    /**
     *
     * @param env OpenRewrite environment
     */
    public DefaultRecipeFetcher(Environment env) {
        this.env = env;
    }

    @Override
    @NonNull
    public List<FileContents> findAllFilesByRecipeName(@NonNull String recipeName) {
        try {
            var recipe = env.activateRecipes(recipeName);
            return findAllFilesContents(recipe);
        } catch (RecipeException e) {
            //throw new ConfigurationException("Error activating recipe: " + recipeName, e);
            return Collections.emptyList();
        }
    }

    @Override
    @NonNull
    public List<Dependency> findAllByRecipeNameAndBuildTool(@NonNull String recipeName, @NonNull BuildTool buildTool) {
        try {
            var recipe = env.activateRecipes(recipeName);
            return findDependencies(recipe, buildTool);
        } catch (RecipeException e) {
            //throw new ConfigurationException("Error activating recipe: " + recipeName, e);
            return Collections.emptyList();
        }
    }

    @Override
    @NonNull
    public Optional<Properties> findPropertiesByRecipeName(@NonNull String recipeName) {
        try {
            var recipe = env.activateRecipes(recipeName);
            return findProperties(recipe);
        } catch (RecipeException e) {
            //throw new ConfigurationException("Error activating recipe: " + recipeName, e);
            return Optional.empty();
        }
    }

    @Override
    @NonNull
    public Optional<Properties> findBootstrapPropertiesByRecipeName(@NonNull String recipeName) {
        try {
            var recipe = env.activateRecipes(recipeName);
            return findBootstrapProperties(recipe);
        } catch (RecipeException e) {
            //throw new ConfigurationException("Error activating recipe: " + recipeName, e);
            return Optional.empty();
        }
    }

    @NonNull
    private Optional<Properties> findDevProperties(@NonNull Recipe recipe) {
        return findProperties(recipe, r -> r.getName().equals(PRECONDITION_FIND_DEV_PROPERTIES));
    }

    @Override
    @NonNull
    public Optional<Properties> findDevPropertiesByRecipeName(@NonNull String recipeName) {
        try {
            var recipe = env.activateRecipes(recipeName);
            return findDevProperties(recipe);
        } catch (RecipeException e) {
//            throw new ConfigurationException("Error activating recipe: " + recipeName, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> findFrameworkDocumentationByRecipeName(String recipeName) {
        return findLinkInAppendToTextFileRecipeByRecipeName(recipeName, link -> true);
    }

    @Override
    public Optional<String> findThirdPartyDocumentationByRecipeName(String recipeName) {
        return findLinkInAppendToTextFileRecipeByRecipeName(recipeName, link -> true);
    }

    /**
     *
     * @param recipe recipe
     * @return Dependency
     */
    @NonNull
    protected Dependency findMavenAnnotationProcessor(@NonNull AddAnnotationProcessor recipe) {
        return Dependency.builder()
                .groupId(recipe.getGroupId())
                .artifactId(recipe.getArtifactId())
                .versionProperty(recipe.getVersion())
                .annotationProcessor(false)
                .build();
    }

    private static Dependency findDependency(org.openrewrite.java.dependencies.AddDependency recipe) {
        Dependency.Builder builder = Dependency.builder()
                .groupId(recipe.getGroupId())
                .artifactId(recipe.getArtifactId());
        if (StringUtils.isNotEmpty(recipe.getVersion())) {
            builder.version(recipe.getVersion());
        }
        String scope = recipe.getScope();
        if (scope != null) {
            ofMavenScope(scope).ifPresent(builder::scope);
        }
        String configuration = recipe.getConfiguration();
        if (configuration != null) {
            ofGradleConfiguration(configuration).ifPresent(builder::scope);
        }
        return builder.build();
    }

    private List<Dependency> findDependencies(Recipe recipe, BuildTool buildTool) {
        List<Dependency> dependencies = new ArrayList<>();
        Recipe resolvedRecipe = resolveRecipe(recipe);
        if (resolvedRecipe instanceof org.openrewrite.java.dependencies.AddDependency d) {
            dependencies.add(findDependency(d));
        } else if (buildTool == BuildTool.GRADLE && resolvedRecipe instanceof org.openrewrite.gradle.AddDependency d) {
            dependencies.add(findGradleDependency(d));
        } else if (buildTool == BuildTool.MAVEN && resolvedRecipe instanceof org.openrewrite.maven.AddDependency d) {
            dependencies.add(findMavenDependency(d));
        } else if (buildTool == BuildTool.MAVEN && resolvedRecipe instanceof org.openrewrite.maven.AddAnnotationProcessor d) {
            dependencies.add(findMavenAnnotationProcessor(d));
        }
        for (Recipe r : resolvedRecipe.getRecipeList()) {
            Recipe resolvedRecipeChild = resolveRecipe(r);
            dependencies.addAll(findDependencies(resolvedRecipeChild, buildTool));
        }
        return dependencies;
    }

    private static List<FileContents> findAllFilesContents(Recipe recipe) {
        Recipe resolvedRecipe = resolveRecipe(recipe);
        List<FileContents> result = new ArrayList<>();
        if (resolvedRecipe instanceof org.openrewrite.xml.CreateXmlFile createXmlFile) {
            result.add(new FileContents(createXmlFile.getRelativeFileName(), createXmlFile.getFileContents()));
        }
        for (Recipe r : resolvedRecipe.getRecipeList()) {
            result.addAll(findAllFilesContents(r));
        }
        return result;
    }

    private static Dependency findGradleDependency(org.openrewrite.gradle.AddDependency recipe) {
        Dependency.Builder builder = Dependency.builder()
                .groupId(recipe.getGroupId())
                .artifactId(recipe.getArtifactId());
        if (StringUtils.isNotEmpty(recipe.getVersion())) {
            builder.version(recipe.getVersion());
        }
        String configuration = recipe.getConfiguration();
        if (configuration != null) {
            ofGradleConfiguration(configuration).ifPresent(builder::scope);
        }
        return builder.build();
    }

    private static Dependency findMavenDependency(org.openrewrite.maven.AddDependency recipe) {
        Dependency.Builder builder = Dependency.builder()
                .groupId(recipe.getGroupId())
                .artifactId(recipe.getArtifactId());
        if (StringUtils.isNotEmpty(recipe.getVersion())) {
            builder.version(recipe.getVersion());
        }
        String scope = recipe.getScope();
        if (scope != null) {
            ofMavenScope(scope).ifPresent(builder::scope);
        }
        return builder.build();
    }

    private static Optional<Scope> ofGradleConfiguration(String configuration) {
        if (configuration.equals("implementation")) {
            return Optional.of(Scope.COMPILE);
        } else if (configuration.equals("developmentOnly")) {
            return Optional.of(Scope.DEVELOPMENT_ONLY);
        } else if (configuration.equals("compileOnly")) {
            return Optional.of(Scope.COMPILE_ONLY);
        } else if (configuration.equals("annotationProcessor")) {
            return Optional.of(Scope.ANNOTATION_PROCESSOR);
        } else if (configuration.equals("testAnnotationProcessor")) {
            return Optional.of(Scope.TEST_ANNOTATION_PROCESSOR);
        } else if (configuration.equals("runtimeOnly")) {
            return Optional.of(Scope.RUNTIME);
        } else if (configuration.equals("testRuntimeOnly")) {
            return Optional.of(Scope.TEST_RUNTIME);
        } else if (configuration.equals("testImplementation")) {
            return Optional.of(Scope.TEST);
        }
        //TODO other configurations
        return Optional.empty();
    }

    private static Optional<Scope> ofMavenScope(String scope) {
        if (scope.equals("compile")) {
            return Optional.of(Scope.COMPILE);
        } else if (scope.equals("provided")) {
                return Optional.of(Scope.COMPILE_ONLY);
        } else if (scope.equals("runtime")) {
            return Optional.of(Scope.RUNTIME);
        } else if (scope.equals("test")) {
            return Optional.of(Scope.TEST);
        }
        //TODO other scopes
        return Optional.empty();
    }

    @NonNull
    private Optional<Properties> findBootstrapProperties(@NonNull Recipe recipe) {
        return findProperties(recipe, r -> r.getName().equals(PRECONDITION_FIND_BOOTSTRAP_PROPERTIES));
    }

    // there is no getter https://github.com/openrewrite/rewrite/pull/5222
    private List<Recipe> getPreconditions(DeclarativeRecipe declarativeRecipe) {
        try {
            Field preconditionsField = DeclarativeRecipe.class.getDeclaredField("preconditions");
            preconditionsField.setAccessible(true);
            return (List<Recipe>) preconditionsField.get(declarativeRecipe);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return Collections.emptyList();
        }
    }

    @NonNull
    private Optional<Properties> findProperties(@NonNull Recipe recipe) {
        return findProperties(recipe, r -> r.getName().equals(PRECONDITION_FIND_APPLICATION_PROPERTIES));
    }

    @NonNull
    private Optional<Properties> findProperties(@NonNull Recipe recipe, Predicate<Recipe> precondition) {
        Recipe resolvedRecipe = resolveRecipe(recipe);
        Properties properties = new Properties();
        for (Recipe r : resolvedRecipe.getRecipeList()) {
            Recipe resolvedRecipeChild = resolveRecipe(r);
            if (resolvedRecipeChild instanceof AddProperty addProperty) {
                if (resolvedRecipe instanceof DeclarativeRecipe declarativeRecipe) {
                    List<Recipe> preconditionRecipes = getPreconditions(declarativeRecipe);
                    if (preconditionRecipes.stream().anyMatch(precondition::test)) {
                        properties.put(addProperty.getProperty(), addProperty.getValue());
                    }
                }
            }
            Optional<Properties> nestedPropertiesOptional = findProperties(resolvedRecipeChild, precondition);
            if (nestedPropertiesOptional.isPresent()) {
                Properties nestedProperties = nestedPropertiesOptional.get();
                nestedProperties.forEach(properties::putIfAbsent);
            }
        }
        if (properties.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(properties);
    }

    /**
     *
     * @param recipeName recipe Name
     * @param contentFunction evaluate link
     * @return link
     */
    protected Optional<String> findLinkInAppendToTextFileRecipeByRecipeName(String recipeName, Function<String, Boolean> contentFunction) {
        try {
            var recipe = env.activateRecipes(recipeName);
            return findLinkInAppendToTextFileRecipeByRecipeName(recipe, contentFunction);
        } catch (RecipeException e) {
            throw new ConfigurationException("Error activating recipe: " + recipeName, e);
        }
    }

    private Optional<String> findLinkInAppendToTextFileRecipeByRecipeName(Recipe recipe, Function<String, Boolean> contentFunction) {
        Recipe resolvedRecipe = resolveRecipe(recipe);
        if (resolvedRecipe instanceof org.openrewrite.text.AppendToTextFile appendToTextFile) {
            String content = appendToTextFile.getContent();
            if (Boolean.TRUE.equals(contentFunction.apply(content))) {
                return Optional.of(content);
            }
        }
        for (Recipe r : resolvedRecipe.getRecipeList()) {
            Optional<String> documentation = findLinkInAppendToTextFileRecipeByRecipeName(r, contentFunction);
            if (documentation.isPresent()) {
                return documentation;
            }
        }
        return Optional.empty();
    }

    @Override
    @NonNull
    public List<BuildPlugin> findAllBuildPluginsByRecipeNameAndBuildTool(@NonNull String recipeName, @NonNull BuildTool buildTool) {
        try {
            var recipe = env.activateRecipes(recipeName);
            return findBuildPlugins(recipe, buildTool);
        } catch (RecipeException e) {
//            throw new ConfigurationException("Error activating recipe: " + recipeName, e);
            return Collections.emptyList();
        }
    }

    private List<BuildPlugin> findBuildPlugins(Recipe recipe, BuildTool buildTool) {
        List<BuildPlugin> plugins = new ArrayList<>();
        Recipe resolvedRecipe = resolveRecipe(recipe);

        if (buildTool == BuildTool.GRADLE && resolvedRecipe instanceof org.openrewrite.gradle.plugins.AddBuildPlugin addPlugin) {
            plugins.add(findGradleBuildPlugin(addPlugin));
        } else if (buildTool == BuildTool.MAVEN && resolvedRecipe instanceof org.openrewrite.maven.AddPlugin addPlugin) {
            plugins.add(findMavenBuildPlugin(addPlugin));
        }

        for (Recipe r : resolvedRecipe.getRecipeList()) {
            Recipe resolvedRecipeChild = resolveRecipe(r);
            plugins.addAll(findBuildPlugins(resolvedRecipeChild, buildTool));
        }
        return plugins;
    }

    private BuildPlugin findGradleBuildPlugin(org.openrewrite.gradle.plugins.AddBuildPlugin recipe) {
        return GradlePlugin.builder()
            .id(recipe.getPluginId())
            .build();
    }

    private BuildPlugin findMavenBuildPlugin(org.openrewrite.maven.AddPlugin recipe) {
        return MavenPlugin.builder()
            .groupId(recipe.getGroupId())
            .artifactId(recipe.getArtifactId())
            .build();
    }
}
