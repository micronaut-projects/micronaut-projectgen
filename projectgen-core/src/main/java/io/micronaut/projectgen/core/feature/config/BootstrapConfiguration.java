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

import io.micronaut.context.env.Environment;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.projectgen.core.buildtools.Source;

/**
 * Bootstrap configuration.
 */
public class BootstrapConfiguration extends Configuration {

    public BootstrapConfiguration(@NonNull String sourceSet, @NonNull String environment) {
        super(Configuration.sourceSetPath(sourceSet), ConfigurationPhase.BOOTSTRAP + "-" + environment, ConfigurationPhase.BOOTSTRAP + "-config-" + environment);
    }

    public BootstrapConfiguration(@NonNull String environment) {
        this("main", environment);
    }

    public BootstrapConfiguration() {
        super(Configuration.sourceSetPath("main"), ConfigurationPhase.BOOTSTRAP.toString(), ConfigurationPhase.BOOTSTRAP + "-config");
    }

    public static BootstrapConfiguration testConfig() {
        return new BootstrapConfiguration(Source.TEST.toString(), Environment.TEST);
    }
}
