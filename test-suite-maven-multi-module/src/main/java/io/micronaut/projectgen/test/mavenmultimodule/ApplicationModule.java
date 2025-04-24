package io.micronaut.projectgen.test.mavenmultimodule;

import io.micronaut.projectgen.core.feature.Feature;
import jakarta.inject.Singleton;

@Singleton
public class ApplicationModule implements Feature {
    @Override
    public String getName() {
        return "application";
    }
}
