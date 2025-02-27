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
package io.micronaut.projectgen.core.buildtools.maven;

import com.fizzed.rocker.RockerModel;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.buildtools.RepositoryResolver;
import io.micronaut.projectgen.core.feature.BuildFeature;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.core.rocker.RockerTemplate;
import io.micronaut.projectgen.core.template.BinaryTemplate;
import io.micronaut.projectgen.core.template.Template;
import io.micronaut.projectgen.core.template.URLTemplate;
import io.micronaut.projectgen.core.utils.OptionUtils;
import jakarta.inject.Singleton;
import java.util.Set;
import io.micronaut.projectgen.core.template.genericPom;

/**
 * Maven Feature.
 */
@Requires(property = "micronaut.starter.feature.maven.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Maven implements BuildFeature {
    public static final String MICRONAUT_MAVEN_DOCS_URL = "https://micronaut-projects.github.io/micronaut-maven-plugin/latest/";
    protected static final String WRAPPER_JAR = ".mvn/wrapper/maven-wrapper.jar";
    protected static final String WRAPPER_PROPS = ".mvn/wrapper/maven-wrapper.properties";
    protected static final String MAVEN_PREFIX = "maven/";
    protected final MavenBuildCreator dependencyResolver;
    protected final RepositoryResolver repositoryResolver;

    public Maven(MavenBuildCreator dependencyResolver, RepositoryResolver repositoryResolver) {
        this.dependencyResolver = dependencyResolver;
        this.repositoryResolver = repositoryResolver;
    }

    @Override
    @NonNull
    public String getName() {
        return "maven";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addMavenWrapper(generatorContext);
        generatePom(generatorContext);
    }

    @Override
    public boolean shouldApply(Options options,
                               Set<Feature> selectedFeatures) {
        return OptionUtils.hasMavenBuildTool(options);
    }

    @Override
    public boolean isMaven() {
        return true;
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://maven.apache.org/guides/index.html";
    }

    /**
     *
     * @param generatorContext Generator Context
     */
    protected void generatePom(GeneratorContext generatorContext) {
        MavenBuild mavenBuild = createBuild(generatorContext);
        ParentPom parentPom = generatorContext.getFeature(ParentPomFeature.class).map(ParentPomFeature::getParentPom).orElse(null);
        RockerModel rockerModel = genericPom.template(parentPom, mavenBuild);
        generatorContext.addTemplate("mavenPom", new RockerTemplate("pom.xml", rockerModel));
    }

    /**
     *
     * @param generatorContext Generator Context
     */
    protected void addMavenWrapper(GeneratorContext generatorContext) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        generatorContext.addTemplate("mavenWrapperJar", new BinaryTemplate(Template.ROOT, WRAPPER_JAR, classLoader.getResource(MAVEN_PREFIX + WRAPPER_JAR)));
        generatorContext.addTemplate("mavenWrapperProperties", new URLTemplate(Template.ROOT, WRAPPER_PROPS, classLoader.getResource(MAVEN_PREFIX + WRAPPER_PROPS)));
        generatorContext.addTemplate("mavenWrapper", new URLTemplate(Template.ROOT, "mvnw", classLoader.getResource(MAVEN_PREFIX + "mvnw"), true));
        generatorContext.addTemplate("mavenWrapperBat", new URLTemplate(Template.ROOT, "mvnw.bat", classLoader.getResource(MAVEN_PREFIX + "mvnw.cmd"), false));
    }

    /**
     *
     * @param generatorContext Generator Context
     * @return Maven Build
     */
    protected MavenBuild createBuild(GeneratorContext generatorContext) {
        return dependencyResolver.create(generatorContext, repositoryResolver.resolveRepositories(generatorContext));
    }
}
