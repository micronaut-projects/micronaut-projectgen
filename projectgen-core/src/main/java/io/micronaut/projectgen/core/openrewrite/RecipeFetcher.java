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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

/**
 * Utility class to interact with OpenRewrite recipes.
 */
public interface RecipeFetcher {
    Optional<String> findFrameworkDocumentationByRecipeName(String recipeName);

    Optional<String> findThirdPartyDocumentationByRecipeName(String recipeName);

    @NonNull
    List<Dependency> findAllByRecipeNameAndBuildTool(@NonNull String recipe, @NonNull BuildTool buildTool);

    @NonNull
    Optional<Properties> findPropertiesByRecipeName(@NonNull String recipe);

    @NonNull
    List<FileContents> findAllFilesByRecipeName(@NonNull String recipe);

}
