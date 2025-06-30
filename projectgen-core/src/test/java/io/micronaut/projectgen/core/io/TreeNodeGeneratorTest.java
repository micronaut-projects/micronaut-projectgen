package io.micronaut.projectgen.core.io;

import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.options.GenericOptionsBuilder;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class TreeNodeGeneratorTest {
    @Test
    void preview(TreeNodeGenerator treeNodeGenerator) throws Exception {
        Options options = GenericOptionsBuilder.builder()
            .packageName("com.example")
            .buildTools(List.of(BuildTool.GRADLE))
            .name("demo")
            .build();
        TreeNode node = treeNodeGenerator.generate(options);
        assertNotNull(node);
        List<TreeNode> children = new ArrayList<>();
        children.add(
            new TreeNode("gradle", "gradle", List.of(
                new TreeNode("gradle/wrapper", "wrapper", List.of(
                    new TreeNode("gradle/wrapper/gradle-wrapper.jar", "gradle-wrapper.jar", Collections.emptyList()),
                    new TreeNode("gradle/wrapper/gradle-wrapper.properties", "gradle-wrapper.properties", Collections.emptyList())
                ))
            )));
        children.add(new TreeNode("gradlew", "gradlew", Collections.emptyList()));
        children.add(new TreeNode("gradlew.bat", "gradlew.bat", Collections.emptyList()));
        children.add(new TreeNode("build.gradle", "build.gradle", Collections.emptyList()));
        children.add(new TreeNode("settings.gradle", "settings.gradle", Collections.emptyList()));
        TreeNode expected = new TreeNode("", "", children);
        assertEquals(expected, node);
    }
}
