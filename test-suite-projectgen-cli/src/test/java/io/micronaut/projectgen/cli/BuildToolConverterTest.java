package io.micronaut.projectgen.cli;

import io.micronaut.projectgen.core.buildtools.BuildTool;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BuildToolConverterTest {

    @Test
    void buildToolConverter() throws Exception {
        BuildToolConverter converter = new BuildToolConverter();
        assertEquals(BuildTool.GRADLE, converter.convert("gradle"));
        assertEquals(BuildTool.MAVEN, converter.convert("maven"));
        assertThrows(CommandLine.TypeConversionException.class, () -> converter.convert("foo"));
    }
}
