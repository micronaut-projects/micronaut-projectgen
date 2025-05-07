/*
 * Copyright 2017-2023 original authors
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
package io.micronaut.starter.feature.k8s;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.starter.feature.discovery.DiscoveryCore;
import io.micronaut.starter.feature.reactor.Reactor;
import io.micronaut.starter.feature.rxjava.RxJava2;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

/**
 * Adds micronaut-kubernetes-rxjava2-client.
 *
 * @author Pavol Gressa
 * @since 3.1
 */
@Requires(property = "micronaut.starter.feature.kubernetes.rxjava2.client.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class KubernetesRxJava2Client implements OpenRewriteFeature {
    private final DiscoveryCore discoveryCore;
    private final RxJava2 rxJava2;

    public KubernetesRxJava2Client(DiscoveryCore discoveryCore,
                                   RxJava2 rxJava2) {
        this.discoveryCore = discoveryCore;
        this.rxJava2 = rxJava2;
    }

    @NonNull
    @Override
    public String getName() {
        return "kubernetes-rxjava2-client";
    }

    @Override
    public String getTitle() {
        return "Kubernetes RxJava2 Client";
    }

    @Override
    public String getDescription() {
        return "Adds Official Kubernetes Java Client with RxJava2 interface";
    }

    @Override
    public String getCategory() {
        return Category.CLIENT;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (featureContext.isPresent(KubernetesClient.class)) {
            featureContext.exclude(KubernetesClient.class::isInstance);
        }
        if (!featureContext.isPresent(RxJava2.class)) {
            featureContext.addFeatureIfNotPresent(RxJava2.class, rxJava2);
        }
        if (!featureContext.isPresent(DiscoveryCore.class)
            && OptionUtils.hasMavenBuildTool(featureContext.getOptions())
            && featureContext.getOptions().language() == Language.GROOVY
        ) {
            featureContext.addFeatureIfNotPresent(DiscoveryCore.class, discoveryCore);
        }
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return List.of("io.micronaut.starter.feature.kubernetes-rxjava2-client");
    }

}
