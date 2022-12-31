package milkycode.dependency_resolver;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A file node for dependency resolution.
 */
public class FileNode {
    private final Path path;
    private final List<FileNode> dependencies = new ArrayList<>();

    /**
     * Creates a file node from the specified path.
     *
     * @param path The path to the file.
     */
    public FileNode(Path path) {
        this.path = path;
    }

    /**
     * Gets the path to the file.
     *
     * @return The path to the file.
     */
    public Path getPath() {
        return path;
    }

    /**
     * Gets the list of dependencies.
     *
     * @return The list of dependencies.
     */
    public List<FileNode> getDependencies() {
        return Collections.unmodifiableList(dependencies);
    }

    /**
     * Adds a dependency to the node.
     *
     * @param node The node of the dependency to add.
     */
    public void addDependency(FileNode node) {
        if (node == null) {
            throw new NullPointerException();
        }

        dependencies.add(node);
    }
}
