package io.micronaut.projectgen.micronaut.features.messaging.jms;

import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.generator.ProjectGenerator;
import io.micronaut.projectgen.core.io.MapOutputHandler;
import io.micronaut.projectgen.core.io.PreviewGenerator;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.OptionsFixture;
import io.micronaut.projectgen.test.BuildTestVerifier;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class OracleAdvancedQueuingTest {
    @Test
    void oracleAqFeaturesAddsTheDependency(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle("jms-oracle-aq");
        Map<String, String> project = previewGenerator.generate(options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("javax.transaction", "jta", Scope.COMPILE, "1.1", false), buildGradle);
        assertTrue(verifier.hasDependency("com.oracle.database.messaging", "aqapi", Scope.COMPILE, "19.3.0.0", false), buildGradle);
    }

    @Test
    void oracleAqFeaturesAddsTheLinkInReadmeFile(PreviewGenerator generator) throws Exception {
        Options options = OptionsFixture.defaultGradle("jms-oracle-aq");
        Map<String, String> project = generator.generate(options);
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://micronaut-projects.github.io/micronaut-jms/snapshot/guide/index.html"));
        assertTrue(readme.contains("https://docs.oracle.com/en/database/oracle/oracle-database/21/adque/aq-introduction.html"));
    }
}
