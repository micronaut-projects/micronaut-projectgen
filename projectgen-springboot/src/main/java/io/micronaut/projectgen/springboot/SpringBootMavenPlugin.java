/*
 * Copyright 2017-2023 original authors
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
package io.micronaut.projectgen.springboot;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.buildtools.maven.MavenPlugin;
import io.micronaut.projectgen.core.buildtools.maven.MavenSpecificFeature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.utils.OptionUtils;
import jakarta.inject.Singleton;

@Requires(property = "micronaut.projectgen.springboot.features.springboot.maven.plugin.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class SpringBootMavenPlugin implements MavenSpecificFeature {

    @Override
    public String getName() {
        return "spring-boot-maven-plugin";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public String getTitle() {
        return "Spring Boot Maven Plugin";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (OptionUtils.hasMavenBuildTool(generatorContext.getOptions())) {
            generatorContext.addBuildPlugin(MavenPlugin.builder()
                .groupId(SpringBootDependencies.GROUP_ID_ORG_SPRINGFRAMEWORK_BOOT)
                .artifactId("spring-boot-maven-plugin")
                .build());
        }
    }

    @Override
    public boolean supports(Options options) {
        return true;
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.spring.io/spring-boot/docs/3.1.1/maven-plugin/reference/html/";
    }
}
