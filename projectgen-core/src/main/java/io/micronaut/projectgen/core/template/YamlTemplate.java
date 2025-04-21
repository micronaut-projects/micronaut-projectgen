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
package io.micronaut.projectgen.core.template;

import io.micronaut.projectgen.core.feature.config.Configuration;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Pattern;

import static io.micronaut.projectgen.core.feature.config.Configuration.PREFIXES;

public class YamlTemplate extends DefaultTemplate {

    private static final Pattern DOT_PATTERN = Pattern.compile("\\.");
    private final Map<String, Object> config;

    public YamlTemplate(String path, Map<String, Object> config) {
        this(DEFAULT_MODULE, path, config);
    }

    public YamlTemplate(String module, String path, Map<String, Object> config) {
        super(module, path);
        this.config = transform(config);
    }

    public Map<String, Object> getConfig() {
        return Collections.unmodifiableMap(config);
    }

    @Override
    public void write(OutputStream outputStream) throws IOException {
        if (!config.isEmpty()) {
            Yaml yaml = new Yaml(createOptions());
            yaml.dump(config, new OutputStreamWriter(outputStream));
        } else {
            outputStream.write("# Place application configuration here".getBytes());
        }
    }

    protected DumperOptions createOptions() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        return options;
    }

    @SuppressWarnings("unchecked")
    protected Map<String, Object> transform(Map<String, Object> config) {
        Map<String, Object> transformed = new LinkedHashMap<>();
        Function<String, Boolean> SKIP_KEY = s -> PREFIXES.stream().anyMatch(s::startsWith);
        for (Map.Entry<String, Object> entry: config.entrySet()) {
            Map<String, Object> finalMap = transformed;
            String key = entry.getKey();
            Object value = entry.getValue();
            int index = key.indexOf('.');
            if (index != -1) {
                String[] keys = DOT_PATTERN.split(key);
                if (!"micronaut".equals(keys[0]) && config.keySet().stream().filter(k -> k.startsWith(keys[0] + ".")).count() == 1) {
                    if (!SKIP_KEY.apply(key)) {
                        finalMap.put(key, value);
                    }
                } else {
                    for (int i = 0; i < keys.length - 1; i++) {
                        String subKey = keys[i];

                        if (!finalMap.containsKey(subKey)) {
                            if (!SKIP_KEY.apply(subKey)) {
                                finalMap.put(subKey, new LinkedHashMap<>());
                            }
                        }
                        Object next = finalMap.get(subKey);
                        if (next instanceof Map) {
                            finalMap = (Map<String, Object>) next;
                        }
                    }
                    String k = keys[keys.length - 1];
                    if (!SKIP_KEY.apply(k)) {
                        finalMap.put(k, value);
                    }
                }
            } else {
                if (!SKIP_KEY.apply(key)) {
                    finalMap.put(key, value);
                }
            }
        }
        return transformed;
    }
}
