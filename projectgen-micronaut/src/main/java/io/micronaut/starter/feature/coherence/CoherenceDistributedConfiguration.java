/*
 * Copyright 2017-2021 original authors
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
package io.micronaut.starter.feature.coherence;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.feature.config.Configuration;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.core.feature.DistributedConfigFeature;
import jakarta.inject.Singleton;

import java.util.Map;

/**
 * Coherence used as Distributed Configuration feature.
 *
 * @author Pavol Gressa
 * @since 2.4
 */
@Requires(property = "micronaut.starter.feature.coherence.distributed.configuration.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class CoherenceDistributedConfiguration implements DistributedConfigFeature {

    public static final String NAME = "coherence-distributed-configuration";
    private final CoherenceFeature coherenceFeature;

    public CoherenceDistributedConfiguration(CoherenceFeature coherenceFeature) {
        this.coherenceFeature = coherenceFeature;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Coherence Distributed Configuration";
    }

    @Override
    public String getDescription() {
        return "Adds support for Distributed Configuration using Coherence";
    }

    @Override
    public String getThirdPartyDocumentation(GeneratorContext generatorContext) {
        return "https://coherence.java.net/";
    }

    @Override
    public String getFrameworkDocumentation(GeneratorContext generatorContext) {
        return "https://micronaut-projects.github.io/micronaut-coherence/latest/guide/#distributedConfiguration";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(CoherenceFeature.class)) {
            featureContext.addFeature(coherenceFeature);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        ModuleContext module = generatorContext.getRootModule();
        Configuration config = generatorContext.isFeaturePresent(DistributedConfigFeature.class)
            ? module.bootstrapConfiguration()
            : module.configuration();

        config.put("coherence.client.enabled", true);
        config.put("coherence.client.host", "${COHERENCE_HOST:localhost}");
        config.put("coherence.client.port", "${COHERENCE_PORT:1408}");

        Dependency.Builder distributedConfiguration = MicronautDependencyUtils.coherenceDependency().artifactId("micronaut-coherence-distributed-configuration").compile();
        module.addDependency(distributedConfiguration);

        if (OptionUtils.hasGradleBuildTool(generatorContext.getOptions()) && !generatorContext.isFeaturePresent(CoherenceGrpcClient.class)) {
            module.addDependency(Dependency.builder()
                    .groupId("com.oracle.coherence.ce")
                    .artifactId("coherence-java-client")
                    .compile());
        }
    }
}
