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
package io.micronaut.projectgen.springboot;

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
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Set;

/**
 * {@link DefaultFeature} for Spring Boot projects.
 */
@Singleton
public class SpringBootDefaultFeature implements DefaultFeature, RequiresRepository {
    private final SpringBootGradlePlugin springBootGradlePlugin;
    private final JavaGradlePlugin javaGradlePlugin;
    private final SpringDependencyManagementGradlePlugin springDependencyManagementGradlePlugin;
    private final SpringBootStarter springBootStarter;
    private final KotlinGradlePlugin kotlinGradlePlugin;
    private final GroovyGradlePlugin groovyGradlePlugin;
    private final SpringBootParentPomFeature springBootParentPomFeature;
    private final SpringBootMavenPlugin springBootMavenPlugin;
    private final GitIgnore gitIgnore;

    public SpringBootDefaultFeature(SpringBootGradlePlugin springBootGradlePlugin,
                                    JavaGradlePlugin javaGradlePlugin,
                                    SpringDependencyManagementGradlePlugin springDependencyManagementGradlePlugin,
                                    SpringBootStarter springBootStarter,
                                    KotlinGradlePlugin kotlinGradlePlugin,
                                    GroovyGradlePlugin groovyGradlePlugin,
                                    SpringBootParentPomFeature springBootParentPomFeature, SpringBootMavenPlugin springBootMavenPlugin, GitIgnore gitIgnore) {
        this.springBootGradlePlugin = springBootGradlePlugin;
        this.javaGradlePlugin = javaGradlePlugin;
        this.springDependencyManagementGradlePlugin = springDependencyManagementGradlePlugin;
        this.springBootStarter = springBootStarter;
        this.kotlinGradlePlugin = kotlinGradlePlugin;
        this.groovyGradlePlugin = groovyGradlePlugin;
        this.springBootParentPomFeature = springBootParentPomFeature;
        this.springBootMavenPlugin = springBootMavenPlugin;
        this.gitIgnore = gitIgnore;
    }

    @Override
    public boolean shouldApply(String applicationType, Options options, Set<Feature> selectedFeatures) {
        return options.framework().equals(getTargetFramework());
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeatureIfNotPresent(GitIgnore.class, gitIgnore);
        if (OptionUtils.hasGradleBuildTool(featureContext.getOptions())) {
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
            featureContext.addFeatureIfNotPresent(SpringBootGradlePlugin.class, springBootGradlePlugin);
            featureContext.addFeatureIfNotPresent(SpringDependencyManagementGradlePlugin.class, springDependencyManagementGradlePlugin);
            featureContext.addFeatureIfNotPresent(SpringBootStarter.class, springBootStarter);
        }
        if (OptionUtils.hasMavenBuildTool(featureContext.getOptions())) {
            featureContext.addFeatureIfNotPresent(SpringBootParentPomFeature.class, springBootParentPomFeature);
            featureContext.addFeatureIfNotPresent(SpringBootMavenPlugin.class, springBootMavenPlugin);
        }
    }

    @Override
    public String getTargetFramework() {
        return "spring-boot";
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
    public boolean supports(String applicationType) {
        return true;
    }

    @Override
    public List<Repository> getRepositories() {
        return List.of(new MavenCentral());
    }
}
