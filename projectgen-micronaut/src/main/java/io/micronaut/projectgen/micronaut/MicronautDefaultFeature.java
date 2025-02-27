/*
 * Copyright 2017-2025 original authors
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
package io.micronaut.projectgen.micronaut;

import io.micronaut.projectgen.core.buildtools.MavenCentral;
import io.micronaut.projectgen.core.buildtools.Repository;
import io.micronaut.projectgen.core.buildtools.RequiresRepository;
import io.micronaut.projectgen.core.feature.DefaultFeature;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.core.feature.gitignore.GitIgnore;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.features.gradle.GroovyGradlePlugin;
import io.micronaut.projectgen.features.gradle.JavaGradlePlugin;
import io.micronaut.projectgen.features.gradle.KotlinGradlePlugin;
import io.micronaut.projectgen.features.gradle.ShadePlugin;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Set;

/**
 * {@link DefaultFeature} for Spring Boot projects.
 */
@Singleton
public class MicronautDefaultFeature implements DefaultFeature, RequiresRepository {
    private final JavaGradlePlugin javaGradlePlugin;
    private final KotlinGradlePlugin kotlinGradlePlugin;
    private final GroovyGradlePlugin groovyGradlePlugin;
    private final GitIgnore gitIgnore;
    private final ShadePlugin shadePlugin;

    public MicronautDefaultFeature(JavaGradlePlugin javaGradlePlugin,
                                   KotlinGradlePlugin kotlinGradlePlugin,
                                   GroovyGradlePlugin groovyGradlePlugin,
                                   GitIgnore gitIgnore, ShadePlugin shadePlugin) {
        this.javaGradlePlugin = javaGradlePlugin;
        this.kotlinGradlePlugin = kotlinGradlePlugin;
        this.groovyGradlePlugin = groovyGradlePlugin;
        this.gitIgnore = gitIgnore;
        this.shadePlugin = shadePlugin;
    }

    @Override
    public boolean shouldApply(Options options, Set<Feature> selectedFeatures) {
        return options instanceof MicronautOptions;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeatureIfNotPresent(GitIgnore.class, gitIgnore);
        if (OptionUtils.hasGradleBuildTool(featureContext.getOptions())) {
            featureContext.addFeatureIfNotPresent(ShadePlugin.class, shadePlugin);
            switch (featureContext.getOptions().language()) {
                case JAVA:
                    featureContext.addFeatureIfNotPresent(JavaGradlePlugin.class, javaGradlePlugin);
                    break;
                case KOTLIN:
                    featureContext.addFeatureIfNotPresent(KotlinGradlePlugin.class, kotlinGradlePlugin);
                    break;
                case GROOVY:
                    featureContext.addFeatureIfNotPresent(GroovyGradlePlugin.class, groovyGradlePlugin);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported language: " + featureContext.getOptions().language());
            }
        }
        if (OptionUtils.hasMavenBuildTool(featureContext.getOptions())) {
        }
    }

    @Override
    public String getName() {
        return "spring boot";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public boolean supports(Options options) {
        return true;
    }

    @Override
    public List<Repository> getRepositories() {
        return List.of(new MavenCentral());
    }
}
