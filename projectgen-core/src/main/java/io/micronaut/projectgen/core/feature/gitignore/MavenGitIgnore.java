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

import io.micronaut.projectgen.core.generator.GeneratorContext;
import jakarta.inject.Singleton;

import java.util.List;

/**
 * .gitignore entries for Maven projects.
 */
@Singleton
public class MavenGitIgnore implements ContributesGitIgnoreEntries {
    private static final List<String> ENTRIES = List.of("target/",
        ".mvn/wrapper/maven-wrapper.jar");

    @Override
    public GitIgnoreGroup gitIgnoreGroup(GeneratorContext generatorContext) {
        return new GitIgnoreGroup("Maven", ENTRIES);
    }
}
