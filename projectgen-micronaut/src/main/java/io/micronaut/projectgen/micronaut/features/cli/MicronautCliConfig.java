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
package io.micronaut.projectgen.micronaut.features.cli;

import java.util.List;

public class MicronautCliConfig {
    private String applicationType;
    private String testFramework;
    private String defaultPackage;
    private String sourceLanguage;
    private List<String> features;
    private String buildTool;

    /**
     * Returns the application type.
     *
     * @return the application type
     */
    public String getApplicationType() {
        return applicationType;
    }

    /**
     * Sets the application type.
     *
     * @param applicationType the application type
     */
    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

    /**
     * Returns the test framework.
     *
     * @return the test framework
     */
    public String getTestFramework() {
        return testFramework;
    }

    /**
     * Sets the test framework.
     *
     * @param testFramework the test framework
     */
    public void setTestFramework(String testFramework) {
        this.testFramework = testFramework;
    }

    /**
     * Returns the default package name.
     *
     * @return the default package
     */
    public String getDefaultPackage() {
        return defaultPackage;
    }

    /**
     * Sets the default package name.
     *
     * @param defaultPackage the default package
     */
    public void setDefaultPackage(String defaultPackage) {
        this.defaultPackage = defaultPackage;
    }

    /**
     * Returns the source language.
     *
     * @return the source language
     */
    public String getSourceLanguage() {
        return sourceLanguage;
    }

    /**
     * Sets the source language.
     *
     * @param sourceLanguage the source language
     */
    public void setSourceLanguage(String sourceLanguage) {
        this.sourceLanguage = sourceLanguage;
    }

    /**
     * Returns the list of features.
     *
     * @return the list of features
     */
    public List<String> getFeatures() {
        return features;
    }

    /**
     * Sets the list of features.
     *
     * @param features the list of features
     */
    public void setFeatures(List<String> features) {
        this.features = features;
    }

    /**
     * Returns the build tool.
     *
     * @return the build tool
     */
    public String getBuildTool() {
        return buildTool;
    }

    /**
     * Sets the build tool.
     *
     * @param buildTool the build tool
     */
    public void setBuildTool(String buildTool) {
        this.buildTool = buildTool;
    }
}
