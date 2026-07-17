/*
 * Copyright 2017-2021 original authors
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
package io.micronaut.projectgen.micronaut.gradle;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.buildtools.BuildPlugin;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.Dockerfile;
import io.micronaut.projectgen.core.buildtools.gradle.GradleDsl;
import io.micronaut.projectgen.core.buildtools.gradle.GradlePlugin;
import io.micronaut.projectgen.core.rocker.RockerWritable;
import io.micronaut.projectgen.micronaut.template.micronautGradle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Micronaut Gradle Plugin.
 */
public class MicronautApplicationGradlePlugin {

    /**
     * Creates a new {@link Builder} instance for configuring the Micronaut Gradle plugin.
     *
     * @return A new builder instance.
     */
    @NonNull
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for configuring the Micronaut Gradle plugin.
     */
    public static final class Builder {

        public static final String LIBRARY = "io.micronaut.library";
        public static final String APPLICATION = "io.micronaut.application";
        public static final String ARTIFACT_ID = "micronaut-gradle-plugin";

        private String id = APPLICATION;
        private GradleDsl dsl = GradleDsl.GROOVY;
        @Nullable
        private String javaVersion;
        @Nullable
        private String runtime;
        @Nullable
        private String testRuntime;
        @Nullable
        private String lambdaRuntimeMainClass;
        @Nullable
        private String aotVersion;
        @Nullable
        private Dockerfile dockerfileNative;
        @Nullable
        private Dockerfile dockerfile;
        @Nullable
        private List<String> dockerBuildImages;
        @Nullable
        private List<String> dockerBuildNativeImages;
        @Nullable
        private List<String> additionalTestResourceModules;
        @Nullable
        private Map<String, String> aotKeys;
        @Nullable
        private Set<String> ignoredAutomaticDependencies;
        private BuildTool buildTool = BuildTool.GRADLE;
        private boolean incremental;
        private String packageName = "example";
        private boolean sharedTestResources;

        public Builder buildTool(BuildTool buildTool) {
            this.buildTool = buildTool;
            return this;
        }

        public Builder javaVersion(String javaVersion) {
            this.javaVersion = javaVersion;
            return this;
        }

        public Builder ignoredAutomaticDependencies(Set<String> ignoredAutomaticDependencies) {
            this.ignoredAutomaticDependencies = ignoredAutomaticDependencies;
            return this;
        }

        public Builder incremental(boolean incremental) {
            this.incremental = incremental;
            return this;
        }

        public Builder packageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder runtime(String runtime) {
            this.runtime = runtime;
            return this;
        }

        public Builder testRuntime(String testRuntime) {
            this.testRuntime = testRuntime;
            return this;
        }

        public Builder aot(String aotVersion) {
            this.aotVersion = aotVersion;
            return this;
        }

        public Builder dockerNative(Dockerfile dockerfileNative) {
            this.dockerfileNative = dockerfileNative;
            return this;
        }

        public Builder docker(Dockerfile dockerfile) {
            this.dockerfile = dockerfile;
            return this;
        }

        public Builder dockerBuildImage(String image) {
            if (dockerBuildImages == null) {
                dockerBuildImages = new ArrayList<>();
            }
            dockerBuildImages.add(image);
            return this;
        }

        public Builder dockerBuildNativeImage(String image) {
            if (dockerBuildNativeImages == null) {
                dockerBuildNativeImages = new ArrayList<>();
            }
            dockerBuildNativeImages.add(image);
            return this;
        }

        public Builder addAdditionalTestResourceModules(String... modules) {
            if (additionalTestResourceModules == null) {
                additionalTestResourceModules = new ArrayList<>();
            }
            additionalTestResourceModules.addAll(Arrays.asList(modules));
            return this;
        }

        public Builder lambdaRuntimeMainClass(String lambdaRuntimeMainClass) {
            this.lambdaRuntimeMainClass = lambdaRuntimeMainClass;
            return this;
        }

        public Builder aotKey(String aotKey, boolean value) {
            if (aotKeys == null) {
                aotKeys = new HashMap<>();
            }
            aotKeys.put(aotKey, value ? StringUtils.TRUE : StringUtils.FALSE);
            return this;
        }

        public Builder dsl(GradleDsl gradleDsl) {
            this.dsl = gradleDsl;
            return this;
        }

        public Builder withSharedTestResources() {
            this.sharedTestResources = true;
            return this;
        }

        public GradlePlugin.Builder builder() {
            return GradlePlugin.builder()
                .id(id)
                .lookupArtifactId(ARTIFACT_ID)
                .extension(new RockerWritable(micronautGradle.template(
                    dsl,
                    buildTool,
                    javaVersion,
                    dockerfile,
                    dockerfileNative,
                    dockerBuildImages,
                    dockerBuildNativeImages,
                    runtime,
                    testRuntime,
                    aotVersion,
                    incremental,
                    packageName,
                    additionalTestResourceModules,
                    sharedTestResources,
                    aotKeys,
                    lambdaRuntimeMainClass,
                    ignoredAutomaticDependencies
                )));
        }

        public BuildPlugin build() {
            return builder().build();
        }
    }
}
