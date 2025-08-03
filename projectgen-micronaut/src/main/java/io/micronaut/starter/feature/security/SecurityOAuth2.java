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
package io.micronaut.starter.feature.security;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.micronaut.features.httpclient.HttpClientFeature;
import io.micronaut.projectgen.micronaut.features.httpclient.HttpClient;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

/**
 * Security feature for OAuth 2.0 authentication support.
 */
@Requires(property = "micronaut.starter.feature.security.oauth2.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class SecurityOAuth2 extends SecurityFeature implements SecurityAuthenticationModeProvider, OpenRewriteFeature {

    public static final String NAME = "security-oauth2";
    public static final int ORDER = SecurityJWT.ORDER + 10;

    private final HttpClient httpClient;

    @Deprecated
    public SecurityOAuth2(SecurityAnnotations securityAnnotations) {
        this(securityAnnotations, new HttpClient());
    }

    @Inject
    public SecurityOAuth2(SecurityAnnotations securityAnnotations, HttpClient httpClient) {
        super(securityAnnotations);
        this.httpClient = httpClient;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        super.processSelectedFeatures(featureContext);
        featureContext.addFeatureIfNotPresent(HttpClientFeature.class, httpClient);
    }

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Micronaut Security OAuth 2.0";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds support for authentication with OAuth 2.0 providers";
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        List<String> recipes = new ArrayList<>();
        if (generatorContext.isFeaturePresent(SecurityJWT.class)) {
            recipes.add("io.micronaut.starter.feature.security-oauth2-jwt-config");
        }
        recipes.add("io.micronaut.starter.feature.security-oauth2");
        return recipes;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }

    @Override
    @NonNull
    public SecurityAuthenticationMode getSecurityAuthenticationMode() {
        return SecurityAuthenticationMode.COOKIE;
    }
}
