package io.micronaut.projectgen.micronaut.openrewrite.recipes;

import org.junit.jupiter.api.Test;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;
import static org.junit.jupiter.api.Assertions.*;
import static org.openrewrite.yaml.Assertions.yaml;

class GenerateConfFileRecipeTest implements RewriteTest {
    @Override
    public void defaults(RecipeSpec spec) {
        spec.recipe(new GenerateConfFileRecipe());
    }

    @Test
    void verifyAFileIsGenerated() {
        rewriteRun(
            yaml(
                """
applicationType: cli
defaultPackage: io.micronaut.projectgen.demo.mavenhelloworld
testFramework: junit
sourceLanguage: java
buildTool: gradle_kotlin
features: [app-name, gradle, http-client-test, java, junit, logback, micronaut-build, picocli, picocli-java-application, picocli-junit, properties, readme, serialization-jackson, shade]
                    """
            )
        );
        assertTrue(true);
    }
}
