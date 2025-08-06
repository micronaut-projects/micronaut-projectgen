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
package io.micronaut.starter.feature.kotlin;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.feature.KotlinApplicationFeature;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.micronaut.features.validator.MicronautValidationFeature;
import io.micronaut.projectgen.micronaut.features.validator.ValidationFeature;
import io.micronaut.starter.feature.Category;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.core.feature.FeaturePredicate;
import io.micronaut.projectgen.core.feature.KotlinSpecificFeature;
import io.micronaut.projectgen.micronaut.template.kotlin.applicationKotlin;
import io.micronaut.projectgen.micronaut.template.kotlin.homeRouteKotlin;
import io.micronaut.projectgen.micronaut.template.kotlin.jacksonFeatureKotlin;
import io.micronaut.projectgen.micronaut.template.kotlin.nameTransformerKotlin;
import io.micronaut.projectgen.micronaut.template.kotlin.uppercaseTransformerKotlin;
import io.micronaut.starter.feature.server.ThirdPartyServerFeature;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.rocker.RockerTemplate;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;

/**
 * Feature that enables support for building Micronaut applications using Ktor as the HTTP server.
 * This feature is only applicable for Kotlin language and the default application type.
 */
@Requires(property = "micronaut.starter.feature.ktor.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Ktor implements KotlinApplicationFeature, ThirdPartyServerFeature, KotlinSpecificFeature, OpenRewriteFeature {

    public static final String NAME = "ktor";
    private final MicronautValidationFeature micronautValidationFeature;

    public Ktor(MicronautValidationFeature micronautValidationFeature) {
        this.micronautValidationFeature = micronautValidationFeature;
    }

    @Override
    public boolean supports(Options options) {
        ApplicationType type = ApplicationType.of(options.template());
        return type == ApplicationType.DEFAULT;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (featureContext.getLanguage() != Language.KOTLIN) {
            featureContext.exclude(new FeaturePredicate() {
                @Override
                public boolean test(Feature feature) {
                    return feature instanceof Ktor;
                }

                @Override
                public Optional<String> getWarning() {
                    return Optional.of("Ktor feature only supports Kotlin");
                }
            });
        }
        if (!featureContext.isPresent(ValidationFeature.class)) {
            featureContext.addFeatureIfNotPresent(ValidationFeature.class, micronautValidationFeature);
        }
    }

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return "Support for using Ktor as the server instead of Micronaut’s native HTTP server";
    }

    @Override
    public String getTitle() {
        return "Ktor";
    }

    @Override
    public String getCategory() {
        return Category.SERVER;
    }

    @Override
    @Nullable
    public String mainClassName(GeneratorContext generatorContext) {
        return generatorContext.getProject().getPackageName() + ".Application";
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        ModuleContext module = generatorContext.getRootModule();
        module.addTemplate("application", new RockerTemplate("src/main/kotlin/{packagePath}/Application.kt", applicationKotlin.template(generatorContext.getProject())));
        module.addTemplate("homeRoute", new RockerTemplate("src/main/kotlin/{packagePath}/HomeRoute.kt", homeRouteKotlin.template(generatorContext.getProject())));
        module.addTemplate("jacksonFeature", new RockerTemplate("src/main/kotlin/{packagePath}/JacksonFeature.kt", jacksonFeatureKotlin.template(generatorContext.getProject())));
        module.addTemplate("nameTransformer", new RockerTemplate("src/main/kotlin/{packagePath}/NameTransformer.kt", nameTransformerKotlin.template(generatorContext.getProject())));
        module.addTemplate("uppercaseTransformer", new RockerTemplate("src/main/kotlin/{packagePath}/UppercaseTransformer.kt", uppercaseTransformerKotlin.template(generatorContext.getProject())));

        return List.of("io.micronaut.starter.feature.ktor");
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public int getOrder() {
        return KotlinApplicationFeature.super.getOrder();
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        KotlinApplicationFeature.super.apply(generatorContext);
        OpenRewriteFeature.super.apply(generatorContext);
    }
}
