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
package io.micronaut.projectgen.springboot;

import io.micronaut.core.util.CollectionUtils;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.options.*;

import java.util.ArrayList;
import java.util.List;

/**
 * SpringBoot Options.
 */
public final class SpringBootOptions implements Options {
    private JdkVersion javaVersion;
    private final String name;
    private final String group;
    private final String artifact;
    private final Packaging packaging;
    private final String description;
    private final String packageName;
    private final Language language;
    private final TestFramework testFramework;
    private final List<BuildTool> buildTools;
    private final OperatingSystem operatingSystem;
    private final List<String> features;

    private SpringBootOptions(JdkVersion javaVersion,
                              String name,
                              String group,
                              String artifact,
                              Packaging packaging,
                              String description,
                              String packageName,
                              Language language,
                              TestFramework testFramework,
                              List<BuildTool> buildTools,
                              OperatingSystem operatingSystem,
                              List<String> features) {
        this.javaVersion = javaVersion;
        this.name = name;
        this.group = group;
        this.artifact = artifact;
        this.packaging = packaging;
        this.description = description;
        this.packageName = packageName;
        this.language = language;
        this.testFramework = testFramework;
        this.buildTools = buildTools;
        this.operatingSystem = operatingSystem;
        this.features = features;
    }

    @Override
    public JdkVersion javaVersion() {
        return javaVersion;
    }

    @Override
    public String name() {
        return name;
    }

    public String getArtifact() {
        return artifact;
    }

    public String getGroup() {
        return group;
    }

    public Packaging getPackaging() {
        return packaging;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String packageName() {
        return packageName;
    }

    @Override
    public Language language() {
        return language;
    }

    @Override
    public TestFramework testFramework() {
        return testFramework;
    }

    @Override
    public List<BuildTool> buildTools() {
        return buildTools;
    }

    @Override
    public OperatingSystem operatingSystem() {
        return operatingSystem;
    }

    @Override
    public List<String> features() {
        return features;
    }

    /**
     *
     * @return A builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder.
     */
    public static class Builder {
        private String group;
        private String artifact;
        private String description;
        private Packaging packaging;
        private JdkVersion javaVersion;
        private String name;
        private String packageName;
        private Language language;
        private TestFramework testFramework;
        private List<BuildTool> buildTools = new ArrayList<>();
        private OperatingSystem operatingSystem;
        private List<String> features = new ArrayList<>();

        /**
         *
         * @param description description
         * @return Builder
         */
        public Builder description(String description) {
            this.description = description;
            return this;
        }

        /**
         *
         * @param group group
         * @return Builder
         */
        public Builder group(String group) {
            this.group = group;
            return this;
        }

        /**
         *
         * @param artifact artifact
         * @return Builder
         */
        public Builder artifact(String artifact) {
            this.artifact = artifact;
            return this;
        }

        /**
         *
         * @param packaging packaging
         * @return Builder
         */
        public Builder packaging(Packaging packaging) {
            this.packaging = packaging;
            return this;
        }

        /**
         *
         * @param javaVersion java version
         * @return Builder
         */
        public Builder javaVersion(JdkVersion javaVersion) {
            this.javaVersion = javaVersion;
            return this;
        }

        /**
         *
         * @param name name
         * @return Builder
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         *
         * @param packageName packageName
         * @return Builder
         */
        public Builder packageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        /**
         *
         * @param language language
         * @return Builder
         */
        public Builder language(Language language) {
            this.language = language;
            return this;
        }

        /**
         *
         * @param testFramework testFramework
         * @return Builder
         */
        public Builder testFramework(TestFramework testFramework) {
            this.testFramework = testFramework;
            return this;
        }

        /**
         *
         * @param buildTool buildTool
         * @return Builder
         */
        public Builder buildTool(BuildTool buildTool) {
            this.buildTools.add(buildTool);
            return this;
        }

        /**
         *
         * @param buildTools buildTools
         * @return Builder
         */
        public Builder buildTools(List<BuildTool> buildTools) {
            this.buildTools = buildTools;
            return this;
        }

        /**
         *
         * @param operatingSystem operatingSystem
         * @return Builder
         */
        public Builder operatingSystem(OperatingSystem operatingSystem) {
            this.operatingSystem = operatingSystem;
            return this;
        }

        /**
         *
         * @param features features
         * @return Builder
         */
        public Builder features(List<String> features) {
            this.features = features;
            return this;
        }

        /**
         *
         * @param feature feature
         * @return Builder
         */
        public Builder feature(String feature) {
            this.features.add(feature);
            return this;
        }

        /**
         *
         * @return build MicronautOptions
         */
        public SpringBootOptions build() {
            if (StringUtils.isEmpty(name)) {
                this.name = "demo";
            }
            if (StringUtils.isEmpty(packageName)) {
                this.packageName = "com.example";
            }
            if (CollectionUtils.isEmpty(buildTools)) {
                buildTools = List.of(BuildTool.GRADLE_KOTLIN);
            }
            if (language == null) {
                language = Language.JAVA;
            }
            if (testFramework == null) {
                testFramework = language.getDefaults().getTest();
            }
            if (javaVersion == null) {
                javaVersion = JdkVersion.JDK_21;
            }
            return new SpringBootOptions(javaVersion,
                name,
                group,
                artifact,
                packaging,
                description,
                packageName,
                language,
                testFramework,
                buildTools,
                operatingSystem,
                features);
        }
    }
}
