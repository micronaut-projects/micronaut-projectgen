package io.micronaut.projectgen.core.io;

import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.options.GenericOptionsBuilder;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class PreviewGeneratorTest {

    @Test
    void preview(PreviewGenerator previewGenerator) throws Exception {
        Options options = GenericOptionsBuilder.builder().buildTools(List.of(BuildTool.GRADLE)).name("demo").build();
        Map<String, String> preview = previewGenerator.generate(options);
        assertNotNull(preview);
        assertFalse(preview.keySet().isEmpty());
        assertTrue(preview.containsKey("gradle/wrapper/gradle-wrapper.properties"));
        assertTrue(preview.containsKey("gradle/wrapper/gradle-wrapper.jar"));
        assertTrue(preview.containsKey("gradlew"));
        assertTrue(preview.containsKey("gradlew.bat"));
    }

}
