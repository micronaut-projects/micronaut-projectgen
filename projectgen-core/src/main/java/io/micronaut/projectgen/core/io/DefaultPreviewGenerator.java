package io.micronaut.projectgen.core.io;

import io.micronaut.projectgen.core.generator.ProjectGenerator;
import io.micronaut.projectgen.core.options.Options;
import jakarta.inject.Singleton;

import java.util.Map;

@Singleton
class DefaultPreviewGenerator implements PreviewGenerator {
    private final ProjectGenerator projectGenerator;

    DefaultPreviewGenerator(ProjectGenerator projectGenerator) {
        this.projectGenerator = projectGenerator;
    }

    @Override
    public Map<String, String> generate(Options options) throws Exception {
        MapOutputHandler outputHandler = new MapOutputHandler();
        projectGenerator.generate(options, outputHandler);
        return outputHandler.getProject();
    }
}
