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
package io.micronaut.projectgen.core.feature;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.projectgen.core.buildtools.BuildProperties;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.utils.OptionUtils;

/**
 * Application Feature.
 */
public interface ApplicationFeature extends Feature {

    @Nullable
    String mainClassName(GeneratorContext generatorContext);

    @Override
    default boolean isVisible() {
        return false;
    }

    @Override
    default void apply(GeneratorContext generatorContext) {
        if (OptionUtils.hasMavenBuildTool(generatorContext.getOptions())) {
            String mainClass = mainClassName(generatorContext);
            if (mainClass != null) {
                BuildProperties buildProperties = generatorContext.getBuildProperties();
                buildProperties.put("exec.mainClass", mainClass);
            }
        }
    }
}
