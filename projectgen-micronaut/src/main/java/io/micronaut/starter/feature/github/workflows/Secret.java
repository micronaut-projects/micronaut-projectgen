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
package io.micronaut.starter.feature.github.workflows;

import io.micronaut.core.annotation.Nullable;

/**
 * GitHub secret.
 *
 * @author Pavol Gressa
 * @since 2.2
 */
public class Secret {

    private final String name;
    @Nullable
    private final String value;
    private final String description;

    public Secret(String name, String description) {
        this(name, null, description);
    }

    public Secret(String name, @Nullable String value, String description) {
        this.name = name;
        this.value = value;
        this.description = description;
    }

    /**
     * Returns the name of the secret.
     *
     * @return the secret name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the description of the secret.
     *
     * @return the secret description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the value of the secret.
     *
     * @return the secret value
     */
    @Nullable
    public String getValue() {
        return value;
    }
}
