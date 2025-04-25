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
package io.micronaut.projectgen.core.feature;

import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.feature.config.Configuration;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.template.Template;

import java.util.function.Function;

/**
 * Configuration feature.
 */
public interface ConfigurationFeature extends OneOfFeature {

    @Override
    default Class<?> getFeatureClass() {
        return ConfigurationFeature.class;
    }

    Function<Configuration, Template> createTemplate(String module);

    @Override
    default void apply(GeneratorContext generatorContext) {
        ModuleContext module = generatorContext.getRootModule();
        addTemplatesForConfigurations(module, "");
        for (String name : generatorContext.getModuleNames()) {
            module = generatorContext.getModuleByName(name);
            addTemplatesForConfigurations(module, name);
        }
    }

    default void addTemplatesForConfigurations(ModuleContext module, String name) {
        Function<Configuration, Template> createTemplateFunc = createTemplate(name);
        for (Configuration config : module.getConfigurations()) {
            if (!config.isEmpty()) {
                String templateName = StringUtils.isEmpty(name)
                    ? config.getTemplateKey()
                    : name + "/" + config.getTemplateKey();
                module.addTemplate(templateName, createTemplateFunc.apply(config));
            }
        }
    }
}
