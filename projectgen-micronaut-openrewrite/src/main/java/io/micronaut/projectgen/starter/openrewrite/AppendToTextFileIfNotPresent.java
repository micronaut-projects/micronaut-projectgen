package io.micronaut.projectgen.starter.openrewrite;


import org.openrewrite.*;
import org.openrewrite.text.PlainText;


public class AppendToTextFileIfNotPresent extends Recipe {

    @Option(displayName = "File pattern", description = "File path or glob pattern to match")
    String filePattern;

    @Option(displayName = "Content", description = "Content to append")
    String content;


    @Override
    public String getDisplayName() {
        return "Append to text file if not present";
    }

    @Override
    public String getDescription() {
        return "Appends content to a text file only if it doesn't already exist";
    }

    public AppendToTextFileIfNotPresent(String filePattern, String content) {
        this.filePattern = filePattern;
        this.content = content;
    }

    @Override
    public TreeVisitor<?, ExecutionContext> getVisitor() {
        return new TreeVisitor<Tree, ExecutionContext>() {
            @Override
            public Tree visit(Tree tree, ExecutionContext ctx) {
                if (tree instanceof PlainText) {
                    PlainText pt = (PlainText) tree;

                    if (pt.getSourcePath().toString().endsWith(filePattern)) {
                        String currentText = pt.getText();
                        if (!currentText.contains(content.trim())) {
                            return pt.withText(currentText + content);
                        }
                    }
                }
                return tree;
            }
        };
    }
}
