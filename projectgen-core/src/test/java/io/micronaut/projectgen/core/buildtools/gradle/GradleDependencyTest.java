package io.micronaut.projectgen.core.buildtools.gradle;

import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.feature.AvailableFeatures;
import io.micronaut.projectgen.core.generator.ContextFactory;
import io.micronaut.projectgen.core.generator.DefaultProjectGenerator;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.generator.Project;
import io.micronaut.projectgen.core.io.ConsoleOutput;
import io.micronaut.projectgen.core.options.GenericOptionsBuilder;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.utils.NameUtils;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest(startApplication = false)
class GradleDependencyTest {
    @Inject
    ContextFactory contextFactory;

    @Inject
    List<AvailableFeatures> availableFeaturesList;

    @Test
    void gradleDependencyWithComments() {
        Options options = GenericOptionsBuilder.builder().packageName("com.example").name("demo").build();
        Project project = NameUtils.parse(options);
        GeneratorContext generatorContext = contextFactory.createGeneratorContext(availableFeaturesList, options, ConsoleOutput.NOOP);
        GradleDependency gradleDependency = new GradleDependency(
            Dependency.builder().groupId("org.codehaus.groovy").artifactId("groovy-nio").version("3.0.5").scope(Scope.COMPILE).build(),
            options,
            generatorContext,
            false,
            null,
            "Configuration Name + Dependency Notation - GroupID : ArtifactID (Name) : Version");
        String snippet = gradleDependency.toSnippet();
        assertNotNull(snippet);
        assertEquals(snippet, """
            /* Configuration Name + Dependency Notation - GroupID : ArtifactID (Name) : Version */
                implementation("org.codehaus.groovy:groovy-nio:3.0.5")""");
    }
}
