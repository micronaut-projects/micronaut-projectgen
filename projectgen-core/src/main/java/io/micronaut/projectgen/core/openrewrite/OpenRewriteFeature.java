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

import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.generator.GeneratorContext;

/**
 * A feature backed by an OpenRewrite recipe.
 */
public interface OpenRewriteFeature extends Feature {

    String getRecipeName();

    @Override
    default void apply(GeneratorContext generatorContext) {
        generatorContext.addDependenciesByRecipeName(getRecipeName());
        generatorContext.addConfigurationByRecipeName(getRecipeName());
    }

    @Override
    default String getFrameworkDocumentation(GeneratorContext gc) {
        if (gc == null) {
            return null;
        }
        return gc.findFrameworkDocumentationByRecipeName(getRecipeName()).orElse(null);
    }

    @Override
    default String getThirdPartyDocumentation(GeneratorContext gc) {
        if (gc == null) {
            return null;
        }
        return gc.findThirdPartyDocumentationByRecipeName(getRecipeName()).orElse(null);
    }
}
