/*
 * Copyright 2017-2023 original authors
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
package io.micronaut.projectgen.core.buildtools;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of {@link RepositoryResolver}.
 */
@Singleton
public class DefaultRepositoryResolver implements RepositoryResolver {
    private Map<String, Repository> repositories = new HashMap<>();

    @Override
    @NonNull
    public List<Repository> resolveRepositories(@NonNull GeneratorContext generatorContext) {
        addFeatureWhichRequireRepositories(generatorContext);
        return new ArrayList<>(repositories.values());
    }

    private void addRepository(Repository repository) {
        repositories.put(repository.getId(), repository);
    }

    private void addFeatureWhichRequireRepositories(GeneratorContext generatorContext) {

        generatorContext.getFeatures()
                .getFeatures()
                .stream()
                .filter(RequiresRepository.class::isInstance)
                .forEach(f -> {
                    for (Repository repository : ((RequiresRepository) f).getRepositories()) {
                        addRepository(repository);
                    }
                });
    }
}
