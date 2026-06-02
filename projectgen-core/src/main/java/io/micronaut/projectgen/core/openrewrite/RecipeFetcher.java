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
package io.micronaut.projectgen.core.openrewrite;

import org.jspecify.annotations.NonNull;
import io.micronaut.projectgen.core.buildtools.BuildPlugin;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

/**
 * Utility class to interact with OpenRewrite recipes.
 */
public interface RecipeFetcher {
    RecipeFetcher NOOP = new RecipeFetcher() {
        @Override
        public Optional<String> findFrameworkDocumentationByRecipeName(String recipeName) {
            return Optional.empty();
        }

        @Override
        public Optional<String> findThirdPartyDocumentationByRecipeName(String recipeName) {
            return Optional.empty();
        }

        @Override
        public List<Dependency> findAllByRecipeNameAndBuildTool(@NonNull String recipe, @NonNull BuildTool buildTool) {
            return List.of();
        }

        @Override
        public Optional<Properties> findPropertiesByRecipeName(@NonNull String recipe) {
            return Optional.empty();
        }

        @Override
        public List<FileContents> findAllFilesByRecipeName(@NonNull String recipe) {
            return List.of();
        }

        @Override
        public Optional<Properties> findBootstrapPropertiesByRecipeName(@NonNull String recipeName) {
            return Optional.empty();
        }

        @Override
        public Optional<Properties> findDevPropertiesByRecipeName(@NonNull String recipeName) {
            return Optional.empty();
        }

        @Override
        public List<BuildPlugin> findAllBuildPluginsByRecipeNameAndBuildTool(@NonNull String recipeName, @NonNull BuildTool buildTool) {
            return List.of();
        }

        @Override
        public Optional<Properties> findMavenBuildPropertiesByRecipeName(@NonNull String recipeName) {
            return Optional.empty();
        }
    };

    Optional<String> findFrameworkDocumentationByRecipeName(String recipeName);

    Optional<String> findThirdPartyDocumentationByRecipeName(String recipeName);

    List<Dependency> findAllByRecipeNameAndBuildTool(@NonNull String recipe, @NonNull BuildTool buildTool);

    Optional<Properties> findPropertiesByRecipeName(@NonNull String recipe);

    List<FileContents> findAllFilesByRecipeName(@NonNull String recipe);

    Optional<Properties> findBootstrapPropertiesByRecipeName(@NonNull String recipeName);

    Optional<Properties> findDevPropertiesByRecipeName(@NonNull String recipeName);

    List<BuildPlugin> findAllBuildPluginsByRecipeNameAndBuildTool(@NonNull String recipeName, @NonNull BuildTool buildTool);

    Optional<Properties> findMavenBuildPropertiesByRecipeName(@NonNull String recipeName);

}
