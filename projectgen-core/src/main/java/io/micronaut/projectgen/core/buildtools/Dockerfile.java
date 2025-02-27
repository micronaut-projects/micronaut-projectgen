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
package io.micronaut.projectgen.core.buildtools;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Dockerfile.
 */
public class Dockerfile {
    @Nullable
    private String baseImage;

    @Nullable
    private String javaVersion;

    @Nullable
    private List<String> args;

    public Dockerfile(@Nullable String baseImage, @Nullable String javaVersion, @Nullable List<String> args) {
        this.baseImage = baseImage;
        this.javaVersion = javaVersion;
        this.args = args;
    }

    /**
     *
     * @return base image
     */
    @Nullable
    public String getBaseImage() {
        return baseImage;
    }

    /**
     *
     * @return java version
     */
    @Nullable
    public String getJavaVersion() {
        return javaVersion;
    }

    /**
     *
     * @return args
     */
    @Nullable
    public List<String> getArgs() {
        return args;
    }

    @NonNull
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder.
     */
    public static class Builder {

        private String baseImage;
        private String javaVersion;
        private List<String> args;

        /**
         *
         * @param baseImage base image
         * @return builder
         */
        @NonNull
        public Builder baseImage(String baseImage) {
            this.baseImage = baseImage;
            return this;
        }

        /**
         *
         * @param javaVersion java version
         * @return builder
         */
        public Builder javaVersion(String javaVersion) {
            this.javaVersion = javaVersion;
            return this;
        }

        /**
         *
         * @param arg arg
         * @return builder
         */
        @NonNull
        public Builder arg(String arg) {
            if (args == null) {
                args = new ArrayList<>();
            }
            args.add(arg);
            return this;
        }

        /**
         *
         * @param args arguments
         * @return builder
         */
        @NonNull
        public Builder args(List<String> args) {
            this.args = args;
            return this;
        }

        /**
         *
         * @return Dockerfile
         */
        @NonNull
        public Dockerfile build() {
            return new Dockerfile(baseImage, javaVersion, args);
        }
    }
}
