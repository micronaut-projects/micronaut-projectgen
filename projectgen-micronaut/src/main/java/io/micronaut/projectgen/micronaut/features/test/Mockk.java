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
package io.micronaut.projectgen.micronaut.features.test;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.feature.BuildFeature;
import io.micronaut.projectgen.core.feature.DefaultFeature;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.FeaturePhase;
import io.micronaut.projectgen.core.feature.LanguageFeature;
import io.micronaut.projectgen.core.feature.TestFeature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.starter.feature.test.MockingFeature;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Feature that provides Mockk mocking library support for Micronaut applications.
 * This feature adds Mockk, a mocking library specifically designed for Kotlin testing.
 */
@Requires(property = "micronaut.starter.feature.mockk.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Mockk implements MockingFeature, DefaultFeature, OpenRewriteFeature {
    public static final String NAME_MOCKK = "mockk";

    @Override
    @NonNull
    public String getName() {
        return NAME_MOCKK;
    }

    @Override
    public String getTitle() {
        return "Mockk";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Mocking library for Kotlin";
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return List.of("io.micronaut.starter.feature.mockk");
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public boolean shouldApply(Options options, Set<Feature> selectedFeatures) {
        return isValid(selectedFeatures, options::buildTools, t -> t.stream().anyMatch(bt -> bt == BuildTool.MAVEN), BuildFeature.class, BuildFeature::isMaven)
            && isValid(selectedFeatures, options::language, t -> t == Language.KOTLIN, LanguageFeature.class, LanguageFeature::isKotlin)
            && isValid(selectedFeatures, options::testFramework, t -> t.isKotlinTestFramework(), TestFeature.class, TestFeature::isKotlinTestFramework);
    }

    /**
     * Validates if a feature should be applied based on selected features and options.
     *
     * @param selectedFeatures the set of selected features
     * @param supplier the supplier for the option value
     * @param nonNull the predicate to test when the value is not null
     * @param nullFeature the feature class to check when the value is null
     * @param nullFeatureTest the predicate to test the feature
     * @param <T> the type of the option value
     * @param <U> the type of the feature
     * @return true if the feature should be applied
     */
    private <T, U extends Feature> boolean isValid(Set<Feature> selectedFeatures,
        Supplier<T> supplier,
        Predicate<T> nonNull,
        Class<U> nullFeature,
        Predicate<U> nullFeatureTest) {
        T suppliedValue = supplier.get();
        return suppliedValue != null
            ? nonNull.test(suppliedValue)
            : selectedFeatures.stream()
            .filter(nullFeature::isInstance)
            .map(nullFeature::cast)
            .anyMatch(nullFeatureTest);
    }

    @Override
    public int getOrder() {
        return FeaturePhase.LOW.getOrder();
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        ModuleContext module = generatorContext.getRootModule();
        for (String recipeName : getRecipes(generatorContext)) {
            module.addConfigurationByRecipeName(recipeName);
        }
        // Only for Maven, these dependencies are applied by the Micronaut Gradle Plugin
        if (OptionUtils.hasMavenBuildTool(generatorContext.getOptions())) {
            for (String recipeName : getRecipes(generatorContext)) {
                module.addDependenciesByRecipeName(generatorContext.getOptions(), recipeName);
            }
        }
    }
}
