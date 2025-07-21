package io.micronaut.projectgen.micronaut.features;

import io.micronaut.projectgen.core.generator.ProjectGenerator;
import io.micronaut.projectgen.core.io.PreviewGenerator;
import io.micronaut.projectgen.core.openrewrite.FileContents;
import io.micronaut.projectgen.core.openrewrite.RecipeFetcher;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.OptionsFixture;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class RecipeBootstrapPropertiesTest {
    @Test
    void recipeCreatesBootstrapPropertiesFileTest(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("aws-parameter-store")).build();
        Map<String, String> project = previewGenerator.generate(options);

        String bootstrapFile = project.get("src/main/resources/bootstrap.properties");
        assertNotNull(bootstrapFile, "Bootstrap properties file created");
    }
}
