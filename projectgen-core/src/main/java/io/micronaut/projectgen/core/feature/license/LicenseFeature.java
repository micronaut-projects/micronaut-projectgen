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
package io.micronaut.projectgen.core.feature.license;

import com.fizzed.rocker.RockerModel;
import io.micronaut.projectgen.core.feature.OneOfFeature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.rocker.RockerTemplate;

/**
 * Marker interface for any feature contributing a license for the project.
 */
public interface LicenseFeature extends OneOfFeature {
    @Override
    default Class<?> getFeatureClass() {
        return LicenseFeature.class;
    }

    @Override
    default void apply(GeneratorContext generatorContext) {
        generatorContext.addTemplate("license", new RockerTemplate("LICENSE", licenseModel(generatorContext)));
    }

    RockerModel licenseModel(GeneratorContext generatorContext);
}
