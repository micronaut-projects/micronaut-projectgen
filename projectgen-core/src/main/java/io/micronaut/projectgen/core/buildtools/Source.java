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
package io.micronaut.projectgen.core.buildtools;

/**
 * Source.
 */
public enum Source {
    MAIN("main"),
    TEST("test"),
    INTEGRATION_TEST("integration-test");

    private final String path;

    Source(String path) {
        this.path = path;
    }

    public static Source of(String path) {
        for (Source sourceSet : Source.values()) {
            if (path.contains("src/" + sourceSet.getPath() + '/')) {
                return sourceSet;
            }
        }

        throw new IllegalStateException("unable to determine source set for " + path);
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return path;
    }
}
