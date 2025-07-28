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
package io.micronaut.starter.feature.picocli.test.spock;

import com.fizzed.rocker.RockerModel;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.generator.Project;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.micronaut.template.picocli.test.spock.picocliSpockTest;
import io.micronaut.starter.feature.picocli.test.PicocliTestFeature;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.options.TestFramework;
import io.micronaut.projectgen.core.rocker.RockerTemplate;

import jakarta.inject.Singleton;

@Requires(property = "micronaut.starter.feature.picocli.spock.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class PicocliSpock implements PicocliTestFeature {

    @Override
    public String getName() {
        return "picocli-spock";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        ModuleContext module = generatorContext.getRootModule();
        module.addTemplate("picocliSpock", getTemplate(generatorContext.getProject()));
    }

    @Override
    public TestFramework getTestFramework() {
        return TestFramework.SPOCK;
    }

    /**
     * Returns the {@link RockerModel} for generating a Picocli Spock test.
     *
     * @param project The project metadata.
     * @return The Rocker model for Spock test generation.
     */
    public RockerModel getModel(Project project) {
        return picocliSpockTest.template(project);
    }

    /**
     * Returns the {@link RockerTemplate} for the Picocli Spock test source file.
     *
     * @param project The project metadata.
     * @return The Rocker template for rendering the Spock test.
     */
    public RockerTemplate getTemplate(Project project) {
        return new RockerTemplate(getTestFramework().getSourcePath(PATH, Language.GROOVY), getModel(project));
    }

}
