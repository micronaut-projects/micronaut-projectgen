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
package io.micronaut.projectgen.core.generator;

import io.micronaut.context.BeanContext;
import io.micronaut.projectgen.core.feature.AvailableFeatures;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.core.io.ConsoleOutput;
import io.micronaut.projectgen.core.io.OutputHandler;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.template.RenderResult;
import io.micronaut.projectgen.core.template.Template;
import io.micronaut.projectgen.core.template.TemplateRenderer;
import io.micronaut.projectgen.core.utils.NameUtils;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 * Default implementation of {@link ProjectGenerator}.
 */
@Singleton
public class DefaultProjectGenerator implements ProjectGenerator {
    private final ContextFactory contextFactory;
    private final BeanContext beanContext;

    public DefaultProjectGenerator(ContextFactory contextFactory, BeanContext beanContext) {
        this.contextFactory = contextFactory;
        this.beanContext = beanContext;
    }

    @Override
    public void generate(Options options, OutputHandler outputHandler, ConsoleOutput consoleOutput, Provider<AvailableFeatures> availableFeaturesProvider) throws Exception {
        Project project = NameUtils.parse(options.name());
        List<String> selectedFeatures = options.features();

        AvailableFeatures availableFeatures = availableFeaturesProvider == null
            ? beanContext.getBean(AvailableFeatures.class)
            : availableFeaturesProvider.get();
        FeatureContext featureContext = contextFactory.createFeatureContext(availableFeatures, selectedFeatures, options);
        GeneratorContext generatorContext = contextFactory.createGeneratorContext(project, featureContext, consoleOutput);

        List<String> features = new ArrayList<>(generatorContext.getFeatures().size());
        features.addAll(generatorContext.getFeatures());
        features.sort(Comparator.comparing(Function.identity()));

        generatorContext.applyFeatures();

        try (TemplateRenderer templateRenderer = TemplateRenderer.create(project, outputHandler)) {
            for (Template template: generatorContext.getTemplates().values()) {
                RenderResult renderResult = templateRenderer.render(template);
                if (renderResult.getError() != null) {
                    throw renderResult.getError();
                }
            }
        }

    }
}
