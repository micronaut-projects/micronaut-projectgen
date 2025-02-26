/*
 * Copyright 2017-2025 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.projectgen.test;

import io.micronaut.context.exceptions.ConfigurationException;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.buildtools.gradle.GradleConfiguration;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.options.TestFramework;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class GradleBuildTestVerifier implements BuildTestVerifier {

    private final BuildTool buildTool;
    private final String template;
    private final Language language;
    private final TestFramework testFramework;

    public GradleBuildTestVerifier(String template, Options options) {
        this.buildTool = options.buildTools().stream().filter(BuildTool::isGradle).findFirst().orElseThrow();
        this.template = template;
        this.language = options.language();
        this.testFramework = options.testFramework();
    }

    @Override
    public boolean hasAnnotationProcessor(String groupId, String artifactId) {
        return hasDependency(groupId, artifactId, Scope.ANNOTATION_PROCESSOR);
    }

    @Override
    public boolean hasTestAnnotationProcessor(String groupId, String artifactId) {
        return hasDependency(groupId, artifactId, Scope.TEST_ANNOTATION_PROCESSOR);
    }

    @Override
    public boolean hasBom(String groupId, String artifactId, Scope scope) {
        Optional<String> gradleConfigurationNameOptional = GradleConfiguration.of(scope, language, testFramework, null)
            .map(GradleConfiguration::getConfigurationName);
        if (!gradleConfigurationNameOptional.isPresent()) {
            throw new ConfigurationException("cannot match " + scope + " to gradle configuration");
        }
        String gradleConfigurationName = gradleConfigurationNameOptional.get();
        return hasBom(groupId, artifactId, gradleConfigurationName);
    }

    @Override
    public boolean hasBom(String groupId, String artifactId, String scope) {
        Pattern pattern = Pattern.compile(scope + "[\\s(]platform\\(\"" + groupId + ":" + artifactId + ":");
        return pattern.matcher(template).find();
    }

    @Override
    public boolean hasDependency(String groupId, String artifactId, Scope scope) {
        Optional<String> gradleConfigurationNameOptional = GradleConfiguration.of(scope, language, testFramework, null)
            .map(GradleConfiguration::getConfigurationName);
        if (!gradleConfigurationNameOptional.isPresent()) {
            throw new ConfigurationException("cannot match " + scope + " to gradle configuration");
        }
        String gradleConfigurationName = gradleConfigurationNameOptional.get();
        return hasDependency(groupId, artifactId, gradleConfigurationName);
    }

    @Override
    public boolean hasDependency(String groupId, String artifactId, String scope) {
        String regex = "(?s).*" + scope + "\\(\"" + groupId + ":" + artifactId + "(?::.+)?\"\\).*";
        return Pattern.compile(regex).matcher(template).matches();
    }

    @Override
    public boolean hasDependency(String groupId, String artifactId, Scope scope, String version, boolean isProperty) {
        return GradleConfiguration.of(scope, language, testFramework, null)
            .map(config -> hasDependency(groupId, artifactId, config.getConfigurationName(), version, isProperty))
            .orElseThrow(() -> new ConfigurationException("cannot match " + scope + " to gradle configuration"));
    }

    @Override
    public boolean hasDependency(String groupId, String artifactId, String scope, String version, boolean isProperty) {
        if (isProperty) {
            return hasDependency(groupId, artifactId, scope);
        }
        String regex = "(?s).*" + scope + "\\(\"" + groupId + ":" + artifactId + ":" + version + "\"\\).*";
        return Pattern.compile(regex).matcher(template).matches();
    }

    @Override
    public boolean hasDependency(String groupId, String artifactId) {
        return Stream.of(GradleConfiguration.values())
            .map(GradleConfiguration::getConfigurationName)
            .anyMatch(scope -> hasDependency(groupId, artifactId, scope));
    }

    @Override
    public boolean hasExclusion(String groupId, String artifactId, String excludedGroupId, String excludedArtifactId) {
        return hasExclusion(groupId, artifactId, excludedGroupId, excludedArtifactId, Scope.COMPILE);
    }

    @Override
    public boolean hasExclusion(String groupId, String artifactId, String excludedGroupId, String excludedArtifactId, Scope scope) {
        Optional<String> gradleConfigurationNameOptional = GradleConfiguration.of(scope, language, testFramework, null)
            .map(GradleConfiguration::getConfigurationName);
        if (!gradleConfigurationNameOptional.isPresent()) {
            throw new ConfigurationException("cannot match " + scope + " to gradle configuration");
        }
        String gradleConfigurationName = gradleConfigurationNameOptional.get();
        String pattern = "(?s).*" + gradleConfigurationName + "\\(\"" + groupId.replace(".", "\\.") + ":" + artifactId + "\"\\)\\s*\\{(.+?)\\}";
        Pattern compiledPattern = Pattern.compile(pattern);
        var matcher = compiledPattern.matcher(template);
        String assignmentOp = (buildTool == BuildTool.GRADLE) ? ": " : " = ";
        return matcher.find() && matcher.group(1).contains("exclude(group" + assignmentOp + "\"" + excludedGroupId + "\", module" + assignmentOp + "\"" + excludedArtifactId + "\")");
    }

    @Override
    public boolean hasTestResourceDependency(String groupId, String artifactId) {
        return hasDependency(groupId, artifactId, Scope.TEST_RESOURCES_SERVICE);
    }

    @Override
    public boolean hasTestResourceDependency(String artifactId) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public boolean hasDependency(String artifactId) {
        return template.contains(artifactId);
    }

    @Override
    public boolean hasTestResourceDependencyWithGroupId(String expectedGroupId) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public boolean hasBuildPlugin(String id) {
        return template.contains("id(\"" + id + "\")");
    }
}
