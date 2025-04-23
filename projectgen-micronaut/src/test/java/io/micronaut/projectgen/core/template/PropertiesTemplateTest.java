package io.micronaut.projectgen.core.template;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.config.ApplicationConfiguration;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.io.MapOutputHandler;
import io.micronaut.projectgen.micronaut.MicronautOptions;
import io.micronaut.projectgen.micronaut.MicronautProjectGenerator;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Property(name = "spec.name", value = "ProjectTemplateTest")
@MicronautTest
class PropertiesTemplateTest {

    @Test
    void propertyWithComments(MicronautProjectGenerator projectGenerator) throws Exception {
        MicronautOptions options = MicronautOptions.builder().feature("default-port-feature").build();
        MapOutputHandler outputHandler = new MapOutputHandler();
        projectGenerator.generate(options, outputHandler);
        Map<String, String> project = outputHandler.getProject();
        String applicationProperties = project.get("src/main/resources/application.properties");
        assertEquals("""
            micronaut.application.name=demo

            # General Server Configuration
            micronaut.server.port=8090
            """, applicationProperties);
    }

    @Requires(property = "spec.name", value = "ProjectTemplateTest")
    @Singleton
    static class ChangeDefaultPortFeature implements Feature {

        @Override
        public String getName() {
            return "default-port-feature";
        }

        @Override
        public void apply(GeneratorContext generatorContext) {
            ApplicationConfiguration configuration = generatorContext.getConfiguration();
            configuration.blankLine();
            configuration.comment("General Server Configuration");
            configuration.put("micronaut.server.port", "8090");
        }
    }
}
