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
package io.micronaut.starter.feature.security;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.starter.feature.ContributingInterceptUrlMapFeature;
import io.micronaut.starter.feature.InterceptUrlMap;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Map;

/**
 * Core Micronaut Security feature.
 *
 * <p>Adds a full featured and customizable security solution to the application,
 * including configuration of security intercept URL maps contributed by other features.</p>
 */
@Requires(property = "micronaut.starter.feature.security.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Security extends SecurityFeature implements OpenRewriteFeature {

    public static final String NAME = "security";

    public Security(SecurityAnnotations securityAnnotations) {
        super(securityAnnotations);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Micronaut Security";
    }

    @Override
    public String getDescription() {
        return "Adds a full featured and customizable security solution";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        OpenRewriteFeature.super.apply(generatorContext);
        addInterceptUrlMapConfiguration(generatorContext);
    }

    /**
     * Adds the security intercept URL map configuration to the root module
     * based on all contributing features.
     *
     * @param generatorContext The context of the code generator.
     */
    protected void addInterceptUrlMapConfiguration(@NonNull GeneratorContext generatorContext) {
        ModuleContext module = generatorContext.getRootModule();
        List<Map<String, String>> list = generatorContext.getFeatures().getFeatures()
            .stream()
            .filter(ContributingInterceptUrlMapFeature.class::isInstance)
            .map(f -> ((ContributingInterceptUrlMapFeature) f).interceptUrlMaps())
            .flatMap(List::stream)
            .map(InterceptUrlMap::toMap)
            .toList();
        if (CollectionUtils.isNotEmpty(list)) {
            module.configuration().put("micronaut.security.intercept-url-map", list);
        }
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return List.of("io.micronaut.starter.feature.security");
    }

}
