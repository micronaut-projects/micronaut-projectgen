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
package io.micronaut.projectgen.core.diff;

import io.micronaut.core.annotation.Internal;
import io.micronaut.projectgen.core.generator.ProjectGenerator;
import io.micronaut.projectgen.core.io.ConsoleOutput;
import io.micronaut.projectgen.core.io.MapOutputHandler;
import io.micronaut.projectgen.core.options.Options;

import java.util.Map;

@Internal
abstract class AbstractFeatureDiffer implements FeatureDiffer {
    private final ProjectGenerator projectGenerator;

    protected AbstractFeatureDiffer(ProjectGenerator projectGenerator) {
        this.projectGenerator = projectGenerator;
    }

    @Override
    public String diff(Options options) throws Exception {
        Options optionsWithoutFeatures = options.withoutFeatures();
        Map<String, String> projectWithoutFeatures = generateProject(optionsWithoutFeatures);
        Map<String, String> project = generateProject(options);
        return diff(projectWithoutFeatures, project);
    }

    private Map<String, String> generateProject(Options options) throws Exception {
        MapOutputHandler outputHandler = new MapOutputHandler();
        Options optionsWithoutFeatures = options.withoutFeatures();
        projectGenerator.generate(options, outputHandler);
        return outputHandler.getProject();
    }

    private String diff(Map<String, String> oldProject, Map<String, String> newProject) {
        StringBuilderConsoleOutput sb = new StringBuilderConsoleOutput();
        diff(oldProject, newProject, sb);
        return sb.toString();
    }

    protected abstract void diff(Map<String, String> oldProject,
                      Map<String, String> newProject,
                      ConsoleOutput consoleOutput);
}
