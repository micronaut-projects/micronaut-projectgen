package io.micronaut.projectgen.demo;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.projectgen.core.buildtools.BuildProperties;
import io.micronaut.projectgen.core.buildtools.MavenCentral;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.buildtools.gradle.GradlePlugin;
import io.micronaut.projectgen.core.buildtools.maven.MavenPlugin;
import io.micronaut.projectgen.core.feature.DefaultFeature;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.utils.OptionUtils;
import jakarta.inject.Singleton;
import io.micronaut.projectgen.core.template.StringTemplate;
import java.util.Set;

@Singleton
class Project implements DefaultFeature {
    private static final Dependency DEPENDENCY_JUNIT_JUPITER = Dependency.builder()
        .groupId("org.junit.jupiter")
        .artifactId("junit-jupiter")
        .version("5.10.2")
        .test()
        .build();
    private static final @NonNull MavenPlugin MAVEN_PLUGIN_SUREFIRE = MavenPlugin.builder()
        .groupId("org.apache.maven.plugins")
        .artifactId("maven-surefire-plugin")
        .version("3.1.2")
        .build();

    @Override
    public boolean shouldApply(Options options, Set<Feature> selectedFeatures) {
        return true;
    }

    @Override
    public String getName() {
        return "entry-point";
    }

    @Override
    public String getDescription() {
        return "It generates a Hello World Maven and Gradle project";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        ModuleContext module = generatorContext.getRootModule();
        module.addDependency(DEPENDENCY_JUNIT_JUPITER);
        Options options = generatorContext.getOptions();
        populateModuleAttributes(module, options);
        if (OptionUtils.hasMavenBuildTool(options)) {
            addMavenBuildProperties(module, options);
            addMavenJarPlugin(module, options);
            module.addBuildPlugin(MAVEN_PLUGIN_SUREFIRE);
        }
        if (OptionUtils.hasGradleBuildTool(options)) {
            addJavaGradlePlugin(module, options);
            addApplicationGradlePlugin(module, options);
            module.repositories().add(new MavenCentral());
        }
        addHelloWorldJavaClass(module);
        addHelloWorldTestJavaClass(module);
    }

    private void populateModuleAttributes(ModuleContext module, Options options) {
        module.moduleAttributes()
            .setCoordinate(Dependency.builder()
                .groupId(options.group())
                .artifactId(options.artifact())
                .version(options.version())
                .build());
    }

    private void addJavaGradlePlugin(ModuleContext module, Options options) {
        int javaVersion = options.java().majorVersion();
        module.addBuildPlugin(GradlePlugin.builder().id("java")
            .extension(String.format("""
    java {
        sourceCompatibility = JavaVersion.VERSION_%1$d
        targetCompatibility = JavaVersion.VERSION_%1$d
    }
    tasks.test {
        useJUnitPlatform()
    }
    """, javaVersion))
            .build());
    }

    private void addApplicationGradlePlugin(ModuleContext module, Options options) {
        module.addBuildPlugin(GradlePlugin.builder().id("application")
            .extension("""
            application {
                mainClass.set("%s.HelloWorld")
            }
            """.formatted(options.packageName()))
            .build());
    }

    private void addMavenBuildProperties(ModuleContext module, Options options) {
        BuildProperties buildProperties = module.buildProperties();
        String java = String.valueOf(options.java().majorVersion());
        buildProperties.put("maven.compiler.source", java);
        buildProperties.put("maven.compiler.target", java);
    }

    private void addMavenJarPlugin(ModuleContext module, Options options) {
        module.addBuildPlugin(MavenPlugin.builder()
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
            </plugin>""", options.packageName()))
            .build());
    }

    private void addHelloWorldJavaClass(ModuleContext module) {
        String path = "src/main/java/com/example/HelloWorld.java";
        module.addTemplate("HelloWorld.java", new StringTemplate(path, """
            package com.example;

            public class HelloWorld {
                public static void main(String[] args) {
                    System.out.println(hello());
                }

                public static String hello() {
                    return "Hello, World!";
                }
            }
            """));
    }

    private void addHelloWorldTestJavaClass(ModuleContext module) {
        String path = "src/test/java/com/example/HelloWorldTest.java";
        module.addTemplate("HelloWorldTest.java", new StringTemplate(path, """
            package com.example;

            import org.junit.jupiter.api.Test;

            import static org.junit.jupiter.api.Assertions.assertEquals;

            class HelloWorldTest {

                @Test
                void testHello() {
                    assertEquals("Hello, World!", HelloWorld.hello());
                }
            }"""));
    }
}
