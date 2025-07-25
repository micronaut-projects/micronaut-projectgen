/*
 * Copyright 2017-2021 original authors
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
package io.micronaut.starter.feature.oraclecloud;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.openrewrite.OpenRewriteFeature;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.starter.feature.Category;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.core.feature.FeaturePhase;
import io.micronaut.starter.feature.database.DatabaseDriverFeature;
import io.micronaut.starter.feature.database.TestContainers;
import io.micronaut.starter.feature.database.jdbc.JdbcFeature;
import io.micronaut.starter.feature.testresources.TestResources;
import jakarta.inject.Singleton;

import java.util.List;

@Requires(property = "micronaut.starter.feature.oracle.cloud.atp.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class OracleCloudAutonomousDatabase extends DatabaseDriverFeature implements OpenRewriteFeature {

    private final OracleCloudSdk oracleCloudSdkFeature;

    public OracleCloudAutonomousDatabase(JdbcFeature jdbcFeature,
        TestContainers testContainers,
        TestResources testResources,
        OracleCloudSdk oracleCloudSdkFeature) {
        super(jdbcFeature, testContainers, testResources);
        this.oracleCloudSdkFeature = oracleCloudSdkFeature;
    }

    @NonNull
    @Override
    public String getName() {
        return "oracle-cloud-atp";
    }

    @Override
    public String getTitle() {
        return "Oracle Cloud Autonomous Transaction Processing (ATP)";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Provides integration with Oracle Cloud Autonomous Database";
    }

    @Override
    public int getOrder() {
        // need to run after the jdbc feature
        return FeaturePhase.DEFAULT.getOrder();
    }

    @Override
    public String getCategory() {
        return Category.CLOUD;
    }

    @Override
    public boolean embedded() {
        return false;
    }

    @Override
    public String getJdbcUrl() {
        return null;
    }

    @Override
    public String getR2dbcUrl() {
        return null;
    }

    @Override
    public String getDriverClass() {
        return null;
    }

    @Override
    public String getDefaultUser() {
        return "";
    }

    @Override
    public String getDefaultPassword() {
        return "";
    }

    @Override
    public String getDataDialect() {
        return "ORACLE";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        super.processSelectedFeatures(featureContext);
        if (!featureContext.isPresent(OracleCloudSdk.class)) {
            featureContext.addFeature(oracleCloudSdkFeature);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        OpenRewriteFeature.super.apply(generatorContext);
    }

    @Override
    public List<String> getRecipes(GeneratorContext generatorContext) {
        return List.of("io.micronaut.starter.feature.oracle-cloud-atp");
    }

}
