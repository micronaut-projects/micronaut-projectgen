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
package io.micronaut.projectgen.quarkus.features;

import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.feature.Feature;

public interface QuarkusFeature extends Feature {
    String QUARKUS_GROUP_ID = "io.quarkus";

    static Dependency.Builder dependency(String artifactId) {
        return Dependency.builder()
            .groupId(QUARKUS_GROUP_ID)
            .artifactId(artifactId);
    }

    static Dependency compileDependency(String artifactId) {
        return dependency(artifactId)
            .compile()
            .build();
    }

    static Dependency testDependency(String artifactId) {
        return dependency(artifactId)
            .test()
            .build();
    }
}
