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
package io.micronaut.starter.feature.chatbots.telegram;

import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.rocker.RockerWritable;
import io.micronaut.starter.feature.chatbots.ChatBotType;
import io.micronaut.starter.feature.chatbots.ChatBots;
import io.micronaut.projectgen.micronaut.template.chatbots.telegram.aboutCommandHandlerGroovy;
import io.micronaut.projectgen.micronaut.template.chatbots.telegram.aboutCommandHandlerGroovyJunit;
import io.micronaut.projectgen.micronaut.template.chatbots.telegram.aboutCommandHandlerGroovySpock;
import io.micronaut.projectgen.micronaut.template.chatbots.telegram.aboutCommandHandlerJava;
import io.micronaut.projectgen.micronaut.template.chatbots.telegram.aboutCommandHandlerJavaJunit;
import io.micronaut.projectgen.micronaut.template.chatbots.telegram.aboutCommandHandlerKotlin;
import io.micronaut.projectgen.micronaut.template.chatbots.telegram.aboutCommandHandlerKotlinJunit;
import io.micronaut.projectgen.micronaut.template.chatbots.telegram.finalCommandHandlerGroovy;
import io.micronaut.projectgen.micronaut.template.chatbots.telegram.finalCommandHandlerJava;
import io.micronaut.projectgen.micronaut.template.chatbots.telegram.finalCommandHandlerKotlin;
import io.micronaut.projectgen.micronaut.template.chatbots.telegram.mockAboutCommandJson;
import io.micronaut.projectgen.micronaut.template.chatbots.telegram.telegramReadme;
import io.micronaut.projectgen.micronaut.template.chatbots.telegram.about;
import io.micronaut.projectgen.micronaut.features.validator.MicronautValidationFeature;
import io.micronaut.projectgen.core.options.TestFramework;
import io.micronaut.projectgen.core.rocker.RockerTemplate;

/**
 * Base class for Telegram chatbot features.
 *
 * @since 4.3.0
 * @author Tim Yates
 */
abstract class ChatBotsTelegram extends ChatBots {

    protected ChatBotsTelegram(MicronautValidationFeature validationFeature) {
        super(validationFeature);
    }

    @Override
    protected void renderTemplates(GeneratorContext generatorContext, ModuleContext module) {
        module.addTemplate(
            "about-markdown",
            new RockerTemplate("src/main/resources/botcommands/about.md", about.template())
        );
        module.addTemplate(
            generatorContext.getOptions().language(),
            "about-command-handler",
            generatorContext.getSourcePath("/{packagePath}/AboutCommandHandler"),
            aboutCommandHandlerJava.template(generatorContext.getProject()),
            aboutCommandHandlerKotlin.template(generatorContext.getProject()),
            aboutCommandHandlerGroovy.template(generatorContext.getProject())
        );
        if (!generatorContext.getTestFramework().isKotlinTestFramework()) {
            module.addTemplate(
                "mock-about-command-json",
                new RockerTemplate(
                    "src/test/resources/mockAboutCommand.json",
                    mockAboutCommandJson.template()
                )
            );
        }
        if (generatorContext.getTestFramework() == TestFramework.JUNIT) {
            module.addTemplate(
                generatorContext.getOptions().language(),
                "about-command-handler-junit-test",
                generatorContext.getTestSourcePath("/{packagePath}/AboutCommandHandler"),
                aboutCommandHandlerJavaJunit.template(generatorContext.getProject()),
                aboutCommandHandlerKotlinJunit.template(generatorContext.getProject()),
                aboutCommandHandlerGroovyJunit.template(generatorContext.getProject())
            );
        } else if (generatorContext.getTestFramework() == TestFramework.SPOCK) {
            module.addTemplate(
                "about-command-handler-spock-groovy-test",
                new RockerTemplate(generatorContext.getTestSourcePath("/{packagePath}/AboutCommandHandler"), aboutCommandHandlerGroovySpock.template(generatorContext.getProject()))
            );
        }

        module.addHelpTemplate(new RockerWritable(telegramReadme.template(rootReadMeTemplate(generatorContext))));

        module.addTemplate(
            generatorContext.getOptions().language(),
            "final-command-handler",
            generatorContext.getSourcePath("/{packagePath}/FinalCommandHandler"),
            finalCommandHandlerJava.template(generatorContext.getProject()),
            finalCommandHandlerKotlin.template(generatorContext.getProject()),
            finalCommandHandlerGroovy.template(generatorContext.getProject())
        );
    }

    @Override
    public ChatBotType getChatBotType() {
        return ChatBotType.TELEGRAM;
    }
}
