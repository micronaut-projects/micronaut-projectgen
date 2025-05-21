package io.micronaut.monolith;

import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.sourcegen.annotations.Builder;
import jakarta.validation.constraints.NotBlank;

@Builder
@Serdeable
public record DownloadForm(@NotBlank String name) {
}
