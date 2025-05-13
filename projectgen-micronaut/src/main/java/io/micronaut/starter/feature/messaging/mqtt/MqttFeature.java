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
package io.micronaut.starter.feature.messaging.mqtt;

import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.starter.feature.messaging.MessagingFeature;
import io.micronaut.starter.feature.testresources.TestResources;

import java.util.ArrayList;
import java.util.List;

/**
 * Sub interface for mqtt features.
 *
 * @author James Kleeh
 * @since 2.2.0
 */
public interface MqttFeature extends OpenRewriteFeature, MessagingFeature {

    @Override
    default String getDescription() {
        return "Adds support for MQTT messaging";
    }

    @Override
    default List<String> getRecipes(GeneratorContext generatorContext) {
        List<String> result = new ArrayList<>();
        if (!generatorContext.isFeaturePresent(TestResources.class)) {
            result.add("io.micronaut.starter.feature.mqtt-uri-conf");
        }
        return result;
    }
}
