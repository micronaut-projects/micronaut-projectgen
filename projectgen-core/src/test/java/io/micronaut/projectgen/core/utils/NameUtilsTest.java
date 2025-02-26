package io.micronaut.projectgen.core.utils;

import io.micronaut.projectgen.core.generator.Project;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class NameUtilsTest {

    @ParameterizedTest
    @MethodSource("nameParseArguments")
    void testProjectNaturalName(String name, String expectedNaturalName, String expectedPropertyName) {
        Project project = NameUtils.parse(name);
        assertEquals(expectedNaturalName, project.getNaturalName());
        assertEquals(expectedPropertyName, project.getPropertyName());
    }

    private static Stream<Arguments> nameParseArguments() {
        return Stream.of(
            Arguments.of("raw-tomatoes", "Raw Tomatoes", "raw-tomatoes"),
            Arguments.of("my-raw-tomatoes", "My Raw Tomatoes", "my-raw-tomatoes"),
            Arguments.of("aName", "A Name", "aName"),
            Arguments.of("name", "Name", "name"),
            Arguments.of("firstName", "First Name", "firstName"),
            Arguments.of("URL", "URL", "URL"),
            Arguments.of("localURL", "Local URL", "localURL"),
            Arguments.of("URLLocal", "URL Local", "URLLocal"),
            Arguments.of("aURLLocal", "A URL Local", "aURLLocal"),
            Arguments.of("MyDomainClass", "My Domain Class", "myDomainClass"),
            Arguments.of("com.myco.myapp.MyDomainClass", "My Domain Class", "myDomainClass")
        );
    }
}
