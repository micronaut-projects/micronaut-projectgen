/*
 * Copyright 2017-2022 original authors
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
package io.micronaut.starter.feature.database;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.core.feature.FeatureContext;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

/**
 * Add support for Micronaut Data Azure Cosmos.
 */
@Requires(property = "micronaut.starter.feature.data.azure.cosmos.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class DataAzureCosmosFeature implements DataDocumentFeature, OpenRewriteFeature {
    private static final String NAME = "data-azure-cosmos";
    private final Data data;

    protected DataAzureCosmosFeature(Data data) {
        this.data = data;
    }

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @NonNull
    @Override
    public String getDescription() {
        return "Adds support for defining data repositories for Azure Cosmos Db";
    }

    @Override
    public String getTitle() {
        return "Micronaut Data Azure Cosmos";
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
// NOTE: Consider adding test resources/containers support later
        List<String> recipes = new ArrayList<>();
        recipes.add("io.micronaut.starter.feature.data-azure-cosmos");
        if (OptionUtils.hasMavenBuildTool(generatorContext.getOptions())) {
            recipes.add("io.micronaut.starter.feature.data-azure-cosmos-annotation-maven");
        } else {
            recipes.add("io.micronaut.starter.feature.data-azure-cosmos-annotation-gradle");
        }
        return  recipes;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeature(data);
    }

}
