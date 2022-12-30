package milkycode.dependency_resolver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter root path: ");
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

        List<FileNode> fileNodeList = getFileNodeList(tree);

        for (FileNode node : fileNodeList) {
            try {
                outputFile(node.getPath());
            } catch (IOException e) {
                System.err.println("Failed to print file: '" + node.getPath() + "'");
            }
        }
    }

    private static List<FileNode> getFileNodeList(DependencyTree tree) {
        try {
            return tree.getOrderedList();
        } catch (CyclicDependencyException e) {
            System.err.println("Cyclic dependency found:");
            for (FileNode node : e.getCycledNodes()) {
                System.err.println(node.getPath());
            }
            System.err.println("Printing all nodes:");
            return tree.getNodes();
        }
    }

    private static void outputFile(Path path) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            reader.lines().forEachOrdered(System.out::println);
        }
    }
}
