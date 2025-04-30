/*
 * Copyright 2017-2021 original authors
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
package io.micronaut.starter.feature.coherence;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
<<<<<<< HEAD
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
=======
import io.micronaut.projectgen.core.generator.ModuleContext;
>>>>>>> 0.0.x
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.projectgen.core.feature.Feature;
import jakarta.inject.Singleton;

import java.util.List;

/**
 * Base coherence feature.
 *
 * @author Pavol Gressa
 * @since 2.4
 */
@Requires(property = "micronaut.starter.feature.coherence.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class CoherenceFeature implements OpenRewriteFeature {

    public static final String NAME = "coherence";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Coherence";
    }

    @Override
    public String getDescription() {
        return "Adds support for using Coherence";
    }

    @Override
<<<<<<< HEAD
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return List.of("io.micronaut.starter.feature.coherence");
=======
    public String getThirdPartyDocumentation(GeneratorContext generatorContext) {
        return "https://coherence.java.net/";
    }

    @Override
    public String getFrameworkDocumentation(GeneratorContext generatorContext) {
        return "https://micronaut-projects.github.io/micronaut-coherence/latest/guide/";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        ModuleContext module = generatorContext.getRootModule();
        module.addDependency(MicronautDependencyUtils.coherenceDependency().artifactId("micronaut-coherence").compile());
        module.addDependency(Dependency.builder().groupId("com.oracle.coherence.ce").artifactId("coherence").compile());
>>>>>>> 0.0.x
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }
}
