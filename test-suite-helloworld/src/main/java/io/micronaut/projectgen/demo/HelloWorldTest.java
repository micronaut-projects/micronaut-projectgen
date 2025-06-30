package io.micronaut.projectgen.demo;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.core.template.StringTemplate;
import jakarta.inject.Singleton;

@Requires(property = "hello-world-test-with-openrewrite", value = StringUtils.FALSE, defaultValue = StringUtils.FALSE)
@Singleton
public class HelloWorldTest implements Feature {
    private final JunitJupiter jupiter;

    public HelloWorldTest(JunitJupiter jupiter) {
        this.jupiter = jupiter;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeature(jupiter);
    }

    @Override
    public String getName() {
        return "hello-world-test";
    }

    @Override
    public String getTitle() {
        return "Add Test";
    }
    @Override
    public String getDescription() {
        return "Add Unit tests with Junit5 dependency";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        ModuleContext module = generatorContext.getRootModule();
        addHelloWorldTestJavaClass(module);
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
