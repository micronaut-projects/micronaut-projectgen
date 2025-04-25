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
package io.micronaut.projectgen.test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public final class ConfigurationUtils {

    public static final String SRC_MAIN_RESOURCES_APPLICATION_PROPERTIES = "src/main/resources/application.properties";
    public static final String SRC_MAIN_RESOURCES_BOOTSTRAP_PROPERTIES = "src/main/resources/bootstrap.properties";

    private ConfigurationUtils() {
    }

    public static Properties loadApplicationProperties(Map<String, String> project) throws Exception {
        return loadPropertiesByPath(project, SRC_MAIN_RESOURCES_APPLICATION_PROPERTIES);
    }

    public static Properties loadPropertiesByPath(Map<String, String> project, String path) throws Exception {
        String applicationProperties = project.get(path);
        return loadProperties(Objects.requireNonNull(applicationProperties));
    }

    public static Properties loadApplicationPropertiesByModule(Map<String, String> project, String module) throws Exception {
        return loadPropertiesByPath(project, module + "/" + SRC_MAIN_RESOURCES_APPLICATION_PROPERTIES);
    }

    public static Properties loadBootstrapProperties(Map<String, String> project) throws Exception {
        return loadPropertiesByPath(project, SRC_MAIN_RESOURCES_BOOTSTRAP_PROPERTIES);
    }

    public static Properties loadBootstrapPropertiesByModule(Map<String, String> project, String module) throws Exception {
        return loadPropertiesByPath(project, module + "/" + SRC_MAIN_RESOURCES_BOOTSTRAP_PROPERTIES);
    }

    public static Properties loadProperties(String propertiesString) throws Exception {
        Properties properties = new Properties();
        try (InputStream inputStream = new ByteArrayInputStream(propertiesString.getBytes(StandardCharsets.UTF_8))) {
            properties.load(inputStream);
        }
        return properties;
    }
}
