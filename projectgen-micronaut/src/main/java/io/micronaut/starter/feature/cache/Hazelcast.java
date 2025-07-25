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
package io.micronaut.starter.feature.cache;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import jakarta.inject.Singleton;

import java.util.List;

@Requires(property = "micronaut.starter.feature.cache.hazelcast.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Hazelcast implements CacheFeature, OpenRewriteFeature {

    @Override
    public String getName() {
        return "cache-hazelcast";
    }

    @Override
    public String getTitle() {
        return "Hazelcast Cache";
    }

    @Override
    public String getDescription() {
        return "Adds support for caching using Hazelcast";
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return List.of("io.micronaut.starter.feature.cache-hazelcast");
    }

}
