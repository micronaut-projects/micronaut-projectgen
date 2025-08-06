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
package io.micronaut.projectgen.micronaut.features.logging;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.feature.LoggingFeature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.core.options.OperatingSystem;
import io.micronaut.projectgen.core.rocker.RockerTemplate;
import io.micronaut.projectgen.micronaut.template.logging.logback;
import jakarta.inject.Singleton;

import java.util.List;

/**
 * Feature that provides Logback logging support for Micronaut applications.
 * This feature adds Logback as the logging framework with configurable coloring and JUL bridge support.
 */
@Requires(property = "micronaut.starter.feature.logback.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Logback implements LoggingFeature, OpenRewriteFeature {
    public static final boolean DEFAULT_COLORING = true;
    private static final boolean USE_JANSI = false;

    @Override
    @NonNull
    public String getName() {
        return "logback";
    }

    @Override
    public String getTitle() {
        return "Logback Logging";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds Logback Logging";
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return List.of("io.micronaut.feature.javalibs.logback-classic");
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        OpenRewriteFeature.super.apply(generatorContext);
        addConfig(generatorContext, generatorContext.hasFeature(Slf4jJulBridge.class));
    }

    /**
     * Adds Logback configuration to the project.
     *
     * @param generatorContext the generator context
     * @param useJul whether to use JUL bridge
     */
    protected void addConfig(GeneratorContext generatorContext, boolean useJul) {
        ModuleContext module = generatorContext.getRootModule();
        module.addTemplate("loggingConfig", new RockerTemplate("src/main/resources/logback.xml",
            logback.template(useJansi(generatorContext), DEFAULT_COLORING, useJul)));
    }

    /**
     * Determines whether to use Jansi for colored output based on the operating system.
     *
     * @param generatorContext the generator context
     * @return whether to use Jansi
     */
    protected boolean useJansi(@NonNull GeneratorContext generatorContext) {
        if (generatorContext.getOptions().operatingSystem() != null
            && generatorContext.getOptions().operatingSystem() == OperatingSystem.WINDOWS) {
            return false;
        }
        // TODO
//        if (generatorContext.getFeatures().hasFeature(AwsLambda.class)) {
//            return false;
//        }
        return USE_JANSI;
    }
}
