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
package io.micronaut.starter.feature.spring;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.starter.feature.server.MicronautServerDependent;
import jakarta.inject.Singleton;

import java.util.List;

/**
 * Feature implementation that adds support for Spring MVC Controller Annotations,
 * dependent on the core Spring feature and applicable to Micronaut server applications.
 */
@Requires(property = "micronaut.starter.feature.spring.web.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class SpringWeb extends SpringFeature implements MicronautServerDependent, OpenRewriteFeature {

    public static final String NAME = "spring-web";

    public SpringWeb(Spring spring) {
        super(spring);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Spring MVC Annotations";
    }

    @Override
    public String getDescription() {
        return "Adds support for using Spring MVC Controller Annotations";
    }

    @Override
    public boolean supports(Options options) {
        ApplicationType applicationType = ApplicationType.of(options.template());
        return applicationType == ApplicationType.DEFAULT;
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return List.of("io.micronaut.starter.feature.spring-web");
    }

}
