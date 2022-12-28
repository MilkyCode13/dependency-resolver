package milkycode.dependency_resolver;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileNode {
    private final Path path;
    private final List<FileNode> dependencies = new ArrayList<>();

    public FileNode(Path path) {
        this.path = path;
    }

    public Path getPath() {
        return path;
    }

    public List<FileNode> getDependencies() {
        return dependencies;
    }

    public void addDependency(FileNode node) {
        if (node == null) {
            throw new NullPointerException();
        }

        dependencies.add(node);
    }
}
