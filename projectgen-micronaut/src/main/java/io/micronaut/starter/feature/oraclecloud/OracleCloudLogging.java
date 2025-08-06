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
package io.micronaut.starter.feature.oraclecloud;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.micronaut.template.function.oraclefunction.OracleCloudFeature;
import jakarta.inject.Singleton;

import java.util.List;

import static io.micronaut.starter.feature.Category.LOGGING;

/**
 * Feature for integration with the Oracle Cloud Logging service.
 */
@Requires(property = "micronaut.starter.feature.oracle.cloud.logging.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class OracleCloudLogging implements OracleCloudFeature, OpenRewriteFeature {
    public static final String NAME = "oracle-cloud-logging";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Oracle Cloud Logging";
    }

    @Override
    public String getDescription() {
        return "Provides integration with the Oracle Cloud Logging service";
    }

    @Override
    public String getCategory() {
        return LOGGING;
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return List.of("io.micronaut.starter.feature.oracle-cloud-logging");
    }

}
