package io.micronaut.projectgen.core.utils;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Introspected
public record CodeSample(String idAttribute,
                  String classAttribute,
                  String content) {

    public CodeSample(String key, String value) {
        this(toValidHtmlId(key),
            PrismLanguageHelper.getPrismLanguageClass(key),
            StringUtils.isEmpty(value) ? "" : value);
    }

    public static List<CodeSample> of(Map<String, String> project) {
        List<CodeSample> result = new ArrayList<>();
        for (Map.Entry<String, String> entry : project.entrySet()) {
            result.add(new CodeSample(entry.getKey(), entry.getValue()));
        }
        return result;
    }

    public static String toValidHtmlId(String input) {
        // 1. Replace invalid characters (anything not a-z, A-Z, 0-9, -, _, :, .) with underscores
        String result = input.replaceAll("[^a-zA-Z0-9\\-_:\\.]", "_");
        // 2. Ensure it starts with a letter (prepend 'id_' if not)
        if (!result.matches("^[a-zA-Z].*")) {
            result = "id_" + result;
        }
        return result;
    }
}
