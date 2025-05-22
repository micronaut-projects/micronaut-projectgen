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
package io.micronaut.projectgen.micronaut;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.projectgen.core.feature.*;
import io.micronaut.projectgen.core.feature.config.Properties;
import io.micronaut.projectgen.core.feature.gitignore.GitIgnore;
import io.micronaut.projectgen.core.options.GenericOptionsBuilder;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.javalibs.logging.Logback;
import io.micronaut.projectgen.micronaut.features.test.MicronautTestJunit5;
import io.micronaut.projectgen.micronaut.features.test.MicronautTestSpock;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Set;

@Singleton
public class ApplicationTypeCliFeature extends ApplicationTypeFeature {
    @Nullable
    private final JavaApplicationFeature javaApplicationFeature;
    @Nullable
    private final KotlinApplicationFeature kotlinApplicationFeature;
    @Nullable
    private final GroovyApplicationFeature groovyApplicationFeature;

    public ApplicationTypeCliFeature(MicronautTestJunit5 micronautTestJunit5,
                                     MicronautTestSpock micronautTestSpock,
                                     Properties properties,
                                     Logback logback,
                                     GitIgnore gitIgnore,
                                     List<JavaApplicationFeature> javaApplicationFeatures,
                                     List<KotlinApplicationFeature> kotlinApplicationFeatures,
                                     List<GroovyApplicationFeature> groovyApplicationFeatures) {
        super(micronautTestJunit5, micronautTestSpock, properties, logback, gitIgnore);
        Options options = GenericOptionsBuilder.builder().template(ApplicationType.CLI.toString()).build();
        this.javaApplicationFeature = javaApplicationFeatures.stream().filter(f -> f.supports(options)).findFirst().orElse(null);
        this.kotlinApplicationFeature = kotlinApplicationFeatures.stream().filter(f -> f.supports(options)).findFirst().orElse(null);
        this.groovyApplicationFeature = groovyApplicationFeatures.stream().filter(f -> f.supports(options)).findFirst().orElse(null);
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        super.processSelectedFeatures(featureContext);
        if (featureContext.getOptions().language() == Language.JAVA && javaApplicationFeature != null) {
            featureContext.addFeatureIfNotPresent(JavaApplicationFeature.class, javaApplicationFeature);
        }
        if (featureContext.getOptions().language() == Language.KOTLIN && kotlinApplicationFeature != null) {
            featureContext.addFeatureIfNotPresent(KotlinApplicationFeature.class, kotlinApplicationFeature);
        }
        if (featureContext.getOptions().language() == Language.GROOVY && groovyApplicationFeature != null) {
            featureContext.addFeatureIfNotPresent(GroovyApplicationFeature.class, groovyApplicationFeature);
        }
    }

    @Override
    public boolean shouldApply(Options options, Set<Feature> selectedFeatures) {
        ApplicationType applicationType = ApplicationType.of(options.template());
        return applicationType == ApplicationType.CLI;
    }

    @Override
    public String getName() {
        return "application-type-cli";
    }
}
