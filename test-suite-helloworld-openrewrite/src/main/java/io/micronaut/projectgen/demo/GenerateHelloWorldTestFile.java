package io.micronaut.projectgen.demo;

import io.micronaut.projectgen.core.options.GenericOptionsBuilder;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.openrewrite.ProjectGenPropertiesScanningRecipe;
import org.openrewrite.*;
import org.openrewrite.text.PlainText;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

public class GenerateHelloWorldTestFile extends ProjectGenPropertiesScanningRecipe {
    @Override
    public String getDisplayName() {
        return "projectgen-properties-scanning";
    }

    @Override
    public String getDescription() {
        return "It reads projectgen.properties and generates additional source files.";
    }
    public static String PATH = "src/test/java/com/example/HelloWorldTest.java";
    private AtomicBoolean done = new AtomicBoolean(false);

    @Override
    public Collection<SourceFile> generate(GenericOptionsBuilder optionsBuilder,
                                           ExecutionContext ctx) {
        Options options = optionsBuilder.build();
        Path path = projectDir != null ? projectDir.resolve(PATH) : Path.of(PATH);
        System.out.println("Path: " + path);
        if (!done.get()) {
            done.compareAndSet(false, true);
            PlainText plainText = PlainText.builder()
                .text(fileContents(options))
                .sourcePath(path)
                .build();
            return Collections.singletonList(plainText);
        }
        return Collections.emptyList();
    }

    public static String fileContents(Options options) {
        return String.format("""
            package %s;

            import org.junit.jupiter.api.Test;

            import static org.junit.jupiter.api.Assertions.assertEquals;

            class HelloWorldTest {

                @Test
                void testHello() {
                    assertEquals("Hello, World!", HelloWorld.hello());
                }
            }""", options.packageName());
    }

}
