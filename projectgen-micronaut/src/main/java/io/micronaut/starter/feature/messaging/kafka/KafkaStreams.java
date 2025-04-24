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
package io.micronaut.starter.feature.messaging.kafka;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.generator.Project;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.starter.feature.database.TestContainers;
import io.micronaut.starter.feature.messaging.MessagingFeature;
import io.micronaut.starter.feature.messaging.SharedTestResourceFeature;
import io.micronaut.projectgen.micronaut.template.kafka.exampleFactoryGroovy;
import io.micronaut.projectgen.micronaut.template.kafka.exampleFactoryJava;
import io.micronaut.projectgen.micronaut.template.kafka.exampleFactoryKotlin;
import io.micronaut.projectgen.micronaut.template.kafka.exampleListenerGroovy;
import io.micronaut.projectgen.micronaut.template.kafka.exampleListenerJava;
import io.micronaut.projectgen.micronaut.template.kafka.exampleListenerKotlin;
import io.micronaut.starter.feature.testresources.EaseTestingFeature;
import io.micronaut.starter.feature.testresources.TestResources;
import jakarta.inject.Singleton;

@Requires(property = "micronaut.starter.feature.kafka.streams.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class KafkaStreams extends EaseTestingFeature implements MessagingFeature, SharedTestResourceFeature {

    private final Kafka kafka;

    public KafkaStreams(TestContainers testContainers, TestResources testResources, Kafka kafka) {
        super(testContainers, testResources);
        this.kafka = kafka;
    }

    @NonNull
    @Override
    public String getName() {
        return "kafka-streams";
    }

    @Override
    public String getTitle() {
        return "Kafka Streams";
    }

    @Override
    public String getDescription() {
        return "Adds support for Kafka Streams";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(Kafka.class)) {
            featureContext.addFeature(kafka);
        }
    }

    @Override
    public String getFrameworkDocumentation(GeneratorContext generatorContext) {
        return "https://micronaut-projects.github.io/micronaut-kafka/latest/guide/index.html#kafkaStream";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        ModuleContext module = generatorContext.getRootModule();
        Project project = generatorContext.getProject();

        String exampleListener = generatorContext.getSourcePath("/{packagePath}/ExampleListener");
        module.addTemplate(generatorContext.getOptions().language(),
            "exampleListener", exampleListener,
            exampleListenerJava.template(project),
            exampleListenerKotlin.template(project),
            exampleListenerGroovy.template(project));

        String exampleFactory = generatorContext.getSourcePath("/{packagePath}/ExampleFactory");
        module.addTemplate(generatorContext.getOptions().language(),
            "exampleFactory", exampleFactory,
            exampleFactoryJava.template(project),
            exampleFactoryKotlin.template(project),
            exampleFactoryGroovy.template(project));

        module.addDependency(Dependency.builder()
                .groupId("io.micronaut.kafka")
                .artifactId("micronaut-kafka-streams")
                .compile());
    }
}
