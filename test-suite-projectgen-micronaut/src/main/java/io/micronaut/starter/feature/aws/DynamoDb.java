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
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.starter.feature.Category;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.micronaut.template.aws.dynamodbConfigurationGroovy;
import io.micronaut.projectgen.micronaut.template.aws.dynamodbConfigurationJava;
import io.micronaut.projectgen.micronaut.template.aws.dynamodbConfigurationKotlin;
import io.micronaut.projectgen.micronaut.template.aws.dynamodbRepositoryGroovy;
import io.micronaut.projectgen.micronaut.template.aws.dynamodbRepositoryJava;
import io.micronaut.projectgen.micronaut.template.aws.dynamodbRepositoryKotlin;
import io.micronaut.projectgen.micronaut.template.aws.ciawsconditionGroovy;
import io.micronaut.projectgen.micronaut.template.aws.ciawsconditionJava;
import io.micronaut.projectgen.micronaut.template.aws.ciawsconditionKotlin;
import io.micronaut.projectgen.micronaut.template.aws.ciawsregionconditionGroovy;
import io.micronaut.projectgen.micronaut.template.aws.ciawsregionconditionJava;
import io.micronaut.projectgen.micronaut.template.aws.ciawsregionconditionKotlin;
import io.micronaut.projectgen.core.feature.config.ApplicationConfiguration;
import io.micronaut.projectgen.core.feature.config.Configuration;
import io.micronaut.projectgen.micronaut.features.validator.MicronautValidationFeature;
import io.micronaut.projectgen.micronaut.features.validator.ValidationFeature;
import jakarta.inject.Singleton;

import java.util.List;

@Requires(property = "micronaut.starter.feature.dynamodb.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class DynamoDb implements AwsFeature, OpenRewriteFeature {

    public static final String NAME = "dynamodb";

    private final AwsV2Sdk awsV2Sdk;
    private final MicronautValidationFeature micronautValidation;

    public DynamoDb(AwsV2Sdk awsV2Sdk, MicronautValidationFeature micronautValidation) {
        this.awsV2Sdk = awsV2Sdk;
        this.micronautValidation = micronautValidation;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeatureIfNotPresent(AwsV2Sdk.class, awsV2Sdk);
        featureContext.addFeatureIfNotPresent(ValidationFeature.class, micronautValidation);
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        ModuleContext module = generatorContext.getRootModule();
        OpenRewriteFeature.super.apply(generatorContext);

        String repositoryFile = generatorContext.getSourcePath("/{packagePath}/DynamoRepository");
        module.addTemplate(generatorContext.getOptions().language(), "dynamoRepository", repositoryFile,
                dynamodbRepositoryJava.template(generatorContext.getProject()),
                dynamodbRepositoryKotlin.template(generatorContext.getProject()),
                dynamodbRepositoryGroovy.template(generatorContext.getProject()));

        String configurationFile = generatorContext.getSourcePath("/{packagePath}/DynamoConfiguration");
        module.addTemplate(generatorContext.getOptions().language() ,"dynamoConfiguration", configurationFile,
                dynamodbConfigurationJava.template(generatorContext.getProject()),
                dynamodbConfigurationKotlin.template(generatorContext.getProject()),
                dynamodbConfigurationGroovy.template(generatorContext.getProject()));

        String ciAwsCredentialsProviderChainCondition = generatorContext.getSourcePath("/{packagePath}/CIAwsCredentialsProviderChainCondition");
        module.addTemplate(generatorContext.getOptions().language(),
            "ciAwsCredentialsProviderChainCondition", ciAwsCredentialsProviderChainCondition,
                ciawsconditionJava.template(generatorContext.getProject()),
                ciawsconditionKotlin.template(generatorContext.getProject()),
                ciawsconditionGroovy.template(generatorContext.getProject()));

        String cIAwsRegionProviderChainCondition = generatorContext.getSourcePath("/{packagePath}/CIAwsRegionProviderChainCondition");
        module.addTemplate(generatorContext.getOptions().language(),
            "cIAwsRegionProviderChainCondition", cIAwsRegionProviderChainCondition,
                ciawsregionconditionJava.template(generatorContext.getProject()),
                ciawsregionconditionKotlin.template(generatorContext.getProject()),
                ciawsregionconditionGroovy.template(generatorContext.getProject()));

        Configuration testConfig = module.testConfiguration();
        testConfig.put("aws.region", "us-east-1");
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    @NonNull
    public String getTitle() {
        return "Amazon DynamoDB";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Integrates with Amazon DynamoDB a NoSQL database service";
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return List.of("io.micronaut.starter.feature.dynamodb");
    }

}
