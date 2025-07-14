package io.micronaut.projectgen.micronaut.features.security;

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

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class SecurityOAuth2Test {
    @Test
    void securityOauth2FeaturesConfiguration(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("security-oauth2")).build();
        Map<String, String> project = previewGenerator.generate(options);
        Properties applicationProperties = ConfigurationUtils.loadApplicationProperties(project);
        Properties applicationDevProperties = ConfigurationUtils.loadDevProperties(project);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        assertEquals("cookie", applicationProperties.getProperty("micronaut.security.authentication"));
        assertEquals("XXX", applicationDevProperties.getProperty("micronaut.security.oauth2.clients.default.client-id"));
        assertEquals("XXX", applicationDevProperties.getProperty("micronaut.security.oauth2.clients.default.client-id"));
    }

    @Test
    void securityOauth2JwtFeaturesConfiguration(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("security-oauth2", "security-jwt")).build();
        Map<String, String> project = previewGenerator.generate(options);
        Properties applicationDevProperties = ConfigurationUtils.loadDevProperties(project);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        assertEquals("${OAUTH_ISSUER}", applicationDevProperties.getProperty("micronaut.security.oauth2.clients.default.openid.issuer"));
    }

    @Test
    void securityOauth2FeaturesAddsTheDependency(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("security-oauth2")).build();
        Map<String, String> project = previewGenerator.generate(options);
        String buildGradle = project.get("build.gradle.kts");
        assertNotNull(buildGradle);
        BuildTestVerifier verifier = BuildTestVerifier.of(buildGradle, options);
        assertTrue(verifier.hasDependency("io.micronaut.security", "micronaut-security-oauth2", Scope.COMPILE), buildGradle);
    }

    @Test
    void securityOauth2FeaturesAddsTheLinkInReadmeFile(PreviewGenerator previewGenerator) throws Exception {
        Options options = OptionsFixture.defaultGradle().features(List.of("security-oauth2")).build();
        Map<String, String> project = previewGenerator.generate(options);
        String readme = project.get("README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("https://micronaut-projects.github.io/micronaut-security/latest/guide/index.html#oauth"));
    }
}
