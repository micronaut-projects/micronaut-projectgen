/*
 * Copyright 2017-2022 original authors
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
package io.micronaut.projectgen.core.feature.config;

import io.micronaut.core.annotation.NonNull;

import java.util.Map;

/**
 * A utility class to supply nested configuration via {@link io.micronaut.projectgen.core.feature.config.ApplicationConfiguration#addNested(NestedConfiguration)}.
 */
public class NestedConfiguration {

    @NonNull
    private final String path;

    @NonNull
    private final Map<String, Object> configuration;

    /**
     * Constructor.
     * For path = custom.config and configuration = [foo: [enabled: true, name: 'bar'] the generated config will be:
     * <pre>
     * custom:
     *   config:
     *     foo:
     *       enabled: true
     *       name: bar
     * </pre>
     * @param path root path
     * @param configuration configuration
     */
    public NestedConfiguration(@NonNull String path, @NonNull Map<String, Object> configuration) {
        this.path = path;
        this.configuration = configuration;
    }

    /**
     *
     * @return path
     */
    @NonNull
    public String getPath() {
        return path;
    }

    /**
     *
     * @return configuration.
     */
    @NonNull
    public Map<String, Object> getConfiguration() {
        return configuration;
    }
}
