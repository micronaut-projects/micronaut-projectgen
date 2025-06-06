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
package io.micronaut.projectgen.http.server;

import io.micronaut.context.annotation.Secondary;
import io.micronaut.core.annotation.Internal;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.gradle.GradleDsl;
import io.micronaut.projectgen.core.options.GenericOptionsBuilder;
import io.micronaut.projectgen.core.options.JdkVersion;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.options.Options;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Default implementation of {@link OptionsBuilder}.
 */
@Secondary
@Singleton
@Internal
public class DefaultOptionsBuilder implements OptionsBuilder {
    protected static final String FIELD_NAME = "name";
    protected static final String FIELD_LANG = "lang";
    protected static final String FIELD_BUILD = "build";
    protected static final String FIELD_JAVA = "java";
    protected static final String FIELD_GRADLE_DSL = "gradleDsl";
    protected static final String FIELD_FEATURES = "features";
    protected static final String FIELD_PACKAGE_NAME = "packageName";
    protected static final String GROUP = "group";
    protected static final String FIELD_ARTIFACT = "artifact";
    protected  static final String VERSION = "version";

    @Override
    public Options createOptions(Map<String, Object> form) {
        return createOptionsBuilder(form).build();
    }

    /**
     *
     * @param form form
     * @return Builder
     */
    @NonNull
    protected GenericOptionsBuilder createOptionsBuilder(@Nullable Map<String, Object> form) {
        GenericOptionsBuilder builder = GenericOptionsBuilder.builder();
        if (form != null) {
            getField(form, VERSION).ifPresent(builder::version);
            getField(form, FIELD_ARTIFACT).ifPresent(builder::artifact);
            getField(form, GROUP).ifPresent(builder::group);
            getField(form, FIELD_PACKAGE_NAME).ifPresent(builder::packageName);
            getField(form, FIELD_NAME).ifPresent(builder::name);
            List<BuildTool> buildTools = new ArrayList<>();
            for (String bt : getFieldList(form, FIELD_BUILD)) {
                BuildTool.of(bt).ifPresent(buildTools::add);
            }
            builder.buildTools(buildTools);
            getField(form, FIELD_GRADLE_DSL)
                .map(GradleDsl::valueOf)
                .ifPresent(builder::gradleDsl);
            getField(form, FIELD_LANG)
                .flatMap(Language::of)
                .ifPresent(builder::language);
            builder.features(getFieldList(form, FIELD_FEATURES));
            getField(form, FIELD_JAVA)
                .map(JdkVersion::valueOf)
                .ifPresent(builder::java);
        }
        return builder;
    }

    private List<String> getFieldList(Map<String, Object> form, String fieldName) {
        Object resultObj = form.get(fieldName);
        List<String> result = new ArrayList<>();
        if (resultObj instanceof List<?>) {
            for (Object featureObj : (List<?>) resultObj) {
                if (featureObj instanceof String feature) {
                    result.add(feature);
                }
            }
        } else if (resultObj instanceof String feature) {
            result.addAll(Arrays.asList(feature.split(",")));
        }
        return result;
    }

    private Optional<String> getField(Map<String, Object> form, String fieldName) {
        Object nameObject = form.get(fieldName);
        if (nameObject == null) {
            return Optional.empty();
        }
        return Optional.of(nameObject.toString());
    }
}
