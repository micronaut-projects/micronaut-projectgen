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

import io.micronaut.projectgen.core.buildtools.maven.MavenPlugin;
import io.micronaut.projectgen.core.buildtools.maven.MavenSpecificFeature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.rocker.RockerWritable;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.micronaut.MicronautOptions;
import io.micronaut.projectgen.micronaut.features.MicronautAot;
import io.micronaut.projectgen.micronaut.template.function.oraclefunction.OracleFunction;
import io.micronaut.starter.feature.messaging.SharedTestResourceFeature;
import jakarta.inject.Singleton;
import io.micronaut.projectgen.micronaut.template.buildtools.maven.micronautMavenPlugin;

import java.util.List;

@Singleton
public class MicronautMavenPlugin implements MavenSpecificFeature {
    @Override
    public String getName() {
        return "micronaut-maven-plugin";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        ModuleContext module = generatorContext.getRootModule();
        if (OptionUtils.hasMavenBuildTool(generatorContext.getOptions())) {
            Boolean shared = generatorContext.hasFeature(SharedTestResourceFeature.class) ? true : null;
            String jvmArguments = null; //TODO
            String configFile = generatorContext.hasFeature(MicronautAot.class) ? "aot-${packaging}.properties" : null;

            List<String> nativeImageBuildArgs = null;
            List<String> appArguments = null;
            if (generatorContext.getOptions() instanceof MicronautOptions micronautOptions &&
                micronautOptions.applicationType() == ApplicationType.FUNCTION &&
                generatorContext.hasFeature(OracleFunction.class)) {
                nativeImageBuildArgs = List.of("-H:+StaticExecutableWithDynamicLibC", "-Dfn.handler=${function.entrypoint}");
                appArguments = List.of("${function.entrypoint}");
            }
            module.addBuildPlugin(MavenPlugin.builder()
                .groupId("io.micronaut.maven")
                .artifactId("micronaut-maven-plugin")
                .extension(new RockerWritable(micronautMavenPlugin.template(shared, jvmArguments, configFile, nativeImageBuildArgs, appArguments)))
                .build());
        }
    }
}
