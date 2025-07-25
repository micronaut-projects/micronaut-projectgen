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
package io.micronaut.starter.feature.asciidoctor;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.core.rocker.RockerWritable;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.CoordinateResolver;
import io.micronaut.projectgen.core.buildtools.maven.MavenPlugin;
import io.micronaut.starter.feature.Category;
import io.micronaut.projectgen.micronaut.template.asciidoctor.asciidocGradle;
import io.micronaut.projectgen.micronaut.template.asciidoctor.asciidocMavenPlugin;
import io.micronaut.projectgen.micronaut.template.asciidoctor.indexAdoc;
import io.micronaut.projectgen.core.rocker.RockerTemplate;

import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

@Requires(property = "micronaut.starter.feature.asciidoctor.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Asciidoctor implements OpenRewriteFeature {

    private final CoordinateResolver coordinateResolver;

    public Asciidoctor(CoordinateResolver coordinateResolver) {
        this.coordinateResolver = coordinateResolver;
    }

    @Override
    public String getName() {
        return "asciidoctor";
    }

    @Override
    public String getTitle() {
        return "Asciidoctor Documentation";
    }

    @Override
    public String getDescription() {
        return "Adds support for creating Asciidoctor documentation";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        ModuleContext module = generatorContext.getRootModule();
        OpenRewriteFeature.super.apply(generatorContext);
        if (OptionUtils.hasGradleBuildTool(generatorContext.getOptions())) {
            module.addTemplate("asciidocGradle", new RockerTemplate("gradle/asciidoc.gradle", asciidocGradle.template()));
        } else if (OptionUtils.hasMavenBuildTool(generatorContext.getOptions())) {
            String mavenPluginArtifactId = "asciidoctor-maven-plugin";
            module.addBuildPlugin(MavenPlugin.builder()
                .artifactId(mavenPluginArtifactId)
                .extension(new RockerWritable(asciidocMavenPlugin.template()))
                .build());
        }
        module.addTemplate("indexAdoc", new RockerTemplate("src/docs/asciidoc/index.adoc", indexAdoc.template()));
    }

    @Override
    public String getCategory() {
        return Category.DOCUMENTATION;
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        List<String> recipes = new ArrayList<>();
        if (OptionUtils.hasGradleBuildTool(generatorContext.getOptions())) {
            recipes.add("io.micronaut.starter.feature.asciidoctor-gradle");
        }
        if (OptionUtils.hasMavenBuildTool(generatorContext.getOptions())) {
            recipes.add("io.micronaut.starter.feature.asciidoctor-maven.properties");
        }
        return recipes;
    }

}
