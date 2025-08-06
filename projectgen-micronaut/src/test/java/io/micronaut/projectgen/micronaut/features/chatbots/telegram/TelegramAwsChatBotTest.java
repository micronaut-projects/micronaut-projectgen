package io.micronaut.projectgen.micronaut.features.chatbots.telegram;

import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.io.PreviewGenerator;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.OptionsFixture;
import io.micronaut.projectgen.test.BuildTestVerifier;
import io.micronaut.projectgen.test.ConfigurationUtils;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class TelegramAwsChatBotTest {
    @Test
    void telegramAwsChatbotFeaturesConfiguration(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().template("function").features(List.of("chatbots-telegram-lambda")).build();
        Map<String, String> project = previewGenerator.generate(options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        assertEquals("botcommands", applicationProperties.getProperty("micronaut.chatbots.folder"));
        assertEquals("WEBHOOK_TOKEN", applicationProperties.getProperty("micronaut.chatbots.telegram.bots.example.token"));
        assertEquals("@MyMicronautExampleBot", applicationProperties.getProperty("micronaut.chatbots.telegram.bots.example.at-username"));
    }

    @Test
    void telegramAwsChatbotFeaturesAddsTheDependency(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().template("function").features(List.of("chatbots-telegram-lambda")).build();
        Map<String, String> project = previewGenerator.generate(options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("io.micronaut.chatbots", "micronaut-chatbots-telegram-lambda", Scope.COMPILE), buildGradle);
    }

    @Test
    void telegramAwsChatbotFeaturesAddsTheLinkInReadmeFile(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().template("function").features(List.of("chatbots-telegram-lambda")).build();
        Map<String, String> project = previewGenerator.generate(options);
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://micronaut-projects.github.io/micronaut-chatbots/latest/guide/"));
    }
}
