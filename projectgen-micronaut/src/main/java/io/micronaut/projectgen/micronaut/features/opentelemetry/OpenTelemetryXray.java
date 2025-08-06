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
package io.micronaut.projectgen.micronaut.features.opentelemetry;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.starter.feature.aws.AwsV2Sdk;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

/**
 * Feature that provides OpenTelemetry X-Ray integration for Micronaut applications.
 * This feature adds OpenTelemetry support with X-Ray as the exporter for distributed tracing,
 * including AWS SDK 2.2 instrumentation when available.
 */
@Requires(property = "micronaut.starter.feature.tracing.opentelemetry.xray.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class OpenTelemetryXray extends AbstractOpenTelemetry implements OpenRewriteFeature {
    public static final String NAME = "tracing-opentelemetry-xray";

    /**
     * Creates a new OpenTelemetryXray with the required OpenTelemetry components.
     *
     * @param otel the base OpenTelemetry feature
     * @param otelHttp the OpenTelemetry HTTP feature
     * @param otelAnnotations the OpenTelemetry annotations feature
     * @param openTelemetryGrpc the OpenTelemetry gRPC feature
     * @param otelExporter the OpenTelemetry OTLP exporter feature
     */
    public OpenTelemetryXray(OpenTelemetry otel,
        OpenTelemetryHttp otelHttp,
        OpenTelemetryAnnotations otelAnnotations,
        OpenTelemetryGrpc openTelemetryGrpc,
        OpenTelemetryExporterOtlp otelExporter) {
        super(otel, otelHttp, otelAnnotations, openTelemetryGrpc, otelExporter);
    }

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "OpenTelemetry XRay Tracing";
    }

    @NonNull
    @Override
    public String getDescription() {
        return "Adds support for distributed tracing with XRay via Open Telemetry";
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        List<String> recipes = new ArrayList<>();
        recipes.add("io.micronaut.starter.feature.tracing-opentelemetry-xray");
        if (generatorContext.getFeatures().isFeaturePresent(AwsV2Sdk.class)) {
            recipes.add("io.opentelemetry.instrumentation.opentelemetry-aws-sdk-2.2.dependency");
        }
        return recipes;
    }
}
