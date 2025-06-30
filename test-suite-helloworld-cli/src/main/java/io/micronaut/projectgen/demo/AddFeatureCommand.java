package io.micronaut.projectgen.demo;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.ReflectiveAccess;
import io.micronaut.projectgen.openrewrite.OpenRewriteConfiguration;
import io.micronaut.projectgen.openrewrite.OpenRewriteRecipesRunner;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

import static io.micronaut.projectgen.core.utils.OperatingSystemUtils.getOperatingSystem;
import static picocli.CommandLine.Help.Ansi.AUTO;

@Command(
    name = "add",
    description = "add feature to an existing project"
)
public class AddFeatureCommand implements Runnable {
    @CommandLine.Spec
    @ReflectiveAccess
    protected CommandLine.Model.CommandSpec spec;

    @CommandLine.Option(
        names = { "--project", "-p" },
        required = true,
        description = "The project folder where the feature will be generated")
    private File projectDir;

    @Named("gradle")
    @Inject
    private OpenRewriteRecipesRunner gradleRecipeRunner;

    @Named("maven")
    @Inject
    private OpenRewriteRecipesRunner mavenRecipeRunner;

    @Override
    public void run() {
        //TODO Given a list of features, get the recipes
        List<String> recipes = HelloWorldTest.RECIPES;
        OpenRewriteConfiguration configuration = OpenRewriteConfiguration.builder()
            .activeRecipes(recipes)
            .exportDatatables(true)
            .recipeChangeLogLevel("INFO")
            .operatingSystem(getOperatingSystem())
            .build();
        //gradleRecipeRunner.run(recipes, projectDir, configuration, this::out, this::err);
        mavenRecipeRunner.run(recipes, projectDir, configuration, this::out, this::err);
    }

    public void out(String message) {
        outWriter().ifPresent(writer -> writer.println(AUTO.string(message)));
    }

    public void err(String message) {
        errWriter().ifPresent(writer -> writer.println(AUTO.string("@|bold,red | Error|@ " + message)));
    }

    @NonNull
    public Optional<CommandLine.Model.CommandSpec> getSpec() {
        return Optional.ofNullable(spec);
    }

    @NonNull
    public Optional<PrintWriter> outWriter() {
        return getSpec().map(spec -> spec.commandLine().getOut());
    }

    @NonNull
    public Optional<PrintWriter> errWriter() {
        return getSpec().map(spec -> spec.commandLine().getErr());
    }

}
