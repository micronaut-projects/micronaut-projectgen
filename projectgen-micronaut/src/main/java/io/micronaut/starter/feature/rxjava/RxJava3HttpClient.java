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
package io.micronaut.starter.feature.rxjava;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import jakarta.inject.Singleton;

import java.util.List;

/**
 * Feature for adding RxJava 3 HTTP client support.
 *
 * <p>Provides the RxJava 3 variant of the Micronaut HTTP client.</p>
 */
@Requires(property = "micronaut.starter.feature.rxjava3.http.client.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class RxJava3HttpClient implements OpenRewriteFeature {

    @NonNull
    @Override
    public String getName() {
        return "rxjava3-http-client";
    }

    @Override
    @NonNull
    public String getTitle() {
        return "RxJava 3 HTTP Client";
    }

    @Override
    public String getDescription() {
        return "RxJava 3 variation of the Micronaut HTTP client.";
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return List.of("io.micronaut.starter.feature.rxjava3-http-client");
    }

}
