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
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.inject.qualifiers.Qualifiers;
import io.micronaut.projectgen.core.feature.AvailableFeatures;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.core.io.ConsoleOutput;
import io.micronaut.projectgen.core.io.OutputHandler;
import io.micronaut.projectgen.core.options.OperatingSystem;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.template.RenderResult;
import io.micronaut.projectgen.core.template.Template;
import io.micronaut.projectgen.core.template.TemplateRenderer;
import io.micronaut.projectgen.core.utils.NameUtils;
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
    public void generate(String applicationType,
                         Project project,
                         Options options,
                         @Nullable OperatingSystem operatingSystem,
                         List<String> selectedFeatures,
                         OutputHandler outputHandler,
                         ConsoleOutput consoleOutput) throws Exception {

        GeneratorContext generatorContext = createGeneratorContext(
                applicationType,
                project,
                options,
                operatingSystem,
                selectedFeatures,
                consoleOutput
        );

        generate(applicationType, project, outputHandler, generatorContext);
    }

    @Override
    public void generate(
            String applicationType,
            Project project,
            OutputHandler outputHandler,
            GeneratorContext generatorContext) throws Exception {
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

    @Override
    public GeneratorContext createGeneratorContext(
            String applicationType,
            Project project,
            Options options,
            @Nullable OperatingSystem operatingSystem,
            List<String> selectedFeatures,
            ConsoleOutput consoleOutput) {
        AvailableFeatures availableFeatures = StringUtils.isEmpty(applicationType)
            ? beanContext.getBean(AvailableFeatures.class)
            : beanContext.getBean(AvailableFeatures.class, Qualifiers.byName(applicationType));
        FeatureContext featureContext = contextFactory.createFeatureContext(availableFeatures, selectedFeatures, applicationType, options, operatingSystem);
        return contextFactory.createGeneratorContext(project, featureContext, consoleOutput);
    }

    @Override
    public void generate(Options options, OutputHandler outputHandler, ConsoleOutput consoleOutput) throws Exception {

        String applicationType = null;
        Project project = NameUtils.parse(options.name());
        OperatingSystem operatingSystem = options.operatingSystem();
        List<String> selectedFeatures = options.features();
        generate(applicationType,
            project,
            options,
            operatingSystem,
            selectedFeatures,
            outputHandler,
            consoleOutput);

    }
}
