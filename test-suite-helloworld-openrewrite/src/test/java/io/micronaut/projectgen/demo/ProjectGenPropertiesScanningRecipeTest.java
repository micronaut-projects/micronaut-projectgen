package io.micronaut.projectgen.demo;

import static org.openrewrite.properties.Assertions.properties;
import org.junit.jupiter.api.Test;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;
import static org.openrewrite.test.SourceSpecs.text;
import java.nio.file.Path;

class ProjectGenPropertiesScanningRecipeTest implements RewriteTest {
    @Override
    public void defaults(RecipeSpec spec) {
        spec.recipe(new HelloWorldTestRecipe());
    }

    @Test
    void verifyAFileIsGenerated() {
        rewriteRun(
            properties(
                """
            artifact=demo-project
            java=JDK_21
            buildTools[0]=maven
            buildTools[1]=gradle
            gradleDsl=KOTLIN
            name=demo
            packageName=com.example
            version=1.0.0
            group=io.micronaut.projectgen""",
                spec -> spec.path(Path.of("projectgen.properties"))
            ),
            text(null,
                """
            package com.example;

            import org.junit.jupiter.api.Test;

            import static org.junit.jupiter.api.Assertions.assertEquals;

            class HelloWorldTest {

                @Test
                void testHello() {
                    assertEquals("Hello, World!", HelloWorld.hello());
                }
            }""",
                spec -> spec.path(Path.of("src/test/java/com/example/HelloWorldTest.java"))
            )
        );
    }
}
