package milkycode.dependency_resolver;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String rootDirectoryPath = scanner.nextLine();

        DependencyTree tree;
        try {
            tree = DependencyTree.build(Path.of(rootDirectoryPath));
        } catch (IOException e) {
            System.err.println("Read error: " + e.getMessage());
            return;
        } catch (DependencyNotFoundException e) {
            System.err.println(e.getMessage());
            return;
        }

        List<FileNode> fileNodeList;
        try {
            fileNodeList = tree.getOrderedList();
        } catch (CyclicDependencyException e) {
            System.err.println("Cyclic dependency found. Printing all files");
            fileNodeList = tree.getNodes();
        }

        for (FileNode node : fileNodeList) {
            System.out.println(node.getPath());
        }
    }
}
