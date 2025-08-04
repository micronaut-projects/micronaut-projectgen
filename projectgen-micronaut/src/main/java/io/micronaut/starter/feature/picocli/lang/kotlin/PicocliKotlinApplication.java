/*
 * Copyright 2017-2024 original authors
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
package io.micronaut.starter.feature.picocli.lang.kotlin;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.feature.KotlinApplicationFeature;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.Project;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.micronaut.template.picocli.lang.kotlin.picocliApplication;
import io.micronaut.starter.feature.RequireKaptFeature;
import io.micronaut.projectgen.core.rocker.RockerTemplate;
import jakarta.inject.Singleton;

/**
 * Feature for generating a Kotlin-based CLI application using Picocli.
 *
 * <p>Creates a Picocli command as the main application class and sets up the Kotlin project structure accordingly.</p>
 */
@Requires(property = "micronaut.starter.feature.picocli.kotlin.application.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class PicocliKotlinApplication implements RequireKaptFeature, KotlinApplicationFeature {

    @Override
    @Nullable
    public String mainClassName(GeneratorContext generatorContext) {
        Project project = generatorContext.getProject();
        return project.getPackageName() + "." + project.getClassName() + "Command";
    }

    @Override
    public String getName() {
        return "picocli-kotlin-application";
    }

    @Override
    public boolean supports(Options options) {
        ApplicationType applicationType = ApplicationType.of(options.template());
        return applicationType == ApplicationType.CLI;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        KotlinApplicationFeature.super.apply(generatorContext);
        ModuleContext module = generatorContext.getRootModule();
        module.addTemplate("application", getTemplate(generatorContext.getProject()));
    }

    /**
     * Returns the {@link RockerTemplate} for generating the Picocli application source file.
     *
     * @param project The project metadata.
     * @return The Rocker template used to render the application source.
     */
    public RockerTemplate getTemplate(Project project) {
        return new RockerTemplate(getPath(),
            picocliApplication.template(project));
    }

    /**
     * Returns the file path for the Picocli application source file.
     *
     * <p>The path is a template string that will be resolved with the project's package path and class name.</p>
     *
     * @return The file path template for the Picocli application source file.
     */
    protected String getPath() {
        return "src/main/kotlin/{packagePath}/{className}Command.kt";
    }
}
