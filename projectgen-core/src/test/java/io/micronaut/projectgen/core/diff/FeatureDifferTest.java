package io.micronaut.projectgen.core.diff;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.buildtools.gradle.Gradle;
import io.micronaut.projectgen.core.buildtools.gradle.GradleDsl;
import io.micronaut.projectgen.core.feature.DefaultFeature;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.options.GenericOptionsBuilder;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Property(name = "spec.name", value = "FeatureDifferTest")
@MicronautTest(startApplication = false)
class FeatureDifferTest {

    @Test
    void featureDiff(FeatureDiffer featureDiffer) throws Exception {
        Options options = GenericOptionsBuilder.builder().name("demo")
            .name("demo")
            .buildTools(List.of(BuildTool.GRADLE))
            .gradleDsl(GradleDsl.KOTLIN)
            .features(List.of("geb-core"))
            .build();
        String diff = featureDiffer.diff(options);
        assertNotNull(diff);
        assertEquals("""
            --- build.gradle.kts
            +++ build.gradle.kts
            @@ -1,0 +1,3 @@
            +dependencies {
            +    testImplementation("org.gebish:geb-core:7.0")
            +}


            """, diff);
    }

    @Requires(property = "spec.name", value = "FeatureDifferTest")
    @Singleton
    static class GradleBuildDefaultFeature implements DefaultFeature {

        @Override
        public String getName() {
            return "geb-default-feature";
        }

        @Override
        public boolean isVisible() {
            return false;
        }

        @Override
        public boolean shouldApply(Options options, Set<Feature> selectedFeatures) {
            return true;
        }
    }

    @Requires(property = "spec.name", value = "FeatureDifferTest")
    @Singleton
    static class GebFeature implements Feature {

        @Override
        public String getName() {
            return "geb-core";
        }

        @Override
        public void apply(GeneratorContext generatorContext) {
            generatorContext.getRootModule().addDependency(Dependency.builder()
                    .groupId("org.gebish")
                    .artifactId("geb-core")
                    .version("7.0")
                    .test()
                .build());
        }
    }

}
