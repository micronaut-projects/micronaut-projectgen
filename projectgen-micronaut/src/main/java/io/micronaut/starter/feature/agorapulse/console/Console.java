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
package io.micronaut.starter.feature.agorapulse.console;

import com.fizzed.rocker.RockerModel;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Coordinate;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.agorapulse.AgoraPulseFeature;
import io.micronaut.projectgen.micronaut.template.agorapulse.console.consoleGroovyDsl;
import io.micronaut.projectgen.micronaut.template.agorapulse.console.consoleGroovyHttp;
import io.micronaut.projectgen.micronaut.template.agorapulse.console.consoleKotlinHttp;
import io.micronaut.starter.feature.agorapulse.worker.Worker;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.rocker.RockerTemplate;
import io.micronaut.starter.util.VersionInfo;
import jakarta.inject.Singleton;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Requires(property = "micronaut.starter.feature.agorapulse.micronaut.console.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public final class Console implements AgoraPulseFeature {

    private static final String ARTIFACT_ID = "micronaut-console";
    private static final String SSRF_HEADER_NAME = "X-Console-Verify";

    @Override
    @NonNull
    public String getCommunityFeatureName() {
        return "micronaut-console";
    }

    @Override
    @NonNull
    public String getCommunityFeatureTitle() {
        return "Micronaut Console";
    }

    @Override
    public String getDescription() {
        return "An extension to Micronaut applications and functions which allows executing arbitrary code.";
    }

    @Override
    public String getThirdPartyDocumentation(GeneratorContext generatorContext) {
        return "https://agorapulse.github.io/micronaut-console/";
    }

    @Override
    public boolean supports(Options options) {
        ApplicationType applicationType = ApplicationType.of(options.template());
        return applicationType == ApplicationType.DEFAULT;
    }

    @Override
    public String getCategory() {
        return Category.DEV_TOOLS;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        String secret = io.micronaut.projectgen.core.utils.StringUtils.randomString();
        addDependency(generatorContext);
        addExampleCode(generatorContext, secret);
        addConfiguration(generatorContext, secret);
    }

    private void addDependency(GeneratorContext generatorContext) {
        ModuleContext module = generatorContext.getRootModule();
        module.addDependency(Dependency.builder()
            .lookupArtifactId(ARTIFACT_ID)
            .developmentOnly());

        if (generatorContext.getLanguage() == Language.JAVA) {
            addGroovyDependency(generatorContext);
        } else if (generatorContext.getLanguage() == Language.KOTLIN) {
            addKotlinScriptingDependency(generatorContext);
        }
    }

    private void addGroovyDependency(GeneratorContext generatorContext) {
        ModuleContext module = generatorContext.getRootModule();
        if (OptionUtils.hasMavenBuildTool(generatorContext.getOptions())) {
            module.buildProperties().put("groovyVersion", VersionInfo.getDependencyVersion("groovy").getValue());
        }
        Dependency.Builder groovy = Dependency.builder()
            .groupId("org.apache.groovy")
            .artifactId("groovy")
            .developmentOnly();
        module.addDependency(groovy);
    }

    private void addKotlinScriptingDependency(GeneratorContext generatorContext) {
        Coordinate coordinate = generatorContext.resolveCoordinate("kotlin-bom");
        ModuleContext module = generatorContext.getRootModule();
        module.buildProperties().put("kotlinVersion", coordinate.getVersion());
        Dependency.Builder kotlin = Dependency.builder()
            .groupId("org.jetbrains.kotlin")
            .compile()
            .version("${kotlinVersion}")
            .template();
        module.addDependency(kotlin.artifactId("kotlin-scripting-jsr223").developmentOnly());
    }

    private void addExampleCode(GeneratorContext generatorContext, String secret) {
        addDslFile(generatorContext);
        addHttpFile(generatorContext, secret);
    }

    private void addDslFile(GeneratorContext generatorContext) {
        ModuleContext module = generatorContext.getRootModule();
        dslFile(generatorContext).ifPresent(rockerModel ->
            module.addTemplate("consoleGroovyDsl", new RockerTemplate("src/test/resources/console.gdsl", rockerModel)));
    }

    private void addHttpFile(GeneratorContext generatorContext, String secret) {
        ModuleContext module = generatorContext.getRootModule();
        httpFile(generatorContext, secret).ifPresent(rockerModel ->
            module.addTemplate("consoleHttpFile", new RockerTemplate("src/test/resources/console.http", rockerModel))
        );
    }

    @NonNull
    private Optional<RockerModel> dslFile(GeneratorContext generatorContext) {
        if (generatorContext.getLanguage() == Language.KOTLIN) {
            return Optional.empty();
        }
        // both Java and Groovy uses Groovy
        return Optional.of(consoleGroovyDsl.template(generatorContext.isFeaturePresent(Worker.class)));
    }

    @NonNull
    private Optional<RockerModel> httpFile(GeneratorContext generatorContext, String secret) {
        if (generatorContext.getLanguage() == Language.KOTLIN) {
            return Optional.of(consoleKotlinHttp.template(SSRF_HEADER_NAME, secret));
        }
        // both Java and Groovy uses Groovy
        return Optional.of(consoleGroovyHttp.template(SSRF_HEADER_NAME, secret));
    }

    private void addConfiguration(GeneratorContext generatorContext, String secret) {
        Map<String, Object> settings = new LinkedHashMap<>();
        settings.put("enabled", true);
        settings.put("addresses", Arrays.asList("/127.0.0.1", "/0:0:0:0:0:0:0:1"));
        settings.put("header-name", SSRF_HEADER_NAME);
        settings.put("header-value", secret);
        ModuleContext module = generatorContext.getRootModule();
        module.configuration().addNested(Collections.singletonMap("console", settings));
    }

}
