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
package io.micronaut.projectgen.micronaut.maven;

import io.micronaut.projectgen.core.buildtools.dependencies.Coordinate;
import io.micronaut.projectgen.core.buildtools.dependencies.CoordinateResolver;
import io.micronaut.projectgen.core.buildtools.maven.ParentPom;
import io.micronaut.projectgen.core.buildtools.maven.ParentPomFeature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.utils.OptionUtils;
import jakarta.inject.Singleton;

/**
 * Micronaut Parent POM feature.
 */
@Singleton
public class MicronautParentPomFeature implements ParentPomFeature {
    public static final String ARTIFACT_ID_MICRONAUT_PARENT = "micronaut-parent";
    private static final String PROPERTY_JDK_VERSION = "jdk.version";
    private static final String PROPERTY_MICRONAUT_VERSION = "micronaut.version";
    private final CoordinateResolver coordinateResolver;

    public MicronautParentPomFeature(CoordinateResolver coordinateResolver) {
        this.coordinateResolver = coordinateResolver;
    }

    @Override
    public ParentPom getParentPom() {
        Coordinate coordinate = coordinateResolver.resolve(ARTIFACT_ID_MICRONAUT_PARENT).orElseThrow();
        return new ParentPom(coordinate);
    }

    @Override
    public String getName() {
        return "spring-boot-starter-parent";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (OptionUtils.hasMavenBuildTool(generatorContext.getOptions())) {
            if (generatorContext.getOptions().javaVersion() != null) {
                generatorContext.getBuildProperties().put(PROPERTY_JDK_VERSION, generatorContext.getOptions().javaVersion().asString());
            }
            coordinateResolver.resolve(ARTIFACT_ID_MICRONAUT_PARENT).ifPresent(coordinate ->
                generatorContext.getBuildProperties().put(PROPERTY_MICRONAUT_VERSION, coordinate.getVersion()));
        }
    }

    @Override
    public boolean isVisible() {
        return false;
    }
}
