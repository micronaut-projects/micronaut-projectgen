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

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import jakarta.inject.Singleton;
import java.util.List;

@Requires(property = "micronaut.starter.feature.mqttv3.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class MqttV3 implements MqttFeature, OpenRewriteFeature {

    public static final String NAME = "mqttv3";

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "MQTT v3 Messaging";
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        List<String> result = MqttFeature.super.getRecipes(generatorContext);
        result.add("io.micronaut.starter.feature.mqttv3");
        return result;
    }
}
