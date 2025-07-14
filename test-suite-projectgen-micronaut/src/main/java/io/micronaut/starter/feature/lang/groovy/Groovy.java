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
package io.micronaut.starter.feature.lang.groovy;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.buildtools.dependencies.Coordinate;
import io.micronaut.projectgen.core.feature.GroovyApplicationFeature;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.core.options.GenericOptionsBuilder;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.starter.buildtools.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.ApplicationFeature;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.core.feature.LanguageFeature;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.starter.util.VersionInfo;
import jakarta.inject.Singleton;
import io.micronaut.projectgen.micronaut.maven.GroovyMavenPlusPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

@Requires(property = "micronaut.starter.feature.groovy.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Groovy implements LanguageFeature, OpenRewriteFeature {

    protected final List<GroovyApplicationFeature> applicationFeatures;

    protected final GroovyMavenPlusPlugin groovyMavenPlusPlugin;

    public Groovy(List<GroovyApplicationFeature> applicationFeatures,
                  GroovyMavenPlusPlugin groovyMavenPlusPlugin) {
        this.applicationFeatures = applicationFeatures;
        this.groovyMavenPlusPlugin = groovyMavenPlusPlugin;
    }

    @Override
    public String getName() {
        return "groovy";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (OptionUtils.hasMavenBuildTool(featureContext.getOptions())) {
            featureContext.addFeature(groovyMavenPlusPlugin);
        }
        processSelectedFeatured(featureContext, feature -> true);
    }

    protected void processSelectedFeatured(FeatureContext featureContext, Predicate<Feature> filter) {
        if (!featureContext.isPresent(ApplicationFeature.class)) {
            ApplicationType type = ApplicationType.of(featureContext.getOptions().template());
            applicationFeatures.stream()
                    .filter(filter)
                    .filter(f -> f.supports(GenericOptionsBuilder.builder().template(type.toString()).build()))
                    .findFirst()
                    .ifPresent(featureContext::addFeature);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        ModuleContext module = generatorContext.getRootModule();
        OpenRewriteFeature.super.apply(generatorContext);
        if (OptionUtils.hasMavenBuildTool(generatorContext.getOptions())) {
            //todo add openrewrite support for maven properties
            addGroovyVersionProperty(generatorContext, module);
        }
    }

    protected void addGroovyVersionProperty(GeneratorContext generatorContext, ModuleContext module) {
        Coordinate coordinate = generatorContext.resolveCoordinate("groovy-bom");
        module.buildProperties().put("groovyVersion", coordinate.getVersion());
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        List<String> recipes = new ArrayList<>();
        recipes.add("io.micronaut.starter.feature.groovy");
        if (OptionUtils.hasMavenBuildTool(generatorContext.getOptions())) {
            recipes.add("io.micronaut.starter.feature.groovy-maven");
        }
            return recipes;
    }

    @Override
    public boolean isGroovy() {
        return true;
    }

    @Override
    public boolean shouldApply(Options options, Set<Feature> selectedFeatures) {
        return options.language() == Language.GROOVY;
    }
}
