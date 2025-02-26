/*
 * Copyright 2017-2023 original authors
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
package io.micronaut.projectgen.core.buildtools.maven;

import io.micronaut.projectgen.core.buildtools.dependencies.Coordinate;

/**
 * Parent POM.
 * @param groupId Group ID
 * @param artifactId artifact ID
 * @param version Version
 * @param relativePath Relative Path
 */
public record ParentPom(String groupId, String artifactId, String version, boolean relativePath) {

    public ParentPom(Coordinate coordinate, boolean relativePath) {
        this(coordinate.getGroupId(), coordinate.getArtifactId(), coordinate.getVersion(), relativePath);
    }

    public ParentPom(Coordinate coordinate) {
        this(coordinate, false);
    }
}
