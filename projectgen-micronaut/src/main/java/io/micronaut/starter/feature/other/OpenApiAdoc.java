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
package io.micronaut.starter.feature.other;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.starter.feature.Category;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.starter.feature.server.MicronautServerDependent;
import jakarta.inject.Singleton;

import java.util.List;

@Requires(property = "micronaut.starter.feature.openapi.adoc.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class OpenApiAdoc implements OpenRewriteFeature, MicronautServerDependent {

    public static final String NAME = "openapi-adoc";

    private final OpenApi openApiFeature;

    public OpenApiAdoc(OpenApi openApiFeature) {
        this.openApiFeature = openApiFeature;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "OpenAPI Conversion to AsciiDoc";
    }

    @Override
    public String getDescription() {
        return "Adds and enables document conversion to AsciiDoc";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeatureIfNotPresent(OpenApi.class, openApiFeature);
    }

    @Override
    public boolean supports(Options options) {
        ApplicationType type = ApplicationType.of(options.template());
        return type == ApplicationType.DEFAULT;
    }

    @Override
    public String getCategory() {
        return Category.API;
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return List.of("io.micronaut.starter.feature.openapi-adoc");
    }

}
