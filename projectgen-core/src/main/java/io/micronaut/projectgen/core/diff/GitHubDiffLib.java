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

import com.github.difflib.DiffUtils;
import com.github.difflib.UnifiedDiffUtils;
import com.github.difflib.patch.Patch;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.Internal;
import io.micronaut.projectgen.core.generator.ProjectGenerator;
import io.micronaut.projectgen.core.io.ConsoleOutput;
import jakarta.inject.Singleton;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Requires(classes = { DiffUtils.class, UnifiedDiffUtils.class, Patch.class})
@Singleton
@Internal
class GitHubDiffLib extends AbstractFeatureDiffer implements FeatureDiffer {

    protected GitHubDiffLib(ProjectGenerator projectGenerator) {
        super(projectGenerator);
    }

    @Override
    protected void diff(Map<String, String> oldProject,
              Map<String, String> newProject,
              ConsoleOutput consoleOutput) {
        for (Map.Entry<String, String> entry: newProject.entrySet()) {
            String oldFile = oldProject.remove(entry.getKey());

            if (entry.getValue() == null) {
                continue;
            }

            List<String> oldFileLines = oldFile == null ? Collections.emptyList() : toLines(oldFile);

            String newFile = entry.getValue();
            List<String> newFileLines = toLines(newFile);

            Patch<String> diff = DiffUtils.diff(oldFileLines, newFileLines);
            List<String> unifiedDiff = UnifiedDiffUtils
                .generateUnifiedDiff(entry.getKey(), entry.getKey(), oldFileLines, diff, 3);

            if (!unifiedDiff.isEmpty()) {
                for (String delta : unifiedDiff) {
                    if (delta.startsWith("+")) {
                        consoleOutput.green(delta);
                    } else if (delta.startsWith("-")) {
                        consoleOutput.red(delta);
                    } else {
                        consoleOutput.out(delta);
                    }
                }
                consoleOutput.out("\n");
            }
        }

        for (Map.Entry<String, String> entry: oldProject.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            List<String> oldFileLines = toLines(entry.getValue());
            Patch<String> diff = DiffUtils.diff(oldFileLines, Collections.emptyList());
            List<String> unifiedDiff = UnifiedDiffUtils.generateUnifiedDiff(entry.getKey(), entry.getKey(), oldFileLines, diff, 3);

            if (!unifiedDiff.isEmpty()) {
                for (String delta : unifiedDiff) {
                    if (delta.startsWith("+")) {
                        consoleOutput.green(delta);
                    } else if (delta.startsWith("-")) {
                        consoleOutput.red(delta);
                    } else {
                        consoleOutput.out(delta);
                    }
                }
                consoleOutput.out("\n");
            }
        }
    }

    private List<String> toLines(String file) {
        return Arrays.asList(file.split("\n"));
    }

}
