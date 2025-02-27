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
package io.micronaut.projectgen.micronaut;

import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.options.*;

import java.util.*;

/**
 * Micronaut Options.
 */
public final class MicronautOptions implements Options {
    private ApplicationType applicationType;
    private JdkVersion javaVersion;
    private String name;
    private String packageName;
    private Language language;
    private TestFramework testFramework;
    private List<BuildTool> buildTools;
    private OperatingSystem operatingSystem;
    private List<String> features;

    private MicronautOptions(ApplicationType applicationType,
                             JdkVersion javaVersion,
                             String name,
                             String packageName,
                             Language language,
                             TestFramework testFramework,
                             List<BuildTool> buildTools,
                             OperatingSystem operatingSystem,
                             List<String> features) {
        this.applicationType = applicationType;
        this.javaVersion = javaVersion;
        this.name = name;
        this.packageName = packageName;
        this.language = language;
        this.testFramework = testFramework;
        this.buildTools = buildTools;
        this.operatingSystem = operatingSystem;
        this.features = features;
    }

    /**
     *
     * @return application type
     */
    public ApplicationType applicationType() {
        return applicationType;
    }

    @Override
    public JdkVersion javaVersion() {
        return javaVersion;
    }

    @Override
    public String name() {
        return name;
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
        private ApplicationType applicationType;
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
         * @param applicationType application type
         * @return Builder
         */
        public Builder applicationType(ApplicationType applicationType) {
            this.applicationType = applicationType;
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
        public MicronautOptions build() {
            return new MicronautOptions(applicationType,
                javaVersion,
                name,
                packageName,
                language,
                testFramework,
                buildTools,
                operatingSystem,
                features);
        }
    }
}
