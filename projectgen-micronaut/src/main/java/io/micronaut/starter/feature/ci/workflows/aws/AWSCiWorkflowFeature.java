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
package io.micronaut.starter.feature.ci.workflows.aws;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.starter.feature.ci.workflows.CIWorkflowFeature;
import io.micronaut.projectgen.core.rocker.RockerTemplate;
import io.micronaut.projectgen.core.template.Template;
import io.micronaut.projectgen.micronaut.template.ci.aws.buildSpec;

import jakarta.inject.Singleton;

import java.util.List;

@Requires(property = "micronaut.starter.feature.aws.codebuild.workflow.ci.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class AWSCiWorkflowFeature extends CIWorkflowFeature implements OpenRewriteFeature {
    public static final String NAME = "aws-codebuild-workflow-ci";
    private static final String WORKFLOW_FILENAME = "buildspec.yml";

    @NonNull
    @Override
    public String getWorkflowFileName(GeneratorContext generatorContext) {
        return WORKFLOW_FILENAME;
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "AWS CodeBuild CI Workflow";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds a AWS CodeBuild build specification to build a Micronaut application";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);
        OpenRewriteFeature.super.apply(generatorContext);
        ModuleContext module = generatorContext.getRootModule();
        module.addTemplate("cloudBuild", workflowRockerTemplate(generatorContext));
    }

    private Template workflowRockerTemplate(GeneratorContext generatorContext) {
        return new RockerTemplate(getWorkflowFileName(generatorContext), buildSpec.template(
                generatorContext.getProject().getName(),
                generatorContext.getJdkVersion(),
                generatorContext.getBuildTool()
            )
        );
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return List.of("io.micronaut.starter.feature.aws-codebuild-workflow-ci-docs");
    }

}
