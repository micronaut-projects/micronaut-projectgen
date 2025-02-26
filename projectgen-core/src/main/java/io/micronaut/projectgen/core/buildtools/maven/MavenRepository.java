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
package io.micronaut.projectgen.core.buildtools.maven;

import com.fizzed.rocker.RockerModel;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.projectgen.core.buildtools.Repository;
import io.micronaut.projectgen.core.rocker.RockerWritable;
import io.micronaut.projectgen.core.template.mavenRepository;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Maven Repository.
 */
public class MavenRepository extends RockerWritable {

    public MavenRepository(RockerModel model) {
        super(model);
    }

    public MavenRepository(String id, String url, boolean snapshot) {
        this(mavenRepository.template(id, url, snapshot));
    }

    @NonNull
    public static List<MavenRepository> listOf(List<Repository> repositories) {
        return repositories.stream()
            .map(it -> new MavenRepository(it.getId(), it.getUrl(), it.isSnapshot()))
            .collect(Collectors.toList());
    }
}
