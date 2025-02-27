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

import io.micronaut.projectgen.core.buildtools.gradle.Gradle;
import io.micronaut.projectgen.core.buildtools.maven.Maven;
import io.micronaut.projectgen.core.feature.*;
import io.micronaut.projectgen.core.feature.config.Properties;
import io.micronaut.projectgen.core.feature.gitignore.GitIgnore;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.options.TestFramework;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.features.gradle.GroovyGradlePlugin;
import io.micronaut.projectgen.features.gradle.JavaGradlePlugin;
import io.micronaut.projectgen.features.gradle.KotlinGradlePlugin;
import io.micronaut.projectgen.features.gradle.ShadePlugin;
import io.micronaut.projectgen.javalibs.logging.Logback;
import io.micronaut.projectgen.micronaut.features.AppName;
import io.micronaut.projectgen.micronaut.features.MicronautAot;
import io.micronaut.projectgen.micronaut.features.serde.MicronautSerdeJackson;
import io.micronaut.projectgen.micronaut.features.test.MicronautTestJunit5;
import io.micronaut.projectgen.micronaut.features.validation.MicronautHttpValidation;
import io.micronaut.projectgen.micronaut.gradle.MicronautApplicationGradlePluginFeature;
import io.micronaut.projectgen.micronaut.maven.MicronautParentPomFeature;
import jakarta.inject.Singleton;
import java.util.Set;

/**
 * {@link DefaultFeature} for Spring Boot projects.
 */
@Singleton
public class ApplicationTypeDefaultFeature extends ApplicationTypeFeature {
    private final Maven maven;
    private final Properties properties;
    private final AppName appName;
    private final Logback logback;
    private final MicronautTestJunit5 micronautTestJunit5;
    private final MicronautHttpValidation micronautHttpValidation;
    private final MicronautApplicationGradlePluginFeature micronautApplicationGradlePlugin;
    private final JavaGradlePlugin javaGradlePlugin;
    private final KotlinGradlePlugin kotlinGradlePlugin;
    private final GroovyGradlePlugin groovyGradlePlugin;
    private final MicronautAot micronautAot;
    private final GitIgnore gitIgnore;
    private final ShadePlugin shadePlugin;
    private final JsonFeature serializationFeature;
    private final MicronautParentPomFeature micronautParentPomFeature;

    @SuppressWarnings("ParameterNumber")
    public ApplicationTypeDefaultFeature(Gradle gradle,
                                         Maven maven,
                                         Properties properties,
                                         AppName appName,
                                         Logback logback,
                                         MicronautTestJunit5 micronautTestJunit5,
                                         MicronautHttpValidation micronautHttpValidation,
                                         MicronautApplicationGradlePluginFeature micronautApplicationGradlePlugin,
                                         JavaGradlePlugin javaGradlePlugin,
                                         KotlinGradlePlugin kotlinGradlePlugin,
                                         GroovyGradlePlugin groovyGradlePlugin,
                                         MicronautAot micronautAot,
                                         GitIgnore gitIgnore,
                                         ShadePlugin shadePlugin,
                                         MicronautSerdeJackson serializationFeature,
                                         MicronautParentPomFeature micronautParentPomFeature) {
        super(gradle);
        this.maven = maven;
        this.properties = properties;
        this.appName = appName;
        this.logback = logback;
        this.micronautTestJunit5 = micronautTestJunit5;
        this.micronautHttpValidation = micronautHttpValidation;
        this.micronautApplicationGradlePlugin = micronautApplicationGradlePlugin;
        this.javaGradlePlugin = javaGradlePlugin;
        this.kotlinGradlePlugin = kotlinGradlePlugin;
        this.groovyGradlePlugin = groovyGradlePlugin;
        this.micronautAot = micronautAot;
        this.gitIgnore = gitIgnore;
        this.shadePlugin = shadePlugin;
        this.serializationFeature = serializationFeature;
        this.micronautParentPomFeature = micronautParentPomFeature;
    }

    @Override
    public boolean shouldApply(Options options, Set<Feature> selectedFeatures) {
        return options instanceof MicronautOptions micronautOptions && micronautOptions.applicationType() == ApplicationType.DEFAULT;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        super.processSelectedFeatures(featureContext);
        if (featureContext.getOptions().testFramework() == null ||
            featureContext.getOptions().testFramework() == TestFramework.JUNIT) {
            featureContext.addFeatureIfNotPresent(MicronautTestJunit5.class, micronautTestJunit5);
        }
        featureContext.addFeatureIfNotPresent(ConfigurationFeature.class, properties);
        featureContext.addFeatureIfNotPresent(AppName.class, appName);
        featureContext.addFeatureIfNotPresent(GitIgnore.class, gitIgnore);
        featureContext.addFeatureIfNotPresent(Logback.class, logback);
        featureContext.addFeatureIfNotPresent(MicronautHttpValidation.class, micronautHttpValidation);
        featureContext.addFeatureIfNotPresent(JsonFeature.class, serializationFeature);
        if (OptionUtils.hasGradleBuildTool(featureContext.getOptions())) {
            featureContext.addFeatureIfNotPresent(ShadePlugin.class, shadePlugin);
            featureContext.addFeatureIfNotPresent(MicronautApplicationGradlePluginFeature.class, micronautApplicationGradlePlugin);
            featureContext.addFeatureIfNotPresent(MicronautAot.class, micronautAot);
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
            featureContext.addFeatureIfNotPresent(Maven.class, maven);
            featureContext.addFeatureIfNotPresent(MicronautParentPomFeature.class, micronautParentPomFeature);
        }
    }

    @Override
    public String getName() {
        return "application-type-default";
    }
}
