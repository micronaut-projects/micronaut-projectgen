/*
 * Copyright 2017-2024 original authors
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
package io.micronaut.projectgen.micronaut.features.langchain4j.languagemodels;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.micronaut.features.langchain4j.Langchain4jLanguageModel;
import jakarta.inject.Singleton;

import java.util.List;

/**
 * Feature that provides integration with Mistral AI's Langchain4j language model in Micronaut.
 * Adds the necessary configuration and recipes for Mistral AI language model support.
 */
@Requires(property = "micronaut.starter.feature.langchain4j.mistralai.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class MistralAiLangchain4jLanguageModel implements Langchain4jLanguageModel, OpenRewriteFeature {
    private static final String NAME = "langchain4j-mistralai";

    @Override
    public String getTitle() {
        return "Mistral AI Langchain4j";
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return List.of("io.micronaut.starter.feature.langchain4j-mistralai");
    }
}
