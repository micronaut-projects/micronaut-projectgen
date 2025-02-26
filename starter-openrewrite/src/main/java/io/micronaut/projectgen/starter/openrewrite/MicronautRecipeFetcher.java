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
package io.micronaut.projectgen.starter.openrewrite;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.openrewrite.RecipeFetcher;
import io.micronaut.projectgen.openrewrite.DefaultRecipeFetcher;
import jakarta.inject.Singleton;
import org.openrewrite.maven.AddAnnotationProcessor;

import java.util.Optional;
import java.util.function.Function;

/**
 * {@link RecipeFetcher} implementation for Micronaut.
 */
@Replaces(RecipeFetcher.class)
@Singleton
public class MicronautRecipeFetcher extends DefaultRecipeFetcher {
    private static final Dependency MICRONAUT_INJECT = Dependency.builder()
        .groupId("io.micronaut")
        .artifactId("micronaut-inject")
        .compile()
        .build();
    private static final Function<String, Boolean> MICRONAUT_DOCUMENTATION_LINK =
        s -> (s.startsWith("https://micronaut-projects.github.io") || s.startsWith("https://docs.micronaut.io"));
    private static final Function<String, Boolean> LINK_NOT_MICRONAUT_DOC =
        s -> s.startsWith("http") && !MICRONAUT_DOCUMENTATION_LINK.apply(s);

    public MicronautRecipeFetcher(org.openrewrite.config.Environment env) {
        super(env);
    }

    @Override
    @NonNull
    protected Dependency findMavenAnnotationProcessor(@NonNull AddAnnotationProcessor recipe) {
        return Dependency.builder()
            .groupId(recipe.getGroupId())
            .artifactId(recipe.getArtifactId())
            .exclude(MICRONAUT_INJECT)
            .versionProperty(recipe.getVersion())
            .annotationProcessor(false)
            .build();
    }

    @Override
    public Optional<String> findFrameworkDocumentationByRecipeName(String recipeName) {
        return findLinkInAppendToTextFileRecipeByRecipeName(recipeName, MICRONAUT_DOCUMENTATION_LINK);
    }

    @Override
    public Optional<String> findThirdPartyDocumentationByRecipeName(String recipeName) {
        return findLinkInAppendToTextFileRecipeByRecipeName(recipeName, LINK_NOT_MICRONAUT_DOC);
    }
}
