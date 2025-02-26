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

import io.micronaut.projectgen.core.buildtools.dependencies.Coordinate;
import io.micronaut.projectgen.core.buildtools.dependencies.CoordinateResolver;
import io.micronaut.projectgen.core.buildtools.maven.ParentPom;
import io.micronaut.projectgen.core.buildtools.maven.ParentPomFeature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.utils.OptionUtils;
import jakarta.inject.Singleton;

@Singleton
public class SpringBootParentPomFeature implements ParentPomFeature {
    private static final String PROPERTY_JAVA_VERSION = "java.version";
    private final CoordinateResolver coordinateResolver;

    public SpringBootParentPomFeature(CoordinateResolver coordinateResolver) {
        this.coordinateResolver = coordinateResolver;
    }

    @Override
    public ParentPom getParentPom() {
        Coordinate coordinate = coordinateResolver.resolve("spring-boot-starter-parent").orElseThrow();
        return new ParentPom(coordinate, true);
    }

    @Override
    public String getName() {
        return "spring-boot-starter-parent";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (OptionUtils.hasMavenBuildTool(generatorContext.getOptions())
            && generatorContext.getOptions().javaVersion() != null) {
            generatorContext.getBuildProperties().put(PROPERTY_JAVA_VERSION, generatorContext.getOptions().javaVersion().asString());
        }
    }

    @Override
    public boolean isVisible() {
        return false;
    }
}
