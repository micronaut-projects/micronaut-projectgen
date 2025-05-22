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
package io.micronaut.projectgen.http.server.controllers;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.projectgen.core.feature.FeatureFetcher;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.http.server.OptionsBuilder;
import io.micronaut.projectgen.http.server.conf.FeaturesControllerConfiguration;

import java.util.Map;

@Requires(property = FeaturesControllerConfiguration.PREFIX + ".enabled", notEquals = StringUtils.FALSE, defaultValue = StringUtils.TRUE)
@Controller("${" + FeaturesControllerConfiguration.PREFIX + ".path:/api/v1/features}")
class FeaturesController {
    private final OptionsBuilder optionsBuilder;
    private final FeatureFetcher featureFetcher;
    FeaturesController(FeatureFetcher featureFetcher, OptionsBuilder optionsBuilder) {
        this.featureFetcher = featureFetcher;
        this.optionsBuilder = optionsBuilder;
    }

    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Post
    FeaturesResponse features(@Body Map<String, Object> form) {
        Options options = optionsBuilder.createOptions(form);
        return new FeaturesResponse(featureFetcher.fetch(options));
    }
}
