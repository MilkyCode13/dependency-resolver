package milkycode.dependency_resolver;

import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        DependencyTree tree;
        try {
            tree = DependencyTree.build(Path.of("root"));
        } catch (IOException | DependencyNotFoundException e) {
            throw new RuntimeException(e);
        }
        for (FileNode node : tree.getNodes()) {
            System.out.println(node.getPath());
            for (FileNode dependency : node.getDependencies()) {
                System.out.println("    " + dependency.getPath());
            }
        }

        try {
            for (FileNode node : tree.getOrderedList()) {
                System.out.println(node.getPath());
            }
        } catch (CyclicDependencyException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Fine :<");
    }
}
