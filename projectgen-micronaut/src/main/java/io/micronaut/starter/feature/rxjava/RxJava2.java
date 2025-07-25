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
import io.micronaut.starter.feature.Category;

import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.micronaut.features.httpclient.HttpClient;
import io.micronaut.starter.feature.server.Netty;
import jakarta.inject.Singleton;

import java.util.List;

@Requires(property = "micronaut.starter.feature.rxjava2.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class RxJava2 implements OpenRewriteFeature {

    private final RxJava2HttpServerNetty rxJava2HttpServerNetty;
    private final RxJava2HttpClient rxJava2HttpClient;

    public RxJava2(RxJava2HttpServerNetty rxJava2HttpServerNetty, RxJava2HttpClient rxJava2HttpClient) {
        this.rxJava2HttpServerNetty = rxJava2HttpServerNetty;
        this.rxJava2HttpClient = rxJava2HttpClient;
    }

    @NonNull
    @Override
    public String getName() {
        return "rxjava2";
    }

    @Override
    public String getTitle() {
        return "RxJava 2";
    }

    @Override
    public String getDescription() {
        return "Adds Reactive support using RxJava 2";
    }

    @Override
    public String getCategory() {
        return Category.REACTIVE;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (featureContext.isPresent(Netty.class)) {
            featureContext.addFeature(rxJava2HttpServerNetty);
        }
        if (featureContext.isPresent(HttpClient.class)) {
            featureContext.addFeature(rxJava2HttpClient);
        }
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return List.of("io.micronaut.starter.feature.rxjava2");
    }

}
