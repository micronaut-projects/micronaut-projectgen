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
package io.micronaut.projectgen.core.feature.gitignore;

import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.rocker.RockerTemplate;
import jakarta.inject.Singleton;
import io.micronaut.projectgen.core.template.gitignore;

import java.util.List;

/**
 * Creates a .gitignore file.
 */
@Singleton
public class GitIgnore implements Feature {
    private final List<ContributesGitIgnoreEntries> contributesGitIgnoreEntries;

    public GitIgnore(List<ContributesGitIgnoreEntries> contributesGitIgnoreEntries) {
        this.contributesGitIgnoreEntries = contributesGitIgnoreEntries;
    }

    @Override
    public String getName() {
        return "gitignore";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        List<GitIgnoreGroup> groups = contributesGitIgnoreEntries.stream()
            .map(contributesGitIgnoreEntry -> contributesGitIgnoreEntry.gitIgnoreGroup(generatorContext))
            .toList();
        generatorContext.addTemplate("gitignore", new RockerTemplate(".gitignore", gitignore.template(groups)));
    }
}
