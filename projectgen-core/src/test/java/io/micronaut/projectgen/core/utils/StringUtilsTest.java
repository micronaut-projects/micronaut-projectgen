package io.micronaut.projectgen.core.utils;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {

    @Test
    void randomStringReturnsDifferentStrings() {
        Set<String> result = new HashSet<>();
        int count = 100;
        for (int i = 0; i < count; i++) {
            result.add(StringUtils.randomString());
        }
        assertEquals(count, result.size());
    }
}
