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
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.other.Management;
import jakarta.inject.Singleton;

import java.util.List;

/**
 * Micrometer feature that automates code instrumentation for Micronaut HTTP server
 * and Micronaut HTTP clients to gather traces and metrics.
 */
@Requires(property = "micronaut.starter.feature.micrometer.observation.http.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class MicrometerObservationHttp extends MicrometerFeature implements OpenRewriteFeature {
    public static final String NAME = "micrometer-observation-http";
    public static final String TITLE = "Micronaut Micrometer Observation HTTP";

    public MicrometerObservationHttp(Core core, Management management) {
        super(core, management);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    @Override
    public String getDescription() {
        return "Automates code instrumentation for Micronaut HTTP server and Micronaut HTTP clients";
    }

    @Override
    public boolean supports(Options options) {
        return ApplicationType.of(options.template()) == ApplicationType.DEFAULT;
    }

    @Override
    public String getCategory() {
        return Category.METRICS;
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return List.of("io.micronaut.starter.feature.micrometer-observation-http");
    }

}
