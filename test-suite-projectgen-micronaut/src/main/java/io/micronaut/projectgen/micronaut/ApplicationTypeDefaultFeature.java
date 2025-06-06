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

import io.micronaut.core.annotation.Nullable;
import io.micronaut.projectgen.core.feature.*;
import io.micronaut.projectgen.core.feature.config.Properties;
import io.micronaut.projectgen.core.feature.gitignore.GitIgnore;
import io.micronaut.projectgen.core.options.GenericOptionsBuilder;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.micronaut.features.AppName;
import io.micronaut.projectgen.micronaut.features.MicronautAot;
import io.micronaut.projectgen.micronaut.features.httpclient.HttpClientFeature;
import io.micronaut.projectgen.micronaut.features.httpclient.HttpClientTest;
import io.micronaut.projectgen.micronaut.features.logging.Logback;
import io.micronaut.projectgen.micronaut.features.serde.MicronautSerdeJackson;
import io.micronaut.projectgen.micronaut.features.test.MicronautTestJunit5;
import io.micronaut.projectgen.micronaut.features.test.MicronautTestSpock;
import io.micronaut.projectgen.micronaut.features.validation.MicronautHttpValidation;
import io.micronaut.projectgen.micronaut.gradle.GroovyGradlePlugin;
import io.micronaut.projectgen.micronaut.gradle.JavaGradlePlugin;
import io.micronaut.projectgen.micronaut.gradle.KotlinGradlePlugin;
import io.micronaut.projectgen.micronaut.gradle.MicronautApplicationGradlePluginFeature;
import io.micronaut.projectgen.micronaut.gradle.ShadePlugin;
import io.micronaut.projectgen.micronaut.maven.MicronautMavenCompilerPlugin;
import io.micronaut.projectgen.micronaut.maven.MicronautMavenPlugin;
import io.micronaut.projectgen.micronaut.maven.MicronautParentPomFeature;
import io.micronaut.starter.feature.function.FunctionFeature;
import io.micronaut.starter.feature.lang.kotlin.KotlinApplication;
import io.micronaut.starter.feature.server.Netty;
import io.micronaut.starter.feature.server.ServerFeature;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Set;

/**
 * {@link DefaultFeature} for Spring Boot projects.
 */
@Singleton
public class ApplicationTypeDefaultFeature extends ApplicationTypeFeature {
    private final MicronautMavenPlugin micronautMavenPlugin;
    private final MicronautMavenCompilerPlugin micronautMavenCompilerPlugin;
    private final AppName appName;
    private final Netty netty;
    private final MicronautHttpValidation micronautHttpValidation;
    private final MicronautApplicationGradlePluginFeature micronautApplicationGradlePlugin;
    private final JavaGradlePlugin javaGradlePlugin;
    private final KotlinGradlePlugin kotlinGradlePlugin;
    private final GroovyGradlePlugin groovyGradlePlugin;
    private final MicronautAot micronautAot;
    private final ShadePlugin shadePlugin;
    private final JsonFeature serializationFeature;
    private final MicronautParentPomFeature micronautParentPomFeature;
    @Nullable
    private final JavaApplicationFeature javaApplicationFeature;
    @Nullable
    private final KotlinApplication kotlinApplication;
    @Nullable
    private final GroovyApplicationFeature groovyApplicationFeature;
    private HttpClientTest httpClientTest;

