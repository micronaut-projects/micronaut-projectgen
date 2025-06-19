package io.micronaut.projectgen.demo;

import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.template.StringTemplate;
import jakarta.inject.Singleton;

import java.util.List;

import static io.micronaut.projectgen.demo.GenerateHelloWorldTestFile.PATH;

@Singleton
public class HelloWorldTest implements OpenRewriteFeature {
    public static final List<String> RECIPES = List.of(
        "io.micronaut.projectgen.demo.junit-jupiter",
        "io.micronaut.projectgen.demo.GenerateHelloWorldTestFile"
    );

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
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return RECIPES;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        OpenRewriteFeature.super.apply(generatorContext);
        ModuleContext module = generatorContext.getRootModule();
        addHelloWorldTestJavaClass(module, generatorContext.getOptions());
    }

    private void addHelloWorldTestJavaClass(ModuleContext module, Options options) {
        module.addTemplate("HelloWorldTest.java",
            new StringTemplate(PATH, GenerateHelloWorldTestFile.fileContents(options)));
    }
}
