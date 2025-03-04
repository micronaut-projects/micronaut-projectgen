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
package io.micronaut.projectgen.micronaut.features.test;

import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.core.feature.TestFeature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.options.TestFramework;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.features.maven.GroovyMavenPlusPlugin;
import jakarta.inject.Singleton;

@Singleton
public class MicronautTestSpock implements TestFeature {
    private static final String GROUP_ID_GROOVY = "org.apache.groovy";
    private static final String GROUP_ID_SPOCKFRAMEWORK = "org.spockframework";
    private static final String ARTIFACT_ID_SPOCK_CORE = "spock-core";
    private static final String ARTIFACT_ID_GROOVY_ALL = "groovy-all";
    private static final Dependency DEPENDENCY_MICRONAUT_INJECT_GROOVY = Dependency.builder()
        .groupId("io.micronaut")
        .artifactId("micronaut-inject-groovy")
        .test()
        .build();

    private static final Dependency DEPENDENCY_MICRONAUT_TEST_SPOCK = Dependency.builder()
        .groupId("io.micronaut.test")
        .artifactId("micronaut-test-spock")
        .test()
        .build();

    private static final Dependency DEPENDENCY_SPOCK_CORE_EXCLUDING_GROOVY_ALL = Dependency.builder()
        .groupId(GROUP_ID_SPOCKFRAMEWORK)
        .artifactId(ARTIFACT_ID_SPOCK_CORE)
        .exclude(Dependency.builder()
            .groupId(GROUP_ID_GROOVY)
            .artifactId(ARTIFACT_ID_GROOVY_ALL)
            .build())
        .test()
        .build();

    private final GroovyMavenPlusPlugin groovyMavenPlusPlugin;

    public MicronautTestSpock(GroovyMavenPlusPlugin groovyMavenPlusPlugin) {
        this.groovyMavenPlusPlugin = groovyMavenPlusPlugin;
    }

    @Override
    public TestFramework getTestFramework() {
        return TestFramework.SPOCK;
    }

    @Override
    public String getName() {
        return "spock";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (OptionUtils.hasMavenBuildTool(featureContext.getOptions())) {
            featureContext.addFeature(groovyMavenPlusPlugin);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        // Only for Maven, these dependencies are applied by the Micronaut Gradle Plugin
        if (OptionUtils.hasMavenBuildTool(generatorContext.getOptions())) {
            if (generatorContext.getLanguage() != Language.GROOVY) {
                generatorContext.addDependency(DEPENDENCY_MICRONAUT_INJECT_GROOVY);
            }
            generatorContext.addDependency(DEPENDENCY_SPOCK_CORE_EXCLUDING_GROOVY_ALL);
            generatorContext.addDependency(DEPENDENCY_MICRONAUT_TEST_SPOCK);
        }
    }
}
