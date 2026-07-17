/*
 * Copyright 2017-2024 original authors
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
package io.micronaut.starter.feature.database;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.projectgen.core.generator.ModuleContext;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.buildtools.dependencies.MavenCoordinate;
import io.micronaut.starter.feature.Category;
import io.micronaut.projectgen.core.feature.FeatureContext;
import io.micronaut.projectgen.core.feature.OneOfFeature;
import io.micronaut.starter.feature.database.jdbc.JdbcFeature;
import io.micronaut.starter.feature.database.r2dbc.R2dbc;
import io.micronaut.starter.feature.database.r2dbc.R2dbcFeature;
import io.micronaut.starter.feature.migration.MigrationFeature;
import io.micronaut.starter.feature.testresources.DbType;
import io.micronaut.starter.feature.testresources.EaseTestingFeature;
import io.micronaut.starter.feature.testresources.TestResources;
import io.micronaut.starter.feature.testresources.TestResourcesAdditionalModulesProvider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static io.micronaut.starter.buildtools.dependencies.MicronautDependencyUtils.GROUP_ID_MICRONAUT_TESTRESOURCES;

/**
 * Abstract base class for database driver features.
 *
 * <p>Handles dependency management, configuration, and integration of JDBC, R2DBC,
 * Hibernate, migration tools, and test resources for Micronaut projects.
 * Provides helper methods for feature selection, dependency resolution, and
 * implementation of database-specific logic.
 */
public abstract class DatabaseDriverFeature extends EaseTestingFeature implements OneOfFeature, DatabaseDriverFeatureDependencies, TestResourcesAdditionalModulesProvider {

    @Nullable
    private final JdbcFeature jdbcFeature;

    public DatabaseDriverFeature() {
        this(null, null, null);
    }

    public DatabaseDriverFeature(@Nullable JdbcFeature jdbcFeature,
        @Nullable TestContainers testContainers,
        @Nullable TestResources testResources) {
        super(testContainers, testResources);
        this.jdbcFeature = jdbcFeature;
    }

    @Override
    public Class<?> getFeatureClass() {
        return DatabaseDriverFeature.class;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        super.processSelectedFeatures(featureContext);
        JdbcFeature feature = jdbcFeature;
        if (feature != null && shouldAddJdbcFeature(featureContext)) {
            featureContext.addFeature(feature);
        }
    }

    private boolean shouldAddJdbcFeature(FeatureContext featureContext) {
        return !featureContext.isPresent(JdbcFeature.class)
            && !featureContext.isPresent(R2dbcFeature.class)
            && !hasHibernateReactiveWithoutMigration(featureContext)
;
    }

    private boolean hasHibernateReactiveWithoutMigration(FeatureContext featureContext) {
        return featureContext.isPresent(HibernateReactiveFeature.class) && !featureContext.isPresent(MigrationFeature.class);
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }

    public abstract boolean embedded();

    @Nullable
    public abstract String getJdbcUrl();

    @Nullable
    public abstract String getR2dbcUrl();

    @Nullable
    public abstract String getDriverClass();

    public abstract String getDefaultUser();

    public abstract String getDefaultPassword();

    public abstract String getDataDialect();

    /**
     * Returns the database type if applicable.
     * <p>
     * Subclasses may override this to provide a specific DbType.
     * Ensure consistency with other database-related features.
     * @return An {@link Optional} containing the {@link DbType}, or empty if not applicable.
     */
    @NonNull
    public Optional<DbType> getDbType() {
        return Optional.empty();
    }

    @Override
    @NonNull
    public List<String> getTestResourcesAdditionalModules(@NonNull GeneratorContext generatorContext) {
        if (
            !generatorContext.isFeaturePresent(Data.class) || generatorContext.isFeaturePresent(HibernateReactiveFeature.class)
        ) {
            return getDbType().map(dbType -> {
                if (generatorContext.isFeaturePresent(R2dbc.class)) {
                    return Collections.singletonList(dbType.getR2dbcTestResourcesModuleName());
                } else if (generatorContext.isFeaturePresent(HibernateReactiveFeature.class)) {
                    return Collections.singletonList(dbType.getHibernateReactiveTestResourcesModuleName());
                } else {
                    return Collections.singletonList(dbType.getJdbcTestResourcesModuleName());
                }
            }).orElseGet(Collections::emptyList);
        }
        return Collections.emptyList();
    }

