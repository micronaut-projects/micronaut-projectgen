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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.projectgen.core.feature.AvailableFeatures;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.core.io.ConsoleOutput;
import io.micronaut.projectgen.core.io.FileSystemOutputHandler;
import io.micronaut.projectgen.core.io.OutputHandler;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.template.RenderResult;
import io.micronaut.projectgen.core.template.Template;
import io.micronaut.projectgen.core.template.TemplateRenderer;
import io.micronaut.projectgen.core.utils.NameUtils;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Default implementation of {@link ProjectGenerator}.
 */
@Singleton
public class DefaultProjectGenerator implements ProjectGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultProjectGenerator.class);
    private final ContextFactory contextFactory;
    private final List<AvailableFeatures> availableFeaturesList;

    public DefaultProjectGenerator(ContextFactory contextFactory,
                                   List<AvailableFeatures> availableFeaturesList) {
        this.contextFactory = contextFactory;
        this.availableFeaturesList = availableFeaturesList;
    }

    @Override
    public void generate(Options options,
                         OutputHandler outputHandler,
                         ConsoleOutput consoleOutput) throws Exception {
        Project project = NameUtils.parse(options.name());
        GeneratorContext generatorContext = createGeneratorContext(project, options, consoleOutput);
        generatorContext.applyFeatures();
        renderTemplates(outputHandler, project, generatorContext);
    }

    @Override
    public void writeTo(@NonNull Options options,
                        @NonNull File outputFolder) {
        try {
            OutputHandler outputHandler = new FileSystemOutputHandler(outputFolder, ConsoleOutput.NOOP);
            generate(options, outputHandler);
        } catch (IOException e) {
            LOG.error("IOException while generating the zip file: {}", e.getMessage());

        } catch (Exception e) {
            LOG.error("Exception while generating the zip file: {}", e.getMessage());
        }
    }

    /**
     *
     * @param project Project
     * @param options Options
     * @param consoleOutput ConsoleOutput
     * @return A Generator Context
     */
    public GeneratorContext createGeneratorContext(Project project,
                                                   Options options,
                                                   ConsoleOutput consoleOutput) {
        List<String> selectedFeatures = options.features();
        AvailableFeatures availableFeatures = availableFeaturesList.stream()
            .filter(feat -> feat.supports(options))
            .findFirst()
            .orElseThrow();
        FeatureContext featureContext = contextFactory.createFeatureContext(availableFeatures, selectedFeatures, options);
        return contextFactory.createGeneratorContext(project, featureContext, consoleOutput);
    }

    private void renderTemplates(OutputHandler outputHandler, Project project, GeneratorContext generatorContext) throws Exception {
        try (TemplateRenderer templateRenderer = TemplateRenderer.create(project, outputHandler)) {
            ModuleContext moduleContext = generatorContext.getRootModule();
            renderTemplates(templateRenderer, moduleContext);
            for (String name : generatorContext.getModuleNames()) {
                renderTemplates(templateRenderer, generatorContext.getModuleByName(name));
            }
        }
    }

    private void renderTemplates(TemplateRenderer templateRenderer, ModuleContext moduleContext) throws Exception {
        for (Template template : moduleContext.templates().values()) {
            RenderResult renderResult = templateRenderer.render(template);
            if (renderResult.getError() != null) {
                throw renderResult.getError();
            }
        }
    }
}
