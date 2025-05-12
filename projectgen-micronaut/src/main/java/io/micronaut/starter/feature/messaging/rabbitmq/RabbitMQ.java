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
package io.micronaut.starter.feature.messaging.rabbitmq;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.starter.feature.database.TestContainers;
import io.micronaut.starter.feature.messaging.MessagingFeature;
import io.micronaut.starter.feature.messaging.SharedTestResourceFeature;
import io.micronaut.starter.feature.testresources.EaseTestingFeature;
import io.micronaut.starter.feature.testresources.TestResources;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

@Requires(property = "micronaut.starter.feature.rabbitmq.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class RabbitMQ extends EaseTestingFeature implements MessagingFeature, SharedTestResourceFeature, OpenRewriteFeature {

    public static final String NAME = "rabbitmq";

    public RabbitMQ(TestContainers testContainers, TestResources testResources) {
        super(testContainers, testResources);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "RabbitMQ Messaging";
    }

    @Override
    public String getDescription() {
        return "Adds support for RabbitMQ messaging";
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        List<String> recipes = new ArrayList<>();
        if (!generatorContext.isFeaturePresent(TestResources.class)) {
            recipes.add("io.micronaut.starter.feature.test-resources-conf");
        }
        recipes.add("io.micronaut.starter.feature.rabbitmq");
        return recipes;
    }

}
