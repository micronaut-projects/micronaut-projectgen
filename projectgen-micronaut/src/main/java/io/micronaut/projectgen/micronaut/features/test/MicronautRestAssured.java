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
package io.micronaut.projectgen.micronaut.features.test;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.starter.feature.Category;
import jakarta.inject.Singleton;

@Requires(property = "micronaut.starter.feature.micronaut.test.rest.assured.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class MicronautRestAssured implements OpenRewriteFeature {

    @NonNull
    @Override
    public String getName() {
        return "micronaut-test-rest-assured";
    }

    @Override
    public String getTitle() {
        return "Micronaut-Test REST-assured";
    }

    @NonNull
    @Override
    public String getDescription() {
        return "A small Micronaut-Test utility module that helps integrate the REST-assured library";
    }

    @Override
    public String getRecipeName() {
        return "io.micronaut.starter.feature.micronaut-test-rest-assured";
    }

    @Override
    public String getCategory() {
        return Category.DEV_TOOLS;
    }
}
