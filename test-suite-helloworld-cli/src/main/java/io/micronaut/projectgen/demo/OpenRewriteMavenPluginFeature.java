package io.micronaut.projectgen.demo;

import io.micronaut.projectgen.core.buildtools.maven.MavenPlugin;
import io.micronaut.projectgen.core.feature.DefaultFeature;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.options.Options;
import jakarta.inject.Singleton;

import java.util.Set;

@Singleton
public class OpenRewriteMavenPluginFeature implements DefaultFeature {
    @Override
    public String getName() {
        return "openrewrite-rewrite-maven-plugin";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        ModuleContext module = generatorContext.getRootModule();
        module.addBuildPlugin(MavenPlugin.builder()
            .groupId("org.openrewrite.maven")
            .artifactId("rewrite-maven-plugin")
            .version("6.11.0")
            .extension("""
                 <plugin>
                        <groupId>org.openrewrite.maven</groupId>
                        <artifactId>rewrite-maven-plugin</artifactId>
                        <version>6.11.0</version>
                        <dependencies>
                          <dependency>
                            <groupId>io.micronaut.projectgen</groupId>
                            <artifactId>test-suite-helloworld-openrewrite</artifactId>
                            <version>0.0.1-SNAPSHOT</version>
                          </dependency>
                        </dependencies>
                      </plugin>
                """)
            .build());
    }

    @Override
    public boolean shouldApply(Options options, Set<Feature> selectedFeatures) {
        return true;
    }
}
