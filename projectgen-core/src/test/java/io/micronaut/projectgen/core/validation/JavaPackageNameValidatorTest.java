package io.micronaut.projectgen.core.validation;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JavaPackageNameValidatorTest {
    private static JavaPackageNameValidator validator = new JavaPackageNameValidator();

    @ParameterizedTest
    @ValueSource(strings = {
        "com.example.app",
        "org.my_module._internal",
        "a.b.c",
        "java.util",
        "x1.y2.z3"
    })
    void testValidPackageNames(String value) {
        assertTrue(validator.isValid(value, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        ".com.example",          // starts with a dot
        "com..example",          // double dots
        "123.bad.name",          // starts with digit
        "com.while.app",         // Java keyword
        "com.example.",          // ends with a dot
        "com.@example",          // invalid character
        "com.example..test"      // double dots again
    })
    void testInvalidPackageNames(String value) {
        assertFalse(validator.isValid(value, null));
    }
}
