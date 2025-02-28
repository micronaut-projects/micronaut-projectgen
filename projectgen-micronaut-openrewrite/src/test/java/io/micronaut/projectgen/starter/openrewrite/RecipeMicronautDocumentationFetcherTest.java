package io.micronaut.projectgen.starter.openrewrite;

import io.micronaut.projectgen.core.openrewrite.RecipeFetcher;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class RecipeMicronautDocumentationFetcherTest {
    private static final String RECIPE_MOCKITO = "io.micronaut.starter.feature.mockito";
    private static final String RECIPE_VALIDATION = "io.micronaut.starter.feature.validation";

    @Inject
    RecipeFetcher fetcher;

    @Test
    void testFetchMicronautDocumentation() {
        Optional<String> documentation = fetcher.findFrameworkDocumentationByRecipeName(RECIPE_MOCKITO);
        assertFalse(documentation.isPresent());

        documentation = fetcher.findFrameworkDocumentationByRecipeName(RECIPE_VALIDATION);
        assertTrue(documentation.isPresent());
    }
}