    @SuppressWarnings("ParameterNumber")
    public ApplicationTypeDefaultFeature(MicronautMavenPlugin micronautMavenPlugin,
                                         AppName appName,
                                         Logback logback,
                                         MicronautTestJunit5 micronautTestJunit5,
                                         MicronautTestSpock micronautTestSpock, MicronautMavenCompilerPlugin micronautMavenCompilerPlugin, Netty netty,
                                         MicronautHttpValidation micronautHttpValidation,
                                         MicronautApplicationGradlePluginFeature micronautApplicationGradlePlugin,
                                         JavaGradlePlugin javaGradlePlugin,
                                         KotlinGradlePlugin kotlinGradlePlugin,
                                         GroovyGradlePlugin groovyGradlePlugin,
                                         MicronautAot micronautAot,
                                         GitIgnore gitIgnore,
                                         ShadePlugin shadePlugin,
                                         MicronautSerdeJackson serializationFeature,
                                         MicronautParentPomFeature micronautParentPomFeature,
                                         List<JavaApplicationFeature> javaApplicationFeatures,
                                         List<KotlinApplication> kotlinApplications,
                                         List<GroovyApplicationFeature> groovyApplicationFeatures,
                                         HttpClientTest httpClientTest) {
        super(micronautTestJunit5, micronautTestSpock, logback, gitIgnore);
        this.micronautMavenPlugin = micronautMavenPlugin;
        this.appName = appName;
        this.micronautMavenCompilerPlugin = micronautMavenCompilerPlugin;
        this.netty = netty;
        this.micronautHttpValidation = micronautHttpValidation;
        this.micronautApplicationGradlePlugin = micronautApplicationGradlePlugin;
        this.javaGradlePlugin = javaGradlePlugin;
        this.kotlinGradlePlugin = kotlinGradlePlugin;
        this.groovyGradlePlugin = groovyGradlePlugin;
        this.micronautAot = micronautAot;
        this.shadePlugin = shadePlugin;
        this.serializationFeature = serializationFeature;
        this.micronautParentPomFeature = micronautParentPomFeature;
        Options options = GenericOptionsBuilder.builder().template(ApplicationType.DEFAULT.toString()).build();
        this.javaApplicationFeature = javaApplicationFeatures.stream().filter(f -> f.supports(options)).findFirst().orElse(null);
        this.kotlinApplication = kotlinApplications.stream().filter(f -> f.supports(options)).findFirst().orElse(null);
        this.groovyApplicationFeature = groovyApplicationFeatures.stream().filter(f -> f.supports(options)).findFirst().orElse(null);
        this.httpClientTest = httpClientTest;
    }

    @Override
    public boolean shouldApply(Options options, Set<Feature> selectedFeatures) {
        ApplicationType applicationType = ApplicationType.of(options.template());
        return applicationType == ApplicationType.DEFAULT;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        super.processSelectedFeatures(featureContext);

        if (featureContext.getSelectedFeatures().stream().noneMatch(f -> f instanceof ServerFeature || f instanceof FunctionFeature)) {
            featureContext.addFeatureIfNotPresent(ServerFeature.class, netty);
        }
        if (featureContext.getOptions().language() == Language.JAVA && javaApplicationFeature != null) {
            featureContext.addFeatureIfNotPresent(JavaApplicationFeature.class, javaApplicationFeature);
        }
        if (featureContext.getOptions().language() == Language.KOTLIN && kotlinApplication != null) {
            featureContext.addFeatureIfNotPresent(KotlinApplicationFeature.class, kotlinApplication);
        }
        if (featureContext.getOptions().language() == Language.GROOVY && groovyApplicationFeature != null) {
            featureContext.addFeatureIfNotPresent(GroovyApplicationFeature.class, groovyApplicationFeature);
        }

        featureContext.addFeatureIfNotPresent(AppName.class, appName);

        featureContext.addFeatureIfNotPresent(MicronautHttpValidation.class, micronautHttpValidation);
        featureContext.addFeatureIfNotPresent(JsonFeature.class, serializationFeature);
        featureContext.addFeatureIfNotPresent(MicronautAot.class, micronautAot);

        if (OptionUtils.hasGradleBuildTool(featureContext.getOptions())) {
            featureContext.addFeatureIfNotPresent(ShadePlugin.class, shadePlugin);
            featureContext.addFeatureIfNotPresent(MicronautApplicationGradlePluginFeature.class, micronautApplicationGradlePlugin);
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
            featureContext.addFeatureIfNotPresent(MicronautParentPomFeature.class, micronautParentPomFeature);
            featureContext.addFeatureIfNotPresent(MicronautMavenPlugin.class, micronautMavenPlugin);
            featureContext.addFeatureIfNotPresent(MicronautMavenCompilerPlugin.class, micronautMavenCompilerPlugin);
        }

        if (!featureContext.isPresent(HttpClientFeature.class)) {
            featureContext.addFeatureIfNotPresent(HttpClientFeature.class, httpClientTest);
        }
    }

    @Override
    public String getName() {
        return "application-type-default";
    }
}
