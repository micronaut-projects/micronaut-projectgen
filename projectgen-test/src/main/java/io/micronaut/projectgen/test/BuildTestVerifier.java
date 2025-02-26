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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.projectgen.core.buildtools.Scope;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.utils.OptionUtils;

/**
 * You can get an instance via {@link BuildTestVerifier#of(String, Options)}.
 */
public interface BuildTestVerifier {

    /**
     *
     * @param groupId Group ID
     * @param artifactId artifact ID
     * @return Whether the build has the annotation processor
     */
    boolean hasAnnotationProcessor(String groupId, String artifactId);

    /**
     *
     * @param groupId Group ID
     * @param artifactId artifact ID
     * @return Whether the build has a test annotation processor
     */
    boolean hasTestAnnotationProcessor(String groupId, String artifactId);

    /**
     *
     * @param groupId Group ID
     * @param artifactId artifact ID
     * @param scope Scope
     * @return Whether it has Bom
     */
    boolean hasBom(String groupId, String artifactId, Scope scope);

    /**
     *
     * @param groupId Group ID
     * @param artifactId artifact ID
     * @param scope Scope
     * @return Whether it has Bom
     */
    boolean hasBom(String groupId, String artifactId, String scope);

    /**
     *
     * @param groupId Group ID
     * @param artifactId artifact ID
     * @param scope Scope
     * @return Whether it has a dependency
     */
    boolean hasDependency(String groupId, String artifactId, Scope scope);

    /**
     *
     * @param groupId Group ID
     * @param artifactId artifact ID
     * @param scope Scope
     * @return Whether it has a dependency
     */
    boolean hasDependency(String groupId, String artifactId, String scope);

    /**
     *
     * @param groupId Group ID
     * @param artifactId artifact ID
     * @param scope Scope
     * @param version version
     * @param isProperty IsProperty
     * @return Whether it has a dependency
     */
    boolean hasDependency(String groupId, String artifactId, Scope scope, String version, boolean isProperty);

    /**
     *
     * @param groupId Group ID
     * @param artifactId artifact ID
     * @param scope Scope
     * @param version version
     * @param isProperty IsProperty
     * @return Whether it has a dependency
     */
    boolean hasDependency(String groupId, String artifactId, String scope, String version, boolean isProperty);

    /**
     *
     * @param groupId Group ID
     * @param artifactId artifact ID
     * @return Whether it has a dependency
     */
    boolean hasDependency(String groupId, String artifactId);

    /**
     *
     * @param groupId Group ID
     * @param artifactId artifact ID
     * @param excludedGroupId Excluded Group ID
     * @param excludedArtifactId Excluded Artifact ID
     * @return Whether it has exclusion
     */
    boolean hasExclusion(String groupId, String artifactId, String excludedGroupId, String excludedArtifactId);

    /**
     *
     * @param groupId Group ID
     * @param artifactId artifact ID
     * @param excludedGroupId Excluded Group ID
     * @param excludedArtifactId Excluded Artifact ID
     * @param scope Scope
     * @return Whether it has exclusion
     */
    boolean hasExclusion(String groupId, String artifactId, String excludedGroupId, String excludedArtifactId, Scope scope);

    /**
     *
     * @param groupId Group ID
     * @param artifactId artifact ID
     * @return Whether it has Test resource dependency
     */
    boolean hasTestResourceDependency(String groupId, String artifactId);

    /**
     *
     * @param artifactId artifact ID
     * @return Whether it has Test resource dependency
     */
    boolean hasTestResourceDependency(String artifactId);

    /**
     *
     * @param artifactId artifact ID
     * @return Whether it has a dependency
     */
    boolean hasDependency(String artifactId);

    /**
     *
     * @param expectedGroupId group ID
     * @return Whether it has a test resource dependency
     */
    boolean hasTestResourceDependencyWithGroupId(String expectedGroupId);

    /**
     *
     * @param id build plugin id
     * @return Whether it has a build plugin
     */
    boolean hasBuildPlugin(String id);

    @NonNull
    static BuildTestVerifier of(@NonNull String template, @NonNull Options options) {
        if (OptionUtils.hasGradleBuildTool(options)) {
            return new GradleBuildTestVerifier(template, options);
        }
        return null;//TODO
    }
}
