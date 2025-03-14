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
package io.micronaut.projectgen.springboot;

import io.micronaut.context.BeanContext;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.projectgen.core.feature.AvailableFeatures;
import io.micronaut.projectgen.core.generator.ProjectGenerator;
import io.micronaut.projectgen.core.io.ConsoleOutput;
import io.micronaut.projectgen.core.io.OutputHandler;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

@Singleton
public class SpringBootProjectGenerator {

    private final ProjectGenerator delegate;
    private final BeanContext beanContext;

    public SpringBootProjectGenerator(ProjectGenerator delegate,
                                     BeanContext beanContext) {
        this.delegate = delegate;
        this.beanContext = beanContext;
    }

    public void generate(@NonNull SpringBootOptions options, @NonNull OutputHandler outputHandler) throws Exception {
        Provider<AvailableFeatures> availableFeaturesProvider = provideAvailableFeatures(options);
        delegate.generate(options, outputHandler, availableFeaturesProvider);
    }

    public void generate(@NonNull SpringBootOptions options, @NonNull OutputHandler outputHandler, ConsoleOutput consoleOutput) throws Exception {
        Provider<AvailableFeatures> availableFeaturesProvider = provideAvailableFeatures(options);
        delegate.generate(options, outputHandler, consoleOutput, availableFeaturesProvider);
    }

    private Provider<AvailableFeatures> provideAvailableFeatures(SpringBootOptions options) {
        return () ->
            beanContext.getBean(AvailableFeatures.class);
    }

}
