/*
 * Copyright 2017-2024 original authors
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
package io.micronaut.starter.feature.chatbots.basecamp;

import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.rocker.RockerWritable;
import io.micronaut.starter.feature.chatbots.ChatBotType;
import io.micronaut.starter.feature.chatbots.ChatBots;
import io.micronaut.projectgen.micronaut.template.chatbots.basecamp.aboutCommandHandlerGroovy;
import io.micronaut.projectgen.micronaut.template.chatbots.basecamp.aboutCommandHandlerGroovyJunit;
import io.micronaut.projectgen.micronaut.template.chatbots.basecamp.aboutCommandHandlerGroovySpock;
import io.micronaut.projectgen.micronaut.template.chatbots.basecamp.aboutCommandHandlerJava;
import io.micronaut.projectgen.micronaut.template.chatbots.basecamp.aboutCommandHandlerJavaJunit;
import io.micronaut.projectgen.micronaut.template.chatbots.basecamp.aboutCommandHandlerKotlin;
import io.micronaut.projectgen.micronaut.template.chatbots.basecamp.aboutCommandHandlerKotlinJunit;
import io.micronaut.projectgen.micronaut.template.chatbots.basecamp.finalCommandHandlerGroovy;
import io.micronaut.projectgen.micronaut.template.chatbots.basecamp.finalCommandHandlerJava;
import io.micronaut.projectgen.micronaut.template.chatbots.basecamp.finalCommandHandlerKotlin;
import io.micronaut.projectgen.micronaut.template.chatbots.basecamp.mockAboutCommandJson;
import io.micronaut.projectgen.micronaut.template.chatbots.basecamp.basecampReadme;
import io.micronaut.projectgen.micronaut.template.chatbots.basecamp.about;
import io.micronaut.projectgen.micronaut.features.validator.MicronautValidationFeature;
import io.micronaut.projectgen.core.options.TestFramework;
import io.micronaut.projectgen.core.rocker.RockerTemplate;

/**
 * Base class for Telegram chatbot features.
 *
 * @since 4.3.0
 * @author Tim Yates
 */
abstract class ChatBotsBasecamp extends ChatBots {

    protected ChatBotsBasecamp(MicronautValidationFeature validationFeature) {
        super(validationFeature);
    }

    @Override
    protected void addConfigurations(ModuleContext module) {
        module.configuration().put(
                "micronaut.chatbots.folder",
                "botcommands"
        );
    }

    @Override
    protected void renderTemplates(GeneratorContext generatorContext, ModuleContext module) {
        module.addTemplate(
                "about-html",
                new RockerTemplate("src/main/resources/botcommands/about.html", about.template())
        );
        module.addTemplate(
            generatorContext.getOptions().language(),
                "basecamp-about-command-handler",
                generatorContext.getSourcePath("/{packagePath}/BasecampAboutCommandHandler"),
                aboutCommandHandlerJava.template(generatorContext.getProject()),
                aboutCommandHandlerKotlin.template(generatorContext.getProject()),
                aboutCommandHandlerGroovy.template(generatorContext.getProject())
        );
        if (!generatorContext.getTestFramework().isKotlinTestFramework()) {
            module.addTemplate(
                    "mock-basecamp-about-command-json",
                    new RockerTemplate(
                            "src/test/resources/mockBasecampAboutCommand.json",
                            mockAboutCommandJson.template()
                    )
            );
        }
        if (generatorContext.getTestFramework() == TestFramework.JUNIT) {
            module.addTemplate(
                generatorContext.getOptions().language(),
                    "about-command-handler-junit-test",
                    generatorContext.getTestSourcePath("/{packagePath}/BasecampAboutCommandHandler"),
                    aboutCommandHandlerJavaJunit.template(generatorContext.getProject()),
                    aboutCommandHandlerKotlinJunit.template(generatorContext.getProject()),
                    aboutCommandHandlerGroovyJunit.template(generatorContext.getProject())
            );
        } else if (generatorContext.getTestFramework() == TestFramework.SPOCK) {
            module.addTemplate(
                    "about-command-handler-spock-groovy-test",
                    new RockerTemplate(generatorContext.getTestSourcePath("/{packagePath}/BasecampAboutCommandHandler"), aboutCommandHandlerGroovySpock.template(generatorContext.getProject()))
            );
        }

        module.addHelpTemplate(new RockerWritable(basecampReadme.template(rootReadMeTemplate(generatorContext))));

        module.addTemplate(
            generatorContext.getOptions().language(),
                "final-command-handler",
                generatorContext.getSourcePath("/{packagePath}/BasecampFinalCommandHandler"),
                finalCommandHandlerJava.template(generatorContext.getProject()),
                finalCommandHandlerKotlin.template(generatorContext.getProject()),
                finalCommandHandlerGroovy.template(generatorContext.getProject())
        );
    }

    @Override
    public String getThirdPartyDocumentation(GeneratorContext generatorContext) {
        return "https://github.com/basecamp/bc3-api/blob/master/sections/chatbots.md";
    }

    @Override
    public ChatBotType getChatBotType() {
        return ChatBotType.BASECAMP;
    }
}
