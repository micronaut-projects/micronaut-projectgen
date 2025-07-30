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
package io.micronaut.starter.feature.lang;

import com.fizzed.rocker.RockerOutput;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;

public abstract class ApplicationRenderingContext {

    private final String defaultEnvironment;
    private final boolean eagerSingletons;

    protected ApplicationRenderingContext(String defaultEnvironment, boolean eagerSingletons) {
        this.defaultEnvironment = defaultEnvironment;
        this.eagerSingletons = eagerSingletons;
    }

    /**
     * Determines if this rendering context is required.
     *
     * @return true if a default environment is set or eager singletons are enabled, false otherwise
     */
    public boolean isRequired() {
        return defaultEnvironment != null || eagerSingletons;
    }

    /**
     * Gets the context configurer output if this context is required.
     *
     * @return the rendered context configurer output, or null if not required
     */
    @Nullable
    public RockerOutput getContextConfigurer() {
        return isRequired() ? getRendered() : null;
    }

    /**
     * Gets the rendered Rocker output for this context.
     *
     * @return the rendered RockerOutput
     */
    @NonNull
    protected abstract RockerOutput getRendered();

    /**
     * Checks if eager singletons are enabled.
     *
     * @return true if eager singletons are enabled, false otherwise
     */
    public boolean isEagerSingletons() {
        return eagerSingletons;
    }

    /**
     * Gets the default environment name.
     *
     * @return the default environment string, may be null
     */
    public String getDefaultEnvironment() {
        return defaultEnvironment;
    }
}
