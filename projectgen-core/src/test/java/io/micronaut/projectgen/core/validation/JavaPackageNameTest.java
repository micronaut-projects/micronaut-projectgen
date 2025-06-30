package io.micronaut.projectgen.core.validation;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Property(name = "spec.name", value = "JavaPackageNameTest")
@MicronautTest(startApplication = false)
class JavaPackageNameTest {

    @Test
    void youCanValidateAJavaPackageNameWithTheAnnotation(CustomValidator customValidator) {
        assertDoesNotThrow(() -> customValidator.validate("com.example"));
        assertThrows(ConstraintViolationException.class,
            () -> customValidator.validate(".com.example"));
    }
    @Introspected
    record Project(@JavaPackageName String packageName) {

    }
    @Requires(property = "spec.name", value = "JavaPackageNameTest")
    @Singleton
    static class CustomValidator {
        void validate(@JavaPackageName String packageName) {

        }
    }
}
