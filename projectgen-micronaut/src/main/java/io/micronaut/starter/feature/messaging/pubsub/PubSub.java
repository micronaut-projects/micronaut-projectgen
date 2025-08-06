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
package io.micronaut.starter.feature.messaging.pubsub;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.starter.feature.messaging.MessagingFeature;
import jakarta.inject.Singleton;

import java.util.List;

/**
 * Google Cloud Pub/Sub messaging feature implementation.
 * <p>
 * Adds support for Google Cloud Pub/Sub real-time messaging service.
 */
@Requires(property = "micronaut.starter.feature.gcp.pubsub.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class PubSub implements MessagingFeature, OpenRewriteFeature {

    public static final String NAME = "gcp-pubsub";

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Google Cloud Pub/Sub";
    }

    @Override
    public String getDescription() {
        return "Adds support for Google Cloud Pub/Sub real-time messaging service";
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return List.of("io.micronaut.starter.feature.gcp-pubsub");
    }

}
