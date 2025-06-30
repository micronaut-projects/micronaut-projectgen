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
package io.micronaut.starter.feature.reloading;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.gradle.GradlePlugin;
import io.micronaut.projectgen.core.buildtools.maven.MavenPlugin;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.starter.buildtools.maven.JvmArgumentsFeature;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Requires(property = "micronaut.starter.feature.jrebel.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Jrebel implements ReloadingFeature, JvmArgumentsFeature, OpenRewriteFeature {
    private static final String JVM_ARGUMENT_AGENT_PATH = "-agentpath:~/bin/jrebel/lib/jrebel6/lib/libjrebel64.dylib";
    private static final String RECIPE_JREBEL_MAVEN_PLUGIN = "io.micronaut.starter.feature.jrebel-maven";
    private static final String RECIPE_JREBEL_GRADLE_PLUGIN = "io.micronaut.starter.feature.jrebel-gradle";
    private static final String RECIPE_JREBEL_DOCS = "io.micronaut.starter.feature.jrebel-docs";

    @Override
    public String getName() {
        return "jrebel";
    }

    @Override
    public String getTitle() {
        return "JRebel JVM Agent";
    }

    @Override
    public String getDescription() {
        return "Adds support for class reloading with JRebel (requires separate JRebel installation)";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        OpenRewriteFeature.super.apply(generatorContext);
        ModuleContext module = generatorContext.getRootModule();
        if (OptionUtils.hasGradleBuildTool(generatorContext.getOptions())) {
            module.buildProperties().addComment("TODO: Replace with agent path from JRebel installation; see documentation");
            module.buildProperties().addComment("rebelAgent=" + JVM_ARGUMENT_AGENT_PATH);
        }
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        List<String> recipes = new ArrayList<>();
        recipes.add(RECIPE_JREBEL_DOCS);
        if (OptionUtils.hasGradleBuildTool(generatorContext.getOptions())) {
            recipes.add(RECIPE_JREBEL_GRADLE_PLUGIN);
        }
        if (OptionUtils.hasMavenBuildTool(generatorContext.getOptions())) {
            recipes.add(RECIPE_JREBEL_MAVEN_PLUGIN);
        }
        return recipes;
    }

    @Override
    public List<String> getJvmArguments() {
        return Collections.singletonList(JVM_ARGUMENT_AGENT_PATH);
    }
}
