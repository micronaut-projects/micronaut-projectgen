package io.micronaut.projectgen.core.feature;

import io.micronaut.projectgen.core.options.GenericOptionsBuilder;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class FeatureFetcherTest {

    @Test
    void featureFetcher(FeatureFetcher featureFetcher) {
        Options options = GenericOptionsBuilder.builder().build();
        List<FeatureResponse> features = featureFetcher.fetch(options);
        assertTrue(features.isEmpty());
    }
}
