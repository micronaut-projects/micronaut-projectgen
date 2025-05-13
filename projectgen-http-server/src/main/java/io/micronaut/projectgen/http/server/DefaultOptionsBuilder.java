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
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.options.GenericOptionsBuilder;
import io.micronaut.projectgen.core.options.JdkVersion;
import io.micronaut.projectgen.core.options.Language;
import io.micronaut.projectgen.core.options.Options;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Secondary
@Singleton
public class DefaultOptionsBuilder implements OptionsBuilder {
    private static final String FIELD_NAME = "name";
    private static final String FIELD_LANG = "lang";
    private static final String FIELD_BUILD = "build";
    private static final String FIELD_JAVA = "java";
    private static final String FIELD_FEATURES = "features";

    @Override
    public Options createOptions(Map<String, Object> form) {
        return createOptionsBuilder(form).build();
    }

    protected GenericOptionsBuilder createOptionsBuilder(Map<String, Object> form) {
        GenericOptionsBuilder builder = GenericOptionsBuilder.builder();
        getField(form, FIELD_NAME).ifPresent(builder::name);

        getField(form, FIELD_BUILD)
            .flatMap(BuildTool::of)
            .map(Collections::singletonList)
            .ifPresent(builder::buildTools);

        getField(form, FIELD_LANG)
            .flatMap(Language::of)
            .ifPresent(builder::language);

        Object featuresObj = form.get(FIELD_FEATURES);

        List<String> features = new ArrayList<>();
        if (featuresObj instanceof List<?>) {
            for (Object featureObj : (List<?>) featuresObj) {
                if (featureObj instanceof String feature) {
                    features.add(feature);
                }
            }
        } else if (featuresObj instanceof String feature) {
            features.add(feature);
        }
        builder.features(features);

        getField(form, FIELD_JAVA)
            .map(JdkVersion::valueOf)
            .ifPresent(builder::java);

        Object nameObject = form.get(FIELD_NAME);
        if (nameObject != null) {
            builder = builder.name(nameObject.toString());
        }
        builder.buildTools(List.of(BuildTool.GRADLE_KOTLIN));
        builder.language(Language.JAVA);
        return builder;
    }

    private Optional<String> getField(Map<String, Object> form, String fieldName) {
        Object nameObject = form.get(fieldName);
        if (nameObject == null) {
            return Optional.empty();
        }
        return Optional.of(nameObject.toString());
    }
}
