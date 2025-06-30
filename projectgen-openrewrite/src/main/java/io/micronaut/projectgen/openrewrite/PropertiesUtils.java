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
package io.micronaut.projectgen.openrewrite;

import org.openrewrite.properties.tree.Properties;
import java.util.Optional;

/**
 * Utility class to work with {@link Properties.Entry} instances.
 */
public final class PropertiesUtils {
    private PropertiesUtils() {
    }

    /**
     *
     * @param entry Entry
     * @param keyName property key name
     * @return the value associated with the property
     */
    public static Optional<String> parseValue(Properties.Entry entry, String keyName) {
        if (keyName.equals(entry.getKey())) {
            return Optional.of(entry.getValue().getText());
        }
        return Optional.empty();
    }
}
