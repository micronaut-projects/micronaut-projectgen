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
package io.micronaut.starter.feature.dekorate;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.starter.feature.micrometer.Prometheus;

import jakarta.inject.Singleton;

import java.util.List;

/**
 * Adds Dekorate Prometheus support that generates ServiceMonitor resource.
 *
 * @author Pavol Gressa
 * @since 2.1
 */
@Requires(property = "micronaut.starter.feature.dekorate.prometheus.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class DekoratePrometheus extends AbstractDekorateServiceFeature implements OpenRewriteFeature {

    private final Prometheus prometheus;

    public DekoratePrometheus(DekorateKubernetes dekorateKubernetes, Prometheus prometheus) {
        super(dekorateKubernetes);
        this.prometheus = prometheus;
    }

    @NonNull
    @Override
    public String getName() {
        return "dekorate-prometheus";
    }

    @Override
    public String getTitle() {
        return "Dekorate Prometheus Support";
    }

    @Override
    public String getDescription() {
        return """
                Extends Decorate's generated Kubernetes deployment manifests with Prometheus ServiceMonitor resource \
                using Dekorate Prometheus Support.\
                """;
    }

    public final void processSelectedFeatures(FeatureContext featureContext) {
        super.processSelectedFeatures(featureContext);
        if (!featureContext.isPresent(Prometheus.class)) {
            featureContext.addFeature(prometheus);
        }
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return List.of("io.micronaut.starter.feature.dekorate-prometheus");
    }

}
