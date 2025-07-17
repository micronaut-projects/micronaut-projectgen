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

import io.micronaut.projectgen.core.buildtools.MavenCentral;
import io.micronaut.projectgen.core.buildtools.Repository;
import io.micronaut.projectgen.core.buildtools.RequiresRepository;
import io.micronaut.projectgen.core.feature.ConfigurationFeature;
import io.micronaut.projectgen.core.feature.DefaultFeature;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.core.feature.LoggingFeature;
import io.micronaut.projectgen.core.feature.config.Properties;
import io.micronaut.projectgen.core.feature.gitignore.GitIgnore;
import io.micronaut.projectgen.core.options.TestFramework;
import io.micronaut.projectgen.micronaut.features.cli.MicronautCli;
import io.micronaut.projectgen.micronaut.features.logging.Logback;
import io.micronaut.projectgen.micronaut.features.test.MicronautTestJunit5;
import io.micronaut.projectgen.micronaut.features.test.MicronautTestSpock;

import java.util.List;

public abstract class ApplicationTypeFeature implements DefaultFeature, RequiresRepository {
    private final MicronautTestJunit5 micronautTestJunit5;
    private final MicronautTestSpock micronautTestSpock;
    private final Logback logback;
    private final GitIgnore gitIgnore;
    private final MicronautCli micronautCli;

    protected ApplicationTypeFeature(MicronautCli micronautCli,
                                     MicronautTestJunit5 micronautTestJunit5,
                                     MicronautTestSpock micronautTestSpock,
                                     Logback logback,
                                     GitIgnore gitIgnore) {
        this.micronautTestJunit5 = micronautTestJunit5;
        this.micronautTestSpock = micronautTestSpock;
        this.logback = logback;
        this.gitIgnore = gitIgnore;
        this.micronautCli = micronautCli;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeatureIfNotPresent(MicronautCli.class, micronautCli);
        featureContext.addFeatureIfNotPresent(GitIgnore.class, gitIgnore);
        featureContext.addFeatureIfNotPresent(LoggingFeature.class, logback);
        if (featureContext.getOptions().testFramework() == null ||
            featureContext.getOptions().testFramework() == TestFramework.JUNIT) {
            featureContext.addFeatureIfNotPresent(MicronautTestJunit5.class, micronautTestJunit5);
        } else if (featureContext.getOptions().testFramework() == TestFramework.SPOCK) {
            featureContext.addFeatureIfNotPresent(MicronautTestSpock.class, micronautTestSpock);
        }
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public List<Repository> getRepositories() {
        return List.of(new MavenCentral());
    }
}
