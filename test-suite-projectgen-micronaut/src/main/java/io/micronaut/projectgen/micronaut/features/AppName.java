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
package io.micronaut.projectgen.micronaut.features;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.feature.DistributedConfigFeature;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.FeaturePhase;
import io.micronaut.projectgen.core.feature.config.Configuration;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.generator.ModuleContext;
import jakarta.inject.Singleton;
import java.util.Map;

/**
 * Feature to set the application name.
 */
@Requires(property = "micronaut.starter.feature.app.name.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class AppName implements Feature {

    @Override
    public String getName() {
        return "app-name";
    }

    @Override
    public int getOrder() {
        return FeaturePhase.LOWEST.getOrder();
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        ModuleContext module = generatorContext.getRootModule();
        Configuration config = generatorContext.isFeaturePresent(DistributedConfigFeature.class)
             ? module.bootstrapConfiguration()
            : module.configuration();
        config.put("micronaut.application.name", generatorContext.getProject().getPropertyName());
    }
}
