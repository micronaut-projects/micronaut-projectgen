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
package io.micronaut.starter.feature.logging;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.feature.LoggingFeature;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.micronaut.template.logging.log4j2;
import io.micronaut.projectgen.core.rocker.RockerTemplate;
import io.micronaut.starter.util.VersionInfo;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

@Requires(property = "micronaut.starter.feature.log4j2.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Log4j2 implements LoggingFeature, OpenRewriteFeature {
    public static final String NAME = "log4j2";

    private static final String GROUP_ID = "org.apache.logging.log4j";

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Log4j 2 Logging";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds Log4j 2 Logging";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        OpenRewriteFeature.super.apply(generatorContext);
        ModuleContext module = generatorContext.getRootModule();
        module.addTemplate("loggingConfig", new RockerTemplate("src/main/resources/log4j2.xml", log4j2.template(generatorContext.getProject())));
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        List<String> recipes = new ArrayList<>();
        recipes.add("io.micronaut.starter.feature.log4j2");
        if (OptionUtils.hasGradleBuildTool(generatorContext.getOptions())) {
             recipes.add("io.micronaut.starter.feature.log4j-bom-gradle");
        }
        if (OptionUtils.hasMavenBuildTool(generatorContext.getOptions())) {
            recipes.add("io.micronaut.starter.feature.log4j-bom-maven");
        }
        return recipes;
    }

}
