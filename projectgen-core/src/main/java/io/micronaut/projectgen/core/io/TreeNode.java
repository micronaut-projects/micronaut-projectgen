package io.micronaut.projectgen.core.io;

import io.micronaut.core.annotation.Introspected;

import java.util.List;

@Introspected
public record TreeNode(String path,
                       String name,
                       List<TreeNode> children) {
}
