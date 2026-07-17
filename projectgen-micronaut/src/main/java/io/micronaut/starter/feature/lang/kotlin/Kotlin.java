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
package io.micronaut.starter.feature.lang.kotlin;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.feature.KotlinApplicationFeature;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.core.options.GenericOptionsBuilder;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Coordinate;
import io.micronaut.starter.feature.ApplicationFeature;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.core.feature.LanguageFeature;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.options.Options;

import jakarta.inject.Singleton;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Represents the Kotlin language feature for the project generator.
 * <p>
 * This class manages Kotlin application features, processes feature selection,
 * applies Kotlin-specific configurations such as setting the Kotlin version,
 */
@Requires(property = "micronaut.starter.feature.kotlin.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Kotlin implements LanguageFeature, OpenRewriteFeature {

    protected final List<KotlinApplicationFeature> applicationFeatures;

    public Kotlin(List<KotlinApplicationFeature> applicationFeatures) {
        this.applicationFeatures = applicationFeatures;
    }

    @Override
    @NonNull
    public String getName() {
        return "kotlin";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        processSelectedFeatures(featureContext, kotlinApplicationFeature -> true);
    }

    /**
     * Processes selected features by adding {@link ApplicationFeature} based on the application type
     * if none is explicitly present.
     *
     * @param featureContext The context containing selected features.
     * @param filter Predicate to filter applicable features.
     */
    protected void processSelectedFeatures(FeatureContext featureContext, Predicate<Feature> filter) {
        if (!featureContext.isPresent(ApplicationFeature.class)) {
            ApplicationType type = ApplicationType.of(featureContext.getOptions().template());
            applicationFeatures.stream()
                .filter(filter)
                .filter(f -> !f.isVisible() && f.supports(GenericOptionsBuilder.builder().template(type.toString()).build()))
                .findFirst()
                .ifPresent(featureContext::addFeature);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        ModuleContext module = generatorContext.getRootModule();
        addKotlinVersionProperty(generatorContext, module);
        OpenRewriteFeature.super.apply(generatorContext);
    }

    /**
     * Adds the Kotlin version property to the build based on the resolved Kotlin BOM coordinate.
     *
     * @param generatorContext The context for project generation.
     * @param module The module to which the property should be added.
     */
    protected void addKotlinVersionProperty(GeneratorContext generatorContext, ModuleContext module) {
        Coordinate coordinate = generatorContext.resolveCoordinate("kotlin-bom");
        module.buildProperties().put("kotlinVersion", java.util.Objects.requireNonNull(coordinate.getVersion()));
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return List.of("io.micronaut.starter.feature.kotlin");
    }

    @Override
    public boolean isKotlin() {
        return true;
    }

    @Override
    public boolean shouldApply(Options options, Set<Feature> selectedFeatures) {
        return options.language() == Language.KOTLIN;
    }
}
