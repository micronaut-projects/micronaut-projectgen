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
package io.micronaut.starter.feature.micrometer;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.projectgen.core.feature.Feature;

import io.micronaut.starter.feature.database.r2dbc.R2dbc;
import io.micronaut.starter.feature.database.r2dbc.R2dbcFeature;
import io.micronaut.starter.feature.database.r2dbc.R2dbcPool;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

@Requires(property = "micronaut.starter.feature.micrometer.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Core implements OpenRewriteFeature {
    private final R2dbcPool r2dbcPool;

    public Core(R2dbcPool r2dbcPool) {
        this.r2dbcPool = r2dbcPool;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (featureContext.isPresent(R2dbcFeature.class)) {
            featureContext.addFeature(r2dbcPool);
        }
    }

    @Override
    public String getName() {
        return "micrometer";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public String getTitle() {
        return "Micrometer Core";
    }

    @Override
    public String getDescription() {
        return "Adds Micronaut Micrometer core dependency";
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return List.of("io.micronaut.starter.feature.micrometer-core");
    }
}
