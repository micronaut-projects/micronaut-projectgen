package io.micronaut.projectgen.micronaut.features.migration;

import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.io.MapOutputHandler;
import io.micronaut.projectgen.micronaut.MicronautOptions;
import io.micronaut.projectgen.micronaut.MicronautProjectGenerator;
import io.micronaut.projectgen.test.BuildTestVerifier;
import io.micronaut.projectgen.test.ConfigurationUtils;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class LiquibaseTest {

    @Test
    void liquibaseConfiguration(MicronautProjectGenerator micronautProjectGenerator) throws Exception {
        MicronautOptions options = MicronautOptions.builder().feature("liquibase").build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        assertEquals("classpath:db/liquibase-changelog.xml",
            applicationProperties.getProperty("liquibase.datasources.default.change-log"));
    }

    @Test
    void liquibaseChangeset(MicronautProjectGenerator micronautProjectGenerator) throws Exception {
        MicronautOptions options = MicronautOptions.builder().feature("liquibase").build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String liquibaseChangelogXml = project.get("src/main/resources/db/liquibase-changelog.xml");
        assertNotNull(liquibaseChangelogXml);
        assertEquals("""
        <?xml version="1.0" encoding="UTF-8" ?>
        <databaseChangeLog
          xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
                 http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.1.xsd">
          <include file="changelog/01-schema.xml" relativeToChangelogFile="true"/>
        </databaseChangeLog>
        """, liquibaseChangelogXml);
        String schemaXml = project.get("src/main/resources/db/changelog/01-schema.xml");
        assertNotNull(schemaXml);
        assertEquals("""
            <?xml version="1.0" encoding="UTF-8" ?>
            <databaseChangeLog
              xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
                     http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.1.xsd">
              <changeSet id="01" author="username">
              </changeSet>
            </databaseChangeLog>
            """, schemaXml);
    }

    @Test
    void liquibaseFeaturesAddsTheDependency(MicronautProjectGenerator micronautProjectGenerator) throws Exception {
        MicronautOptions options = MicronautOptions.builder().feature("liquibase").build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("io.micronaut.liquibase", "micronaut-liquibase", Scope.COMPILE),buildGradle);
        assertTrue(verifier.hasDependency("org.slf4j", "jul-to-slf4j", Scope.COMPILE),buildGradle);
    }

    @Test
    void liquibaseFeaturesAddsTheLinkInReadmeFile(MicronautProjectGenerator micronautProjectGenerator) throws Exception {
        MicronautOptions options = MicronautOptions.builder().feature("liquibase").build();
        Map<String, String> project = generateProject(micronautProjectGenerator, options);
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://micronaut-projects.github.io/micronaut-liquibase/latest/guide/index.html"));
        assertTrue(readme.contains("https://www.liquibase.org/"));
    }

    private static Map<String, String> generateProject(MicronautProjectGenerator micronautProjectGenerator,
                                                       MicronautOptions options) throws Exception {
        MapOutputHandler outputHandler = new MapOutputHandler();
        micronautProjectGenerator.generate(options, outputHandler);
        return outputHandler.getProject();
    }
}
