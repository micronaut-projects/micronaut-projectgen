package io.micronaut.projectgen.core.template;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.config.ApplicationConfiguration;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.io.PreviewGenerator;
import io.micronaut.projectgen.core.options.ConfigurationFormat;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.OptionsFixture;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Property(name = "spec.name", value = "YamlTemplateTest")
@MicronautTest
class YamlTemplateTest {

    @Test
    void propertyWithComments(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle()
            .configurationFormat(ConfigurationFormat.YAML)
            .features(List.of("default-port-feature")).build();
        Map<String, String> project = previewGenerator.generate(options);
        String applicationProperties = project.get("src/main/resources/application.yml");
        assertEquals("""
micronaut:
  application:
    name: demo
  server:
    port: 8090
""", applicationProperties);
    }

    @Requires(property = "spec.name", value = "YamlTemplateTest")
    @Singleton
    static class ChangeDefaultPortFeature implements Feature {

        @Override
        public String getName() {
            return "default-port-feature";
        }

        @Override
        public void apply(GeneratorContext generatorContext) {
            ModuleContext module = generatorContext.getRootModule();
            ApplicationConfiguration configuration = module.configuration();
            configuration.blankLine();
            configuration.comment("General Server Configuration");
            configuration.put("micronaut.server.port", 8090);
        }
    }
}