    @Override
    @NonNull
    public List<MavenCoordinate> getTestResourcesDependencies(@NonNull GeneratorContext generatorContext) {
        List<MavenCoordinate> dependencies = new ArrayList<>();
        if (
            !generatorContext.isFeaturePresent(Data.class) || generatorContext.isFeaturePresent(HibernateReactiveFeature.class) || generatorContext.isFeaturePresent(R2dbc.class)
        ) {
            getDbType()
                .map(dbType -> {
                    if (generatorContext.isFeaturePresent(R2dbc.class)) {
                        return dbType.getR2dbcTestResourcesModuleName();
                    } else if (generatorContext.isFeaturePresent(HibernateReactiveFeature.class)) {
                        return dbType.getHibernateReactiveTestResourcesModuleName();
                    } else {
                        return dbType.getJdbcTestResourcesModuleName();
                    }
                })
                .map(resourceName -> new MavenCoordinate(GROUP_ID_MICRONAUT_TESTRESOURCES, "micronaut-test-resources-" + resourceName, null))
                .ifPresent(dependencies::add);
        }
        if ((generatorContext.isFeaturePresent(HibernateReactiveFeature.class) || generatorContext.isFeaturePresent(R2dbc.class))
            && generatorContext.isFeaturePresent(DatabaseDriverFeature.class)
            && !generatorContext.isFeaturePresent(MigrationFeature.class)
        ) {
            generatorContext.getFeature(DatabaseDriverFeature.class)
                .flatMap(DatabaseDriverFeatureDependencies::getJavaClientDependency)
                .map(Dependency.Builder::build)
                .ifPresent(driver -> dependencies.add(new MavenCoordinate(Objects.requireNonNull(driver.getGroupId()), driver.getArtifactId(), null)));
        }
        return dependencies;
    }

    /**
     * Provides additional configuration specific to the database driver feature.
     *
     * @param generatorContext the context of the project generation
     * @return a map containing additional configuration properties; empty by default
     */
    public Map<String, Object> getAdditionalConfig(GeneratorContext generatorContext) {
        return Collections.emptyMap();
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        ModuleContext module = generatorContext.getRootModule();
        parseDependencies(generatorContext).forEach(module::addDependency);
    }

    /**
     * Parses and returns a list of dependencies required for the database driver feature
     * based on the provided GeneratorContext.
     *
     * <p>The method considers various features such as R2DBC, Hibernate Reactive, and
     * Migration to determine the necessary dependencies. It uses the GeneratorContext
     * to check for the presence of specific features and accordingly adds the required
     * dependencies to the list.</p>
     *
     * @param generatorContext the context of the project generation
     * @return a non-null list of Dependency.Builder objects representing the dependencies
     *         required for the database driver feature
     */
    @NonNull
    protected List<Dependency.Builder> parseDependencies(GeneratorContext generatorContext) {
        List<Dependency.Builder> dependencies = new ArrayList<>();
        if (generatorContext.isFeaturePresent(R2dbc.class)) {
            getR2DbcDependency().ifPresent(dependencies::add);
            if (!generatorContext.isFeaturePresent(MigrationFeature.class)) {
                return dependencies;
            }
        }
        if (generatorContext.getFeatures().hasFeature(HibernateReactiveFeature.class)) {
            dependencies.addAll(dependenciesForHibernateReactive(generatorContext));
        } else {
            getJavaClientDependency().ifPresent(dependencies::add);
        }
        return dependencies;
    }

    /**
     * Returns a list of dependencies required for Hibernate Reactive based on the provided GeneratorContext.
     *
     * <p>This method considers the presence of the Migration feature to determine whether to include
     * the Java client dependency in addition to the Hibernate Reactive Java client dependency.</p>
     *
     * @param generatorContext the context of the project generation, which is used to check for the presence of specific features
     * @return a non-null list of Dependency.Builder objects representing the dependencies required for Hibernate Reactive
     */
    @NonNull
    protected List<Dependency.Builder> dependenciesForHibernateReactive(@NonNull GeneratorContext generatorContext) {
        List<Dependency.Builder> dependencies = new ArrayList<>();
        getHibernateReactiveJavaClientDependency().ifPresent(dependencies::add);
        if (generatorContext.isFeaturePresent(MigrationFeature.class)) {
            getJavaClientDependency().ifPresent(dependencies::add);
        }
        return dependencies;
    }
}
