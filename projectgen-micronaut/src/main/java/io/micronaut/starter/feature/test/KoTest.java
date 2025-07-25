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
package io.micronaut.starter.feature.test;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.feature.TestFeature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.core.options.TestFramework;
import io.micronaut.projectgen.core.template.URLTemplate;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

@Requires(property = "micronaut.starter.feature.kotest.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class KoTest implements TestFeature, OpenRewriteFeature {

    @Override
    @NonNull
    public String getName() {
        return "kotest";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        ModuleContext module = generatorContext.getRootModule();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        module.addTemplate("koTestConfig",
            new URLTemplate("src/test/kotlin/io/kotest/provided/ProjectConfig.kt",
                classLoader.getResource("kotest/ProjectConfig.kt")));
        OpenRewriteFeature.super.apply(generatorContext);
    }

    @Override
    public TestFramework getTestFramework() {
        return TestFramework.KOTEST;
    }

    @Override
    public String getTitle() {
        return "Micronaut Test Kotest5";
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        List<String> recipes = new ArrayList<>();
        recipes.add("io.micronaut.starter.feature.kotest-docs");
        if (OptionUtils.hasMavenBuildTool(generatorContext.getOptions())) {
            recipes.add("io.micronaut.starter.feature.kotest");
        }
        return recipes;
    }

}
