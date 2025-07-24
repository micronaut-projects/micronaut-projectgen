package io.micronaut.projectgen.micronaut.features.other;

import io.micronaut.projectgen.core.io.PreviewGenerator;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.OptionsFixture;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class OpenApiExplorerTest {
    @Test
    void openapiExplorerFeaturesAddsTheLinkInReadmeFile(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("openapi-explorer")).build();
        Map<String, String> project = previewGenerator.generate(options);
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://micronaut-projects.github.io/micronaut-openapi/latest/guide/#openapiExplorer"));
        assertTrue(readme.contains("https://github.com/Authress-Engineering/openapi-explorer"));
    }
}
