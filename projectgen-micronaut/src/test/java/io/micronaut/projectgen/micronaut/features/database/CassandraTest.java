package io.micronaut.projectgen.micronaut.features.database;

import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.io.PreviewGenerator;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.OptionsFixture;
import io.micronaut.projectgen.test.BuildTestVerifier;
import io.micronaut.projectgen.test.ConfigurationUtils;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
class CassandraTest {
    @Test
    void cassandraFeaturesConfiguration(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("cassandra")).build();
        Map<String, String> project = previewGenerator.generate(options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        assertEquals("20", applicationProperties.getProperty("cassandra.default.advanced.control-connection.schema-agreement.timeout"));
        assertEquals("defaultSession", applicationProperties.getProperty("cassandra.default.basic.session-name"));
        assertEquals("datacenter1", applicationProperties.getProperty("cassandra.default.basic.load-balancing-policy.local-datacenter"));
        assertEquals("localhost:9042", applicationProperties.getProperty("cassandra.default.basic.contact-points[0]"));
        assertEquals("DefaultSslEngineFactory", applicationProperties.getProperty("cassandra.default.advanced.ssl-engine-factory"));
    }

    @Test
    void cassandraTestResourceFeaturesConfiguration(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("cassandra", "test-resources")).build();
        Map<String, String> project = previewGenerator.generate(options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        assertEquals("600s", applicationProperties.getProperty("test-resources.containers.cassandra.startup-timeout"));
        assertEquals("cassandra", applicationProperties.getProperty("test-resources.containers.cassandra.image-name"));
        assertEquals("9042", applicationProperties.getProperty("test-resources.containers.cassandra.exposed-ports[0].cassandra.port"));
        assertEquals("localhost:${cassandra.port}", applicationProperties.getProperty("cassandra.default.basic.contact-points[0]"));
    }

    @Test
    void cassandraMicrometerFeaturesConfiguration(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("cassandra", "micrometer-appoptics")).build();
        Map<String, String> project = previewGenerator.generate(options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        assertEquals("MicrometerMetricsFactory", applicationProperties.getProperty("cassandra.default.advanced.metrics.factory.class"));
        assertEquals("connected-nodes", applicationProperties.getProperty("cassandra.default.advanced.metrics.session.enabled[0]"));
        assertEquals("cql-requests", applicationProperties.getProperty("cassandra.default.advanced.metrics.session.enabled[1]"));
        assertEquals("bytes-sent", applicationProperties.getProperty("cassandra.default.advanced.metrics.session.enabled[2]"));
        assertEquals("bytes-received", applicationProperties.getProperty("cassandra.default.advanced.metrics.session.enabled[3]"));
        assertEquals("cql-requests", applicationProperties.getProperty("cassandra.default.advanced.metrics.node.enabled[0]"));
        assertEquals("bytes-sent", applicationProperties.getProperty("cassandra.default.advanced.metrics.node.enabled[1]"));
        assertEquals("bytes-received", applicationProperties.getProperty("cassandra.default.advanced.metrics.node.enabled[2]"));
    }

    @Test
    void CassandraWithMicrometerFeaturesAddsTheDependency(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("cassandra", "micrometer-atlas")).build();
        Map<String, String> project = previewGenerator.generate(options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("io.micronaut.cassandra", "micronaut-cassandra", Scope.COMPILE), buildGradle);
        assertTrue(verifier.hasDependency("com.datastax.oss", "java-driver-metrics-micrometer", Scope.COMPILE), buildGradle);
    }

    @Test
    void r2dbcFeaturesAddsTheLinkInReadmeFile(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("cassandra")).build();
        Map<String, String> project = previewGenerator.generate(options);
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://micronaut-projects.github.io/micronaut-cassandra/latest/guide/index.html"));
        assertTrue(readme.contains("https://docs.datastax.com/en/developer/java-driver/latest/"));
    }
}
