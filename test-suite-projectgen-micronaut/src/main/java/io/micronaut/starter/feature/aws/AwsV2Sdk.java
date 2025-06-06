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
package io.micronaut.starter.feature.aws;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import jakarta.inject.Singleton;

import java.util.List;

@Requires(property = "micronaut.starter.feature.aws.v2.sdk.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class AwsV2Sdk implements AwsFeature, OpenRewriteFeature {

    public static final String NAME = "aws-v2-sdk";
    static final Dependency.Builder URL_CONNECTION_CLIENT = Dependency.builder()
            .groupId(GROUP_ID_AWS_SDK_V2)
            .artifactId("url-connection-client")
            .compile();
    static final Dependency.Builder APACHE_CLIENT_DEPENDENCY = Dependency.builder()
            .groupId(GROUP_ID_AWS_SDK_V2)
            .artifactId("apache-client")
            .compile();
    static final Dependency.Builder NETTY_NIO_CLIENT_DEPENDENCY = Dependency.builder()
            .groupId(GROUP_ID_AWS_SDK_V2)
            .artifactId("netty-nio-client")
            .compile();

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "AWS SDK 2.x";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Provides integration with the AWS SDK 2.x";
    }

    @Override
    public String getCategory() {
        return Category.CLOUD;
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return List.of("io.micronaut.starter.feature.aws-v2-sdk");
    }

}
