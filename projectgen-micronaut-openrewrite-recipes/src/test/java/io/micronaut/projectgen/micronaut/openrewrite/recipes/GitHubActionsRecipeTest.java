package io.micronaut.projectgen.micronaut.openrewrite.recipes;

import org.junit.jupiter.api.Test;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;
import org.openrewrite.test.SourceSpecs;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.openrewrite.yaml.Assertions.yaml;
import static org.openrewrite.test.SourceSpecs.text;

class GitHubActionsRecipeTest implements RewriteTest {
    @Override
    public void defaults(RecipeSpec spec) {
        spec.recipe(new GitHubActionsRecipe());
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
buildTool: maven
features: [app-name, gradle, http-client-test, java, junit, logback, micronaut-build, picocli, picocli-java-application, picocli-junit, properties, readme, serialization-jackson, shade]
                    """,
                spec -> spec.path(Path.of("micronaut-cli.yml"))
            ),
            text(null,
                """
                    name: Java CI with Maven
                    on:
                      push:
                        branches: [ main ]
                      pull_request:
                        branches: [ main ]

                    jobs:
                      build:
                        runs-on: ubuntu-latest
                        steps:
                        - uses: actions/checkout@v3
                        - name: Set up JDK 21
                          uses: actions/setup-java@v3
                          with:
                            java-version: 21
                            distribution: temurin
                            cache: maven
                       - name: Build with Maven
                         run: mvn -B verify --file pom.xml""",
                spec -> spec.path(Path.of(".github/workflows/gradle.ym"))
            )
        );
  }
}
