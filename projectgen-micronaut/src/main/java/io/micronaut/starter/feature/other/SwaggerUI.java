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
package io.micronaut.starter.feature.other;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.starter.feature.ContributingInterceptUrlMapFeature;
import io.micronaut.starter.feature.InterceptUrlMap;
import io.micronaut.starter.feature.staticResources.StaticResource;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

/**
 * Adds Swagger UI as a static OpenAPI documentation view.
 *
 * <p>Registers static resources and anonymous access mapping for the Swagger UI.</p>
 */
@Requires(property = "micronaut.starter.feature.swagger.ui.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class SwaggerUI extends OpenApiView implements ContributingInterceptUrlMapFeature, OpenRewriteFeature {
    public static final String NAME = "swagger-ui";

    public SwaggerUI(OpenApi openApiFeature) {
        super(openApiFeature);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Swagger UI";
    }

    @Override
    public String getDescription() {
        return "Adds and enables Swagger UI by default";
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return List.of("io.micronaut.starter.feature.swagger-ui-docs");
    }

    @Override
    public List<InterceptUrlMap> interceptUrlMaps() {
        List<InterceptUrlMap> result = new ArrayList<>(super.interceptUrlMaps());
        result.add(InterceptUrlMap.anonymousAcccess("/swagger-ui/**"));
        return result;
    }

    @Override
    public List<StaticResource> staticResources() {
        List<StaticResource> result = new ArrayList<>(super.staticResources());
        result.add(new StaticResource("swagger-ui", "/swagger-ui/**", "classpath:META-INF/swagger/views/swagger-ui"));
        return result;
    }
}
