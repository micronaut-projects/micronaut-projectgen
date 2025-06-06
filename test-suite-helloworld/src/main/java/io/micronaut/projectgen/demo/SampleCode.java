package io.micronaut.projectgen.demo;

import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.template.StringTemplate;
import jakarta.inject.Singleton;

@Singleton
class SampleCode implements Feature {
    @Override
    public String getName() {
        return "sample-code";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        ModuleContext module = generatorContext.getRootModule();
        addHelloWorldJavaClass(module);
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    private void addHelloWorldJavaClass(ModuleContext module) {
        String path = "src/main/java/com/example/HelloWorld.java";
        module.addTemplate("HelloWorld.java", new io.micronaut.projectgen.core.template.StringTemplate(path, """
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
}
