package io.micronaut.projectgen.demo;

import io.micronaut.projectgen.core.buildtools.BuildProperties;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.utils.OptionUtils;
import jakarta.inject.Singleton;

@Singleton
class MavenCompilerProperties implements Feature {
    @Override
    public String getName() {
        return "maven-compiler-properties";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        Options options = generatorContext.getOptions();
        if (OptionUtils.hasMavenBuildTool(options)) {
            BuildProperties buildProperties = generatorContext.getRootModule()
                .buildProperties();
            String java = String.valueOf(options
                .java()
                .majorVersion());
            buildProperties.put("maven.compiler.source", java);
            buildProperties.put("maven.compiler.target", java);
        }
    }
}
