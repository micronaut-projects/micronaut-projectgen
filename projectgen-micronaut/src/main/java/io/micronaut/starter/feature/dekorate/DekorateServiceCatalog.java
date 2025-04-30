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
package io.micronaut.starter.feature.dekorate;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;

import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import jakarta.inject.Singleton;

import java.util.List;

/**
 * Adds Dekorate Service Catalog support.
 *
 * @author Pavol Gressa
 * @since 2.1
 */
@Requires(property = "micronaut.starter.feature.dekorate.servicecatalog.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class DekorateServiceCatalog extends AbstractDekorateServiceFeature implements OpenRewriteFeature {

    public DekorateServiceCatalog(DekorateKubernetes dekorateKubernetes) {
        super(dekorateKubernetes);
    }

    @NonNull
    @Override
    public String getName() {
        return "dekorate-servicecatalog";
    }

    @Override
    public String getTitle() {
        return "Dekorate Service Catalog Support";
    }

    @Override
    public String getDescription() {
        return """
                Extends Decorate's generated Kubernetes deployment manifests with Service Catalog resources \
                using Dekorate Service Catalog Support\
                """;
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return List.of("io.micronaut.starter.feature.dekorate-servicecatalog");
    }

}
