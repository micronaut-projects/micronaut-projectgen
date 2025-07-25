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
package io.micronaut.starter.feature.ci.workflows.github;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.starter.feature.ci.workflows.CIWorkflowFeature;
import io.micronaut.projectgen.micronaut.template.ci.github.javaAction;
import io.micronaut.projectgen.core.options.JdkDistribution;
import io.micronaut.projectgen.core.rocker.RockerTemplate;
import io.micronaut.projectgen.core.template.Template;
import jakarta.inject.Singleton;

@Requires(property = "micronaut.starter.feature.github.workflow.ci.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class GithubCiWorkflowFeature extends CIWorkflowFeature {
    public static final String NAME = "github-workflow-ci";

    private static final String WORKFLOW_BASE_PATH = ".github/workflows/";
    private static final String DEFAULT_BRANCH = "main";

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Github Actions CI Workflow";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds a GitHub Actions Workflow to build a Micronaut application";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);
        ModuleContext module = generatorContext.getRootModule();
        module.addTemplate("javaAction", workflowRockerTemplate(generatorContext));
    }

    private Template workflowRockerTemplate(GeneratorContext generatorContext) {
        String workflowFilePath = WORKFLOW_BASE_PATH + getWorkflowFileName(generatorContext);
        return new RockerTemplate(workflowFilePath, javaAction.template(
            generatorContext.getJdkVersion(),
            JdkDistribution.DEFAULT_DISTRIBUTION,
            generatorContext.getOptions().getBuildTool(),
            DEFAULT_BRANCH)
        );
    }

    @Override
    public String getThirdPartyDocumentation(GeneratorContext generatorContext) {
        return "https://docs.github.com/en/actions";
    }

    @NonNull
    @Override
    public String getWorkflowFileName(GeneratorContext generatorContext) {
        if (OptionUtils.hasGradleBuildTool(generatorContext.getOptions())) {
            return "gradle.yml";
        } else if (OptionUtils.hasMavenBuildTool(generatorContext.getOptions())) {
            return "maven.yml";
        }
        throw new IllegalArgumentException("Unexpected constant for BuildTool enum");
    }
}
