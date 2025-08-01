/*
 * Copyright 2017-2023 original authors
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
package io.micronaut.projectgen.micronaut.features.objectstorage;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.starter.feature.Category;
import jakarta.inject.Singleton;

import java.util.List;

/**
 * Feature that provides local object storage support for Micronaut applications.
 * This feature adds a local implementation to save objects to a folder on the local filesystem,
 * useful for testing and development purposes.
 */
@Requires(property = "micronaut.starter.feature.object.storage.local.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class ObjectStorageLocal implements ObjectStorageFeature, OpenRewriteFeature {
    private static final String LOCAL = "Local";
    private static final String DESCRIPTION = " This feature adds a local implementation to save to a folder in your computer which you may want to use during testing and development.";

    @Override
    public String getCloudProvider() {
        return LOCAL;
    }

    @Override
    public String getDescription() {
        return PREAMBLE + DESCRIPTION;
    }

    @Override
    public String getCategory() {
        return Category.DEV_TOOLS;
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return List.of("io.micronaut.starter.feature.object-storage-local");
    }

}
