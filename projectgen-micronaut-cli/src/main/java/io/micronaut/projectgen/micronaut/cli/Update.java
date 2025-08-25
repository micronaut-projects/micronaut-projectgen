package io.micronaut.projectgen.micronaut.cli;

import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.template.StringTemplate;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class Update implements OpenRewriteFeature {

    public static final List<String> RECIPES = List.of(
        "io.micronaut.starter.AddRewriteMicronautDependency",
        "io.micronaut.starter.update.gradle.micronaut.version",
        "io.micronaut.starter.update.micronaut.gradle.plugin.version",
        "io.micronaut.starter.update.shadow.gradle.plugin.version"
    );

    @Override
    public String getName() {
        return "update-micronaut";
    }

    @Override
    public String getTitle() {
        return "Apply recipes to update micronaut";
    }
    @Override
    public String getDescription() {
        return "Apply recipes to update micronaut";
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return RECIPES;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        OpenRewriteFeature.super.apply(generatorContext);
    }

}
