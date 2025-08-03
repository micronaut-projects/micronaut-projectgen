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
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Security feature for JWT (JSON Web Token) based authentication.
 * Implements SecurityAuthenticationModeProvider to specify the authentication mode.
 */
@Requires(property = "micronaut.starter.feature.security.jwt.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class SecurityJWT extends SecurityFeature implements SecurityAuthenticationModeProvider, OpenRewriteFeature {

    public static final String NAME = "security-jwt";

    public static final int ORDER = 0;

    public SecurityJWT(SecurityAnnotations securityAnnotations) {
        super(securityAnnotations);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Micronaut Security JWT";
    }

    @Override
    public String getDescription() {
        return "Adds support for JWT (JSON Web Token) based Authentication";
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        List<String> recipes = new ArrayList<>();
        Optional<SecurityAuthenticationMode> securityAuthenticationModeOptional = SecurityAuthenticationModeUtils.resolveSecurityAuthenticationMode(generatorContext);
        if (securityAuthenticationModeOptional.isPresent()
            && (securityAuthenticationModeOptional.get() == SecurityAuthenticationMode.BEARER || securityAuthenticationModeOptional.get() == SecurityAuthenticationMode.COOKIE)
        ) {
            recipes.add("io.micronaut.starter.feature.security-jwt-config");
        }
        recipes.add("io.micronaut.starter.feature.security-jwt");
        return recipes;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }

    @Override
    @NonNull
    public SecurityAuthenticationMode getSecurityAuthenticationMode() {
        return SecurityAuthenticationMode.BEARER;
    }
}
