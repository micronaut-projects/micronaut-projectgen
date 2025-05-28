/*
 * Copyright 2017-2025 original authors
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
package io.micronaut.projectgen.core.io;

import io.micronaut.core.annotation.Internal;

import java.util.ArrayList;
import java.util.List;
import java.util.*;

@Internal
class TreeBuilder {
    static TreeNode buildTree(Collection<String> paths) {
        MutableNode root = new MutableNode("", "");
        for (String path : paths) {
            String[] parts = path.split("/");
            MutableNode current = root;
            StringBuilder fullPath = new StringBuilder();
            for (String part : parts) {
                if (!fullPath.isEmpty()) {
                    fullPath.append("/");
                }
                fullPath.append(part);
                current.children.putIfAbsent(part, new MutableNode(fullPath.toString(), part));
                current = current.children.get(part);
            }
        }
        return root.toImmutable();
    }

    private static class MutableNode {
        String path;
        String name;
        Map<String, MutableNode> children = new LinkedHashMap<>();

        MutableNode(String path, String name) {
            this.path = path;
            this.name = name;
        }

        TreeNode toImmutable() {
            List<TreeNode> childList = new ArrayList<>();
            for (MutableNode child : children.values()) {
                childList.add(child.toImmutable());
            }
            return new TreeNode(path, name, childList);
        }
    }
}
