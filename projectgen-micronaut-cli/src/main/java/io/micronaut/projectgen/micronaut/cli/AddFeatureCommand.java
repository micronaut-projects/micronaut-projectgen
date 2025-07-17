package io.micronaut.projectgen.micronaut.cli;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.ReflectiveAccess;
import io.micronaut.projectgen.core.feature.AvailableFeatures;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.generator.ContextFactory;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.io.ConsoleOutput;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.micronaut.features.cli.MicronautCli;
import io.micronaut.projectgen.openrewrite.OpenRewriteConfiguration;
import io.micronaut.projectgen.openrewrite.OpenRewriteRecipesRunner;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
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

    @CommandLine.Option(names = {"-f", "--features"}, paramLabel = "FEATURE", split = ",",
        description = "The features to use.")
    @ReflectiveAccess
    protected List<String> features = new ArrayList<>();

    @Inject
    ContextFactory contextFactory;

    @Inject
    List<AvailableFeatures> availableFeatures;

    @Override
    public void run() {
        Options options = MicronautCli.load(projectDir);
        List<String> recipes = new ArrayList<>();
        GeneratorContext generatorContext = contextFactory.createGeneratorContext(availableFeatures , options, ConsoleOutput.NOOP);
        for (String featureName : features) {
            Feature feature = generatorContext.getFeatures().getFeatures().stream()
                .filter(f -> f.getName().equals(featureName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Feature " + featureName + " not found"));
            if (!(feature instanceof OpenRewriteFeature)) {
                throw new IllegalArgumentException("Feature " + featureName + " does not support OpenRewrite recipes");
            }
            OpenRewriteFeature openRewriteFeature = (OpenRewriteFeature) feature;
            recipes.addAll(openRewriteFeature.getRecipes(generatorContext));
        }
        OpenRewriteConfiguration configuration = OpenRewriteConfiguration.builder()
            .activeRecipes(recipes)
            .exportDatatables(true)
            .recipeChangeLogLevel("INFO")
            .operatingSystem(getOperatingSystem())
            .build();

        if (OptionUtils.hasGradleBuildTool(options)) {
            gradleRecipeRunner.run(recipes, projectDir, configuration, this::out, this::err);
        }
        if (OptionUtils.hasMavenBuildTool(options)) {
            mavenRecipeRunner.run(recipes, projectDir, configuration, this::out, this::err);
        }
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
