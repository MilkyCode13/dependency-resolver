package milkycode.dependency_resolver;

import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        DependencyTree tree;
        try {
            tree = DependencyTree.build(Path.of("root"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (FileNode node : tree.getNodes()) {
            System.out.println(node.getPath());
            for (FileNode dependency : node.getDependencies()) {
                System.out.println("    " + dependency.getPath());
            }
        }

        System.out.println("Fine :<");
    }
}
