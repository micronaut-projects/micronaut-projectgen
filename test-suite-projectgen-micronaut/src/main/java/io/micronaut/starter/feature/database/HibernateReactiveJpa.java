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
package io.micronaut.starter.feature.database;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.starter.buildtools.dependencies.MicronautDependencyUtils;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.starter.feature.migration.MigrationFeature;
import io.micronaut.starter.feature.reactor.Reactor;
import io.micronaut.starter.feature.testresources.TestResources;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

@Requires(property = "micronaut.starter.feature.hibernate.reactive.jpa.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class HibernateReactiveJpa extends HibernateReactiveFeature implements OpenRewriteFeature {

    public static final String NAME = "hibernate-reactive-jpa";

    private final Reactor reactiveFeature;

    public HibernateReactiveJpa(Reactor reactiveFeature, TestContainers testContainers, TestResources testResources) {
        super(testContainers, testResources);
        this.reactiveFeature = reactiveFeature;
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeature(reactiveFeature);
    }

    @Override
    public String getTitle() {
        return "Hibernate Reactive JPA";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds support for Hibernate Reactive/JPA";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);
        OpenRewriteFeature.super.apply(generatorContext);
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        List<String> recipes = new ArrayList<>();
        if (generatorContext.isFeaturePresent(MigrationFeature.class)) {
            recipes.add("io.micronaut.starter.feature.jpa-hbm2ddl-none");
        } else {
            recipes.add("io.micronaut.starter.feature.jpa-hbm2ddl-update");
        }
        if(generatorContext.isFeaturePresent(TestResources.class)){
            addDatabaseConfigRecipe(generatorContext, recipes);
        }
        recipes.add("io.micronaut.starter.feature.hibernate-reactive-jpa");
        return recipes;
    }

}
