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

import io.micronaut.projectgen.core.buildtools.maven.MavenCompilerPluginAnnotationProcessors;
import io.micronaut.projectgen.core.buildtools.maven.MavenPlugin;
import io.micronaut.projectgen.core.buildtools.maven.MavenSpecificFeature;
import io.micronaut.projectgen.core.buildtools.maven.Packaging;
import io.micronaut.projectgen.core.feature.BuildFeature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.rocker.RockerTemplate;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.features.maven.template.mavenCompilerPlugin;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class MicronautMavenCompilerPlugin implements MavenSpecificFeature, BuildFeature {
    @Override
    public String getName() {
        return "micronaut-maven-compiler-plugin";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        ModuleContext module = generatorContext.getRootModule();
        if (OptionUtils.hasMavenBuildTool(generatorContext.getOptions())) {
            module.addBuildPlugin(mavenCompilerPlugin(module, generatorContext));
            module.moduleAttributes().setPackaging("${packaging}");
            module.buildProperties().put("packaging", Packaging.JAR.toString());
        }
    }

    private MavenPlugin mavenCompilerPlugin(ModuleContext module, GeneratorContext generatorContext) {
        String version = null;
        String configurationCombine = null;
        Boolean incrementalCompilation = null;
        String source = null;
        String target = null;

        MavenCompilerPluginAnnotationProcessors ann = MavenCompilerPluginAnnotationProcessors.of(module, generatorContext.getOptions().language());

        List<String> compilerArgs = List.of("-Amicronaut.processing.group=" + generatorContext.getProject().getPackageName(),
                "-Amicronaut.processing.module=" + generatorContext.getOptions().name());

        return MavenPlugin.builder()
                .groupId("org.apache.maven.plugins")
                .artifactId("maven-compiler-plugin")
                .extension(
                        new RockerTemplate(mavenCompilerPlugin.template(version,
                                configurationCombine,
                                incrementalCompilation,
                                source,
                                target,
                                ann,
                                compilerArgs)))
                .build();
    }
}
