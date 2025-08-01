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
package io.micronaut.starter.feature.buildtools.gradle;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.buildtools.Dockerfile;
import io.micronaut.projectgen.core.buildtools.gradle.GradleDsl;
import io.micronaut.projectgen.core.buildtools.gradle.GradlePlugin;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.rocker.RockerWritable;
import io.micronaut.projectgen.micronaut.template.buildtools.gradle.micronautGradle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Builder class for creating Micronaut Gradle plugin configurations.
 * This class provides a fluent API for configuring various aspects of the Micronaut Gradle plugin
 * including Java version, runtime settings, Docker configurations, and build tool options.
 */
public class MicronautApplicationGradlePlugin {

    /**
     * Creates a new builder instance for configuring the Micronaut Gradle plugin.
     *
     * @return a new Builder instance
     */
    @NonNull
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class for configuring Micronaut Gradle plugin settings.
     * Provides methods to set various configuration options like Java version, runtime,
     * Docker settings, and build tool preferences.
     */
    public static final class Builder {
        public static final String LIBRARY = "io.micronaut.library";
        public static final String APPLICATION = "io.micronaut.application";
        private static final String ARTIFACT_ID = "micronaut-gradle-plugin";

        String id = APPLICATION;
        private GradleDsl dsl = GradleDsl.GROOVY;
        private String javaVersion;
        private String runtime;
        private String testRuntime;

        private String lambdaRuntimeMainClass;
        private String aotVersion;
        private Dockerfile dockerfileNative;
        private Dockerfile dockerfile;
        private List<String> dockerBuildImages;
        private Map<String, String> aotKeys;
        private List<String> dockerBuildNativeImages;
        private List<String> additionalTestResourceModules;
        private BuildTool buildTool;
        private boolean incremental;
        private  String packageName;
        private boolean sharedTestResources;

        private Set<String> ignoredAutomaticDependencies;

        /**
         * Sets the build tool for the project.
         *
         * @param buildTool the build tool to use
         * @return this builder instance
         */
        public Builder buildTool(BuildTool buildTool) {
            this.buildTool = buildTool;
            return this;
        }

        /**
         * Sets the Java version for the project.
         *
         * @param javaVersion the Java version to use
         * @return this builder instance
         */
        public Builder javaVersion(String javaVersion) {
            this.javaVersion = javaVersion;
            return this;
        }

        /**
         * Sets the automatic dependencies to ignore.
         *
         * @param ignoredAutomaticDependencies set of dependency names to ignore
         * @return this builder instance
         */
        public Builder ignoredAutomaticDependencies(Set<String> ignoredAutomaticDependencies) {
            this.ignoredAutomaticDependencies = ignoredAutomaticDependencies;
            return this;
        }

        /**
         * Sets whether incremental compilation should be enabled.
         *
         * @param incremental true to enable incremental compilation
         * @return this builder instance
         */
        public Builder incremental(boolean incremental) {
            this.incremental = incremental;
            return this;
        }

        /**
         * Sets the package name for the project.
         *
         * @param packageName the package name
         * @return this builder instance
         */
        public Builder packageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        /**
         * Sets the plugin ID.
         *
         * @param id the plugin ID
         * @return this builder instance
         */
        public Builder id(String id) {
            this.id = id;
            return this;
        }

        /**
         * Sets the runtime for the project.
         *
         * @param runtime the runtime to use
         * @return this builder instance
         */
        public Builder runtime(String runtime) {
            this.runtime = runtime;
            return this;
        }

        /**
         * Sets the test runtime for the project.
         *
         * @param testRuntime the test runtime to use
         * @return this builder instance
         */
        public Builder testRuntime(String testRuntime) {
            this.testRuntime = testRuntime;
            return this;
        }

        /**
         * Sets the AOT (Ahead of Time) compilation version.
         *
         * @param aotVersion the AOT version
         * @return this builder instance
         */
        public Builder aot(String aotVersion) {
            this.aotVersion = aotVersion;
            return this;
        }

        /**
         * Sets the Dockerfile for native compilation.
         *
         * @param dockerfileNative the Dockerfile for native compilation
         * @return this builder instance
         */
        public Builder dockerNative(Dockerfile dockerfileNative) {
            this.dockerfileNative = dockerfileNative;
            return this;
        }

        /**
         * Sets the Dockerfile for general compilation.
         *
         * @param dockerfile the Dockerfile for general compilation
         * @return this builder instance
         */
        public Builder docker(Dockerfile dockerfile) {
            this.dockerfile = dockerfile;
            return this;
        }

        /**
         * Adds a Docker build image.
         *
         * @param image the Docker image to add
         * @return this builder instance
         */
        public Builder dockerBuildImage(String image) {
            if (dockerBuildImages == null) {
                dockerBuildImages = new ArrayList<>();
            }
            dockerBuildImages.add(image);
            return this;
        }

        /**
         * Adds a Docker build native image.
         *
         * @param image the Docker image to add
         * @return this builder instance
         */
        public Builder dockerBuildNativeImage(String image) {
            if (dockerBuildNativeImages == null) {
                dockerBuildNativeImages = new ArrayList<>();
            }
            dockerBuildNativeImages.add(image);
            return this;
        }

        /**
         * Adds additional test resource modules.
         *
         * @param modules the test resource modules to add
         * @return this builder instance
         */
        public Builder addAdditionalTestResourceModules(String... modules) {
            if (additionalTestResourceModules == null) {
                additionalTestResourceModules = new ArrayList<>();
            }
            additionalTestResourceModules.addAll(Arrays.asList(modules));
            return this;
        }

        /**
         * Sets the main class for the Lambda runtime.
         *
         * @param lambdaRuntimeMainClass the main class for the Lambda runtime
         * @return this builder instance
         */
        public Builder lambdaRuntimeMainClass(String lambdaRuntimeMainClass) {
            this.lambdaRuntimeMainClass = lambdaRuntimeMainClass;
            return this;
        }

        /**
         * Sets an AOT key and its value.
         *
         * @param aotKey the AOT key
         * @param value the value for the AOT key
         * @return this builder instance
         */
        public Builder aotKey(String aotKey, boolean value) {
            if (aotKeys == null) {
                aotKeys = new HashMap<>();
            }
            aotKeys.put(aotKey, value ? StringUtils.TRUE : StringUtils.FALSE);
            return this;
        }

        /**
         * Builds the Gradle plugin configuration.
         *
         * @return a GradlePlugin.Builder instance
         */
        public GradlePlugin.Builder builder() {
            return GradlePlugin.builder()
                .id(id)
                .lookupArtifactId(ARTIFACT_ID)
                .extension(new RockerWritable(micronautGradle.template(dsl, buildTool, javaVersion, dockerfile, dockerfileNative, dockerBuildImages, dockerBuildNativeImages, runtime, testRuntime, aotVersion, incremental, packageName, additionalTestResourceModules, sharedTestResources, aotKeys, lambdaRuntimeMainClass, ignoredAutomaticDependencies)));
        }

        /**
         * Sets the DSL (Domain Specific Language) for the Gradle configuration.
         *
         * @param gradleDsl the DSL to use
         * @return this builder instance
         */
        public Builder dsl(GradleDsl gradleDsl) {
            this.dsl = gradleDsl;
            return this;
        }

        /**
         * Enables shared test resources.
         *
         * @return this builder instance
         */
        public Builder withSharedTestResources() {
            this.sharedTestResources = true;
            return this;
        }
    }

    private MicronautApplicationGradlePlugin() {
    }

}
