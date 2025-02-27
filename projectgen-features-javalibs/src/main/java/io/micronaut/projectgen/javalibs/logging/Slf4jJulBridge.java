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
package io.micronaut.projectgen.javalibs.logging;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.projectgen.core.feature.LoggingFeature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import jakarta.inject.Singleton;

@Singleton
public class Slf4jJulBridge implements OpenRewriteFeature, LoggingFeature {
    @Override
    public String getRecipeName() {
        return "io.micronaut.feature.javalibs.jul-to-slf4j";
    }

    @Override
    @NonNull
    public String getName() {
        return "jul-to-slf4j";
    }

    @Override
    public String getTitle() {
        return "SLF4J JUL Bridge";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Java Util Logging bridge for SLF4J with Logback.";
    }

    @Override
    public String getThirdPartyDocumentation(GeneratorContext generatorContext) {
        return "https://www.slf4j.org/legacy.html#jul-to-slf4jBridge";
    }

}
