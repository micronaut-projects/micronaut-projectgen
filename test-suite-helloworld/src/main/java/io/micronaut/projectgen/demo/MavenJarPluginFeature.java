package io.micronaut.projectgen.demo;

import io.micronaut.projectgen.core.buildtools.maven.MavenPlugin;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import jakarta.inject.Singleton;

@Singleton
class MavenJarPluginFeature implements Feature {
    @Override
    public String getName() {
        return "maven-jar-plugin";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getRootModule().addBuildPlugin(MavenPlugin.builder()
            .groupId("org.apache.maven.plugins")
            .artifactId("maven-jar-plugin")
            .extension(String.format("""
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <version>3.3.0</version>
                <configuration>
                    <archive>
                        <manifest>
                            <mainClass>%s.HelloWorld</mainClass>
                        </manifest>
                    </archive>
                </configuration>
            </plugin>""", generatorContext.getOptions().packageName()))
            .build());
    }
}
