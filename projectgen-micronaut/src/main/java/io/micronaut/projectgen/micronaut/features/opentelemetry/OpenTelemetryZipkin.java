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
import jakarta.inject.Singleton;

import java.util.List;

/**
 * Feature that provides OpenTelemetry Zipkin integration for Micronaut applications.
 * This feature adds OpenTelemetry support with Zipkin as the exporter for distributed tracing.
 */
@Requires(property = "micronaut.starter.feature.tracing.opentelemetry.zipkin.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class OpenTelemetryZipkin extends AbstractOpenTelemetry implements OpenRewriteFeature {
    /**
     * Creates a new OpenTelemetryZipkin with the required OpenTelemetry components.
     *
     * @param otel the base OpenTelemetry feature
     * @param otelHttp the OpenTelemetry HTTP feature
     * @param otelAnnotations the OpenTelemetry annotations feature
     * @param openTelemetryGrpc the OpenTelemetry gRPC feature
     * @param openTelemetryExporterZipkin the OpenTelemetry Zipkin exporter feature
     */
    public OpenTelemetryZipkin(OpenTelemetry otel,
        OpenTelemetryHttp otelHttp,
        OpenTelemetryAnnotations otelAnnotations,
        OpenTelemetryGrpc openTelemetryGrpc,
        OpenTelemetryExporterZipkin openTelemetryExporterZipkin) {
        super(otel, otelHttp, otelAnnotations, openTelemetryGrpc, openTelemetryExporterZipkin);
    }

    @Override
    @NonNull
    public String getName() {
        return super.getName() + "zipkin";
    }

    @Override
    public String getTitle() {
        return "OpenTelemetry Zipkin";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "It adds Micronaut integration with OpenTelemetry and sets Zipkin as the exporter.";
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return List.of("io.micronaut.starter.feature.tracing-opentelemetry-zipkin");
    }

}
