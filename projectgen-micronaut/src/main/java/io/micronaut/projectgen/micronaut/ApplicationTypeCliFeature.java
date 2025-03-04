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
package io.micronaut.projectgen.micronaut;

import io.micronaut.projectgen.core.buildtools.gradle.Gradle;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.config.Properties;
import io.micronaut.projectgen.core.feature.gitignore.GitIgnore;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.javalibs.logging.Logback;
import io.micronaut.projectgen.micronaut.features.test.MicronautTestJunit5;
import io.micronaut.projectgen.micronaut.features.test.MicronautTestSpock;
import jakarta.inject.Singleton;

import java.util.Set;

@Singleton
public class ApplicationTypeCliFeature extends ApplicationTypeFeature {

    public ApplicationTypeCliFeature(Gradle gradle,
                                     MicronautTestJunit5 micronautTestJunit5,
                                     MicronautTestSpock micronautTestSpock,
                                     Properties properties,
                                     Logback logback,
                                     GitIgnore gitIgnore) {
        super(gradle, micronautTestJunit5, micronautTestSpock, properties, logback, gitIgnore);
    }

    @Override
    public boolean shouldApply(Options options, Set<Feature> selectedFeatures) {
        return options instanceof MicronautOptions micronautOptions && micronautOptions.applicationType() == ApplicationType.CLI;
    }

    @Override
    public String getName() {
        return "application-type-cli";
    }
}
