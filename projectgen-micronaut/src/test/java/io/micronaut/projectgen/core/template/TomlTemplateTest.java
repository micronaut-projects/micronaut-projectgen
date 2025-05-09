package io.micronaut.projectgen.core.template;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.config.ApplicationConfiguration;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.generator.ProjectGenerator;
import io.micronaut.projectgen.core.io.MapOutputHandler;
import io.micronaut.projectgen.core.options.GenericOptionsBuilder;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.micronaut.OptionsFixture;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Property(name = "spec.name", value = "TomlTemplateTest")
@MicronautTest
class TomlTemplateTest {

    @Test
    void propertyWithComments(ProjectGenerator projectGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle()
            .features(List.of("default-port-feature", "toml"))
            .build();
        MapOutputHandler outputHandler = new MapOutputHandler();
        projectGenerator.generate(options, outputHandler);
        Map<String, String> project = outputHandler.getProject();
        String applicationProperties = project.get("src/main/resources/application.toml");
        assertEquals("""

[micronaut]
application.name = 'demo'
server.port = 8090
""", applicationProperties);
    }

    @Requires(property = "spec.name", value = "TomlTemplateTest")
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
